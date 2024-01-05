package app.helpers;

import static app.searchbar.SearchType.SONG;

import app.Constants;
import app.io.nodes.input.InputNode;
import app.io.nodes.output.PlaylistManagerOutputNode;
import library.Library;
import library.entities.audio.AudioEntity;
import library.entities.audio.audio.collections.Playlist;
import library.entities.audio.audioFiles.Song;
import library.users.User;

/** Singleton class that handles the playlists */
public final class PlaylistManager {
  private static PlaylistManager playlistManager;

  /**
   * Method used to get the instance for the singleton class
   *
   * @return the instance of the PlaylistManager
   */
  public static PlaylistManager getInstance() {
    if (PlaylistManager.playlistManager == null) {
      playlistManager = new PlaylistManager();
    }
    return PlaylistManager.playlistManager;
  }

  /**
   * Creates a new playlist based on the command from the user. It checks if a playlist with the
   * same name already exists, and adds the new playlist to the user's owned playlists and the
   * library.
   *
   * @param command The input command containing details for creating the playlist.
   * @return A PlaylistManagerOutputNode indicating the result of the playlist creation.
   */
  public PlaylistManagerOutputNode createPlaylist(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());
    assert user != null;

    Playlist newPlaylist = new Playlist(command);

    if (playlistAlreadyExists(newPlaylist)) {
      return new PlaylistManagerOutputNode(command, Constants.PLAYLIST_EXISTS_ERROR_MESSAGE);
    }

    user.getOwnedPlaylists().add(newPlaylist);
    Library.getInstance().addPlaylist(newPlaylist);
    return new PlaylistManagerOutputNode(command, Constants.PLAYLIST_NO_ERROR_MESSAGE);
  }

  /**
   * Checks if a playlist with the same name already exists in the user's owned playlists.
   *
   * @param playlist The playlist to check for existence.
   * @return true if the playlist already exists, false otherwise.
   */
  public boolean playlistAlreadyExists(final Playlist playlist) {
    User user = Library.getInstance().getUserByName(playlist.getOwner());

    assert user != null;
    for (Playlist p : user.getOwnedPlaylists()) {
      if (p.getName().equals(playlist.getName())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Retrieves a playlist at a specified index from the user's owned playlists.
   *
   * @param index The index of the playlist to retrieve.
   * @param user The user who should be the owner of the playlist.
   * @return The Playlist at the specified index, or null if the index is invalid.
   */
  public Playlist getPlaylistAtByUser(final Integer index, final User user) {
    if (index > user.getOwnedPlaylists().size()) {
      return null;
    }

    return user.getOwnedPlaylists().get(index - 1);
  }

  /**
   * Adds or removes a song to/from a user's playlist based on the command. Checks if the playlist
   * exists, ensures a track is loaded, and then adds or removes the song from the playlist.
   *
   * @param command The input command containing details for adding or removing a song.
   * @return A PlaylistManagerOutputNode indicating the result of the add/remove operation.
   */
  public PlaylistManagerOutputNode addRemoveInPlaylist(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());
    assert user != null;
    Playlist playlist = getPlaylistAtByUser(command.getPlaylistId(), user);

    if (playlist == null) {
      return new PlaylistManagerOutputNode(command, Constants.PLAYLIST_NOT_EXISTS_ERROR_MESSAGE);
    }

    if (!user.getAudioPlayer().hasLoadedTrack()) {
      return new PlaylistManagerOutputNode(command, Constants.ADD_REMOVE_ERROR_MESSAGE);
    }

    AudioEntity loadedTrack =
        user.getAudioPlayer().getTimeManager().getPlayingAudioEntity(user.getAudioPlayer());
    if (!loadedTrack.getType().equals(SONG)) {
      return new PlaylistManagerOutputNode(command, Constants.LOADED_SOURCE_NOT_SONG_ERROR);
    }

    Song song = (Song) loadedTrack;

    if (playlist.containsSong(song)) {
      playlist.removeSong(song);
      return new PlaylistManagerOutputNode(
          command, Constants.REMOVE_FROM_PLAYLIST_NO_ERROR_MESSAGE);
    }

    playlist.addSong(song);
    return new PlaylistManagerOutputNode(command, Constants.ADD_TO_PLAYLIST_NO_ERROR_MESSAGE);
  }
}
