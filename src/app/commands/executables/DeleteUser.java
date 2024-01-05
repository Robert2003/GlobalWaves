package app.commands.executables;

import static app.Constants.DELETE_USER_ERROR_MESSAGE;
import static app.Constants.DELETE_USER_NO_ERROR_MESSAGE;
import static app.Constants.DOESNT_EXIST;
import static app.Constants.THE_USERNAME;

import app.audio.player.AudioPlayer;
import app.audio.player.states.PlayerPlayPauseStates;
import app.commands.Executable;
import app.helpers.UserType;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.pagination.concretepages.ArtistPage;
import app.pagination.concretepages.HostPage;
import app.pagination.enums.PageType;
import app.searchbar.SearchType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import library.Library;
import library.entities.audio.audio.collections.Album;
import library.entities.audio.audio.collections.Playlist;
import library.entities.audio.audio.collections.Podcast;
import library.entities.audio.audioFiles.Song;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

public final class DeleteUser implements Executable {
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return new DeleteUserOutputNode(command, THE_USERNAME + command.getUsername() + DOESNT_EXIST);
    }

    if (isSomeoneInteracting(user)) {
      return new DeleteUserOutputNode(command, command.getUsername() + DELETE_USER_ERROR_MESSAGE);
    }

    if (user.getUserType() == UserType.ARTIST) {
      deleteDataForArtist(user);
    } else if (user.getUserType() == UserType.HOST) {
      deleteDataForHost(user);
    } else if (user.getUserType() == UserType.NORMAL) {
      deleteDataForNormalUser(user);
    }

    Library.getInstance().getUsers().remove(user);
    return new DeleteUserOutputNode(command, command.getUsername() + DELETE_USER_NO_ERROR_MESSAGE);
  }

  private void deleteDataForArtist(final User artist) {
    for (User user : Library.getInstance().getUsers()) {
      deleteArtistSongsFromUserLikedSongs(artist, user);
      deleteSongsFromLibrary(artist);
    }
  }

  private static void deleteSongsFromLibrary(final User artist) {
    // Delete artist's songs from the library
    for (Album album : Library.getInstance().getAlbums()) {
      if (album.getOwner().equals(artist.getUsername())) {
        for (Song song : album.getSongs()) {
          Library.getInstance().getSongs().remove(song);
        }
      }
    }
  }

  private static void deleteArtistSongsFromUserLikedSongs(final User artist, final User user) {
    // Delete artist's songs from liked songs of all users
    for (Album album : Library.getInstance().getAlbums()) {
      if (album.getOwner().equals(artist.getUsername())) {
        for (Song song : album.getSongs()) {
          user.getLikedSongs().remove(song);
        }
      }
    }
  }

  private void deleteDataForHost(final User host) {
    for (User user : Library.getInstance().getUsers()) {
      // Delete page
      if (!user.equals(host) && user.getCurrentPage().equals(host.getCurrentPage())) {
        user.changePage(PageType.HOME_PAGE);
      }
    }
  }

  private void deleteDataForNormalUser(final User user) {
    for (Playlist playlist : user.getFollowedPlaylists()) {
      playlist.setFollowers(playlist.getFollowers() - 1);
    }

    for (Playlist playlist : user.getOwnedPlaylists()) {
      for (User u : Library.getInstance().getNormalUsers()) {
        u.getFollowedPlaylists().remove(playlist);
      }
    }
  }

  private boolean isSomeoneInteracting(final User userToInteract) {
    return switch (userToInteract.getUserType()) {
      case ARTIST -> isArtistInteracted(userToInteract);
      case HOST -> isHostInteracted(userToInteract);
      case NORMAL -> isNormalUserInteracted(userToInteract);
    };
  }

  private boolean isArtistInteracted(final User artist) {
    for (User user : Library.getInstance().getNormalUsers()) {
      if (isUserOnArtistPage(user, artist) || isUserPlayingArtistSong(user, artist)) {
        return true;
      }
    }
    return false;
  }

  private boolean isUserOnArtistPage(final User user, final User artist) {
    return user.getCurrentPage().getType() == PageType.ARTIST_PAGE
        && ((ArtistPage) user.getCurrentPage()).getArtistName().equals(artist.getUsername());
  }

  private boolean isUserPlayingArtistSong(final User user, final User artist) {
    if (user.getAudioPlayer().getPlayPauseState() != PlayerPlayPauseStates.STOPPED
        && user.getAudioPlayer().getLoadedTrack().getType() != SearchType.PODCAST) {
      AudioPlayer audioPlayer = user.getAudioPlayer();
      Song song = (Song) audioPlayer.getTimeManager().getPlayingAudioEntity(audioPlayer);
      return song.getArtist().equals(artist.getUsername());
    }
    return false;
  }

  private boolean isHostInteracted(final User host) {
    for (User user : Library.getInstance().getNormalUsers()) {
      if (isUserOnHostPage(user, host) || isUserPlayingHostPodcast(user, host)) {
        return true;
      }
    }
    return false;
  }

  private boolean isUserOnHostPage(final User user, final User host) {
    return user.getCurrentPage().getType() == PageType.HOST_PAGE
        && ((HostPage) user.getCurrentPage()).getHostName().equals(host.getUsername());
  }

  private boolean isUserPlayingHostPodcast(final User user, final User host) {
    if (user.getAudioPlayer().getPlayPauseState() != PlayerPlayPauseStates.STOPPED
        && user.getAudioPlayer().getLoadedTrack().getType() == SearchType.PODCAST) {
      Podcast podcast = (Podcast) user.getAudioPlayer().getLoadedTrack();
      return podcast.getOwner().equals(host.getUsername());
    }
    return false;
  }

  private boolean isNormalUserInteracted(final User normalUser) {
    for (User user : Library.getInstance().getNormalUsers()) {
      if (isUserPlayingNormalUserPlaylist(user, normalUser)) {
        return true;
      }
    }
    return false;
  }

  private boolean isUserPlayingNormalUserPlaylist(final User user, final User normalUser) {
    return user.getAudioPlayer().hasLoadedTrack()
        && (user.getAudioPlayer().getLoadedTrack().getType() == SearchType.PLAYLIST
            && ((Playlist) user.getAudioPlayer().getLoadedTrack())
                .getOwner().equals(normalUser.getUsername()));
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "message"})
  private static final class DeleteUserOutputNode extends Node {
    private String user;
    private String message;

    DeleteUserOutputNode(final InputNode command, final String message) {
      this.setCommand(command.getCommand());
      this.setTimestamp(command.getTimestamp());
      this.setUser(command.getUsername());
      this.setMessage(message);
    }
  }
}
