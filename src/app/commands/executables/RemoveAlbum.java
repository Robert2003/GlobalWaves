package app.commands.executables;

import static app.Constants.DOESNT_EXIST;
import static app.Constants.NOT_ARTIST_ERROR_MESSAGE;
import static app.Constants.NO_ALBUM_ERROR_MESSAGE;
import static app.Constants.REMOVE_ALBUM_ERROR_MESSAGE;
import static app.Constants.REMOVE_ALBUM_NO_ERROR_MESSAGE;
import static app.Constants.THE_USERNAME;
import static app.searchbar.SearchType.ALBUM;
import static app.searchbar.SearchType.PLAYLIST;
import static app.searchbar.SearchType.SONG;

import app.commands.Executable;
import app.helpers.UserType;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.searchbar.SearchType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import library.Library;
import library.entities.audio.audio.collections.Album;
import library.entities.audio.audio.collections.Playlist;
import library.entities.audio.audioFiles.Song;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

public final class RemoveAlbum implements Executable {
  @Override
  public Node execute(final InputNode command) {
    User artist = Library.getInstance().getUserByName(command.getUsername());

    if (artist == null) {
      return new RemoveAlbumOutputNode(
          command, THE_USERNAME + command.getUsername() + DOESNT_EXIST);
    }

    if (artist.getUserType() != UserType.ARTIST) {
      return new RemoveAlbumOutputNode(command, command.getUsername() + NOT_ARTIST_ERROR_MESSAGE);
    }

    if (!artistHasAlbum(command)) {
      return new RemoveAlbumOutputNode(
          command, command.getUsername() + NO_ALBUM_ERROR_MESSAGE);
    }

    Album album = Library.getInstance().getAlbumByName(command.getName(), command.getUsername());
    if (isSomeoneInteracting(album)) {
      return new RemoveAlbumOutputNode(
          command, command.getUsername() + REMOVE_ALBUM_ERROR_MESSAGE);
    }

    for (Song song : album.getSongs()) {
      Library.getInstance().getSongs().remove(song);
    }

    Library.getInstance().getAlbums().remove(album);

    return new RemoveAlbumOutputNode(
        command, command.getUsername() + REMOVE_ALBUM_NO_ERROR_MESSAGE);
  }

  private boolean artistHasAlbum(final InputNode command) {
    for (Album album : Library.getInstance().getAlbums()) {
      if (album.getName().equals(command.getName())
          && album.getOwner().equals(command.getUsername())) {
        return true;
      }
    }
    return false;
  }

  private boolean isSomeoneInteracting(final Album albumToBeDeleted) {
    // Parse all users and check if any of them has loaded the albumToBeDeleted
    for (User user : Library.getInstance().getNormalUsers()) {
      if (user.getAudioPlayer().hasLoadedTrack()
          && user.getAudioPlayer().getLoadedTrack().equals(albumToBeDeleted)) {
        return true;
      }

      if (user.getAudioPlayer().hasLoadedTrack() && user.getAudioPlayer().getLoadedTrack().getType() == PLAYLIST) {
        Playlist playlist = (Playlist) user.getAudioPlayer().getLoadedTrack();
        for (Song song : playlist.getSongs()) {
          if (song.getAlbum().equals(albumToBeDeleted.getName())) {
            return true;
          }
        }
      }

      if (user.getAudioPlayer().hasLoadedTrack() && user.getAudioPlayer().getLoadedTrack().getType() == ALBUM) {
        Album album = (Album) user.getAudioPlayer().getLoadedTrack();
        for (Song song : album.getSongs()) {
          if (song.getAlbum().equals(albumToBeDeleted.getName())) {
            return true;
          }
        }
      }

      if (user.getAudioPlayer().hasLoadedTrack() && user.getAudioPlayer().getLoadedTrack().getType() == SONG) {
        Song song = (Song) user.getAudioPlayer().getLoadedTrack();
        if (song.getAlbum().equals(albumToBeDeleted.getName())) {
          return true;
        }
      }
    }

    // Check if there is any playlist that contains a song of the albumToBeDeleted
    for (Song song : albumToBeDeleted.getSongs()) {
      for (Playlist playlist : Library.getInstance().getPlaylists()) {
        if (playlist.containsSong(song)) {
          return true;
        }
      }
    }

    return false;
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "message"})
  private final class RemoveAlbumOutputNode extends Node {
    private String user;
    private String message;

    RemoveAlbumOutputNode(final InputNode command, final String message) {
      this.setCommand(command.getCommand());
      this.setTimestamp(command.getTimestamp());
      this.setUser(command.getUsername());
      this.setMessage(message);
    }
  }
}
