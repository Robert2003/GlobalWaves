package app;

public final class Constants {
  private Constants() {
  }

  public static final int PRINT_LIMIT = 5;
  public static final int FORWARD_TIME = 90;
  public static final int BACKWARD_TIME = 90;
  public static final String LOADED_SOURCE_NOT_PLAYLIST_ERROR =
      "The loaded source is not a playlist or an album.";
  public static final String SELECTED_SOURCE_NOT_PLAYLIST_ERROR =
      "The selected source is not a playlist.";
  public static final String LOADED_SOURCE_NOT_PODCAST_ERROR =
      "The loaded source is not a podcast.";
  public static final String LOADED_SOURCE_NOT_SONG_ERROR = "The loaded source is not a song.";
  public static final String PHRASE_TERMINATOR = ".";
  public static final String SELECT_ERROR_MESSAGE =
      "Please conduct a search before making a selection.";
  public static final String SELECTED_ID_TOO_HIGH_ERROR_MESSAGE = "The selected ID is too high.";
  public static final String SELECT_NO_ERROR_MESSAGE = "Successfully selected ";
  public static final String LOAD_NO_ERROR_MESSAGE = "Playback loaded successfully.";
  public static final String PLAY_NO_ERROR_MESSAGE = "Playback resumed successfully.";
  public static final String PAUSE_NO_ERROR_MESSAGE = "Playback paused successfully.";
  public static final String REPEAT_NO_ERROR_MESSAGE = "Repeat mode changed to ";
  public static final String SHUFFLE_ACTIVATE_NO_ERROR_MESSAGE =
      "Shuffle function activated successfully.";
  public static final String SHUFFLE_DEACTIVATE_NO_ERROR_MESSAGE =
      "Shuffle function deactivated successfully.";
  public static final String FORWARD_NO_ERROR_MESSAGE = "Skipped forward successfully.";
  public static final String BACKWARD_NO_ERROR_MESSAGE = "Rewound successfully.";
  public static final String NEXT_NO_ERROR_MESSAGE =
      "Skipped to next track successfully. The current track is ";
  public static final String PREV_NO_ERROR_MESSAGE =
      "Returned to previous track successfully. The current track is ";
  public static final String SWITCH_VISIBILITY_ERROR_MESSAGE =
      "The specified playlist ID is too high.";
  public static final String SWITCH_VISIBILITY_NO_ERROR_MESSAGE =
      "Visibility status updated successfully to ";
  public static final String PLAYLIST_EXISTS_ERROR_MESSAGE =
      "A playlist with the same name already exists.";
  public static final String PLAYLIST_NO_ERROR_MESSAGE = "Playlist created successfully.";
  public static final String PLAYLIST_NOT_EXISTS_ERROR_MESSAGE =
      "The specified playlist does not exist.";
  public static final String ADD_TO_PLAYLIST_NO_ERROR_MESSAGE = "Successfully added to playlist.";
  public static final String REMOVE_FROM_PLAYLIST_NO_ERROR_MESSAGE =
      "Successfully removed from playlist.";
  public static final String LIKE_NO_ERROR_MESSAGE = "Like registered successfully.";
  public static final String UNLIKE_NO_ERROR_MESSAGE = "Unlike registered successfully.";
  public static final String FOLLOW_NO_ERROR_MESSAGE = "Playlist followed successfully.";
  public static final String UNFOLLOW_NO_ERROR_MESSAGE = "Playlist unfollowed successfully.";
  public static final String FOLLOW_OWN_PLAYLIST_ERROR =
      "You cannot follow or unfollow your own playlist.";
  public static final String SEARCH_NO_ERROR_BEFORE_SIZE_MESSAGE = "Search returned ";
  public static final String SEARCH_NO_ERROR_AFTER_SIZE_MESSAGE = " results";
  public static final String SEARCH_PLAYLIST_ERROR_MESSAGE = "Search returned 0 results";
  public static final String THE_USERNAME = "The username ";
  public static final String DOESNT_EXIST = " doesn't exist.";
  public static final String NOT_ARTIST_ERROR_MESSAGE = " is not an artist.";
  public static final String HAS_ALBUM_ERROR_MESSAGE = " has another album with the same name.";
  public static final String NOT_UNIQUE_SONG_ERROR_MESSAGE =
      " has the same song at least twice in this album.";
  public static final String ADD_ALBUM_NO_ERROR_MESSAGE = " has added new album successfully.";
  public static final String NOT_HOST_ERROR_MESSAGE = " is not a host.";
  public static final String HAS_ANNOUNCEMENT_ERROR_MESSAGE =
      " has already added an announcement with this name.";
  public static final String ADD_ANNOUNCEMENT_NO_ERROR_MESSAGE =
      " has successfully added new announcement.";
  public static final String HAS_EVENT_ERROR_MESSAGE = " has another event with the same name.";
  public static final String EVENT_FOR = "Event for ";
  public static final String INVALID_DATE_ERROR_MESSAGE = " does not have a valid date.";
  public static final String ADD_EVENT_NO_ERROR_MESSAGE = " has added new event successfully.";
  public static final String HAS_MERCHANDISE_ERROR_MESSAGE = " has merchandise with the same name.";
  public static final String PRICE_ERROR_MESSAGE = "Price for merchandise can not be negative.";
  public static final String ADD_MERCHANDISE_NO_ERROR_MESSAGE =
      " has added new merchandise successfully.";
  public static final String HAS_PODCAST_ERROR_MESSAGE = " has another podcast with the same name.";
  public static final String NOT_UNIQUE_EPISODE_ERROR_MESSAGE =
      " has the same episode in this podcast.";
  public static final String ADD_PODCAST_NO_ERROR_MESSAGE = " has added new podcast successfully.";
  public static final String USERNAME_IS_TAKEN = " is already taken.";
  public static final String ADD_USER_NO_ERROR_MESSAGE = " has been added successfully.";
  public static final String CHANGE_PAGE_ERROR_MESSAGE =
      " is trying to access a non-existent page.";
  public static final String DELETE_USER_ERROR_MESSAGE = " can't be deleted.";
  public static final String DELETE_USER_NO_ERROR_MESSAGE = " was successfully deleted.";
  public static final String IS_OFFLINE_ERROR_MESSAGE = " is offline.";
  public static final String NO_ALBUM_ERROR_MESSAGE = " doesn't have an album with the given name.";
  public static final String REMOVE_ALBUM_ERROR_MESSAGE = " can't delete this album.";
  public static final String REMOVE_ALBUM_NO_ERROR_MESSAGE = " deleted the album successfully.";
  public static final String NO_ANNOUNCEMENT_ERROR_MESSAGE =
      " has no announcement with the given name.";
  public static final String REMOVE_ANNOUNCEMENT_NO_ERROR_MESSAGE =
      " has successfully deleted the announcement.";
  public static final String NO_EVENT_ERROR_MESSAGE = " doesn't have an event with the given name.";
  public static final String REMOVE_EVENT_NO_ERROR_MESSAGE = " deleted the event successfully.";
  public static final String NO_PODCAST_ERROR_MESSAGE =
      " doesn't have a podcast with the given name.";
  public static final String REMOVE_PODCAST_ERROR_MESSAGE = " can't delete this podcast.";
  public static final String REMOVE_PODCAST_NO_ERROR_MESSAGE = " deleted the podcast successfully.";
  public static final String NOT_NORMAL_USER_ERROR_MESSAGE = " is not a normal user.";
  public static final String SWITCH_CONNECTION_NO_ERROR_MESSAGE =
      " has changed status successfully.";
  private static final String SELECT_ERROR_BLUEPRINT = "Please select a source before ";
  public static final String LOAD_ERROR_MESSAGE = SELECT_ERROR_BLUEPRINT + "attempting to load.";
  public static final String FOLLOW_ERROR_MESSAGE =
      SELECT_ERROR_BLUEPRINT + "following or unfollowing.";
  private static final String LOAD_ERROR_BLUEPRINT = "Please load a source before ";
  public static final String PLAY_PAUSE_ERROR_MESSAGE =
      LOAD_ERROR_BLUEPRINT + "attempting to pause or resume playback.";
  public static final String REPEAT_ERROR_MESSAGE =
      LOAD_ERROR_BLUEPRINT + "setting the repeat status.";
  public static final String SHUFFLE_ERROR_MESSAGE =
      LOAD_ERROR_BLUEPRINT + "using the shuffle function.";
  public static final String FORWARD_ERROR_MESSAGE =
      LOAD_ERROR_BLUEPRINT + "attempting to forward.";
  public static final String BACKWARD_ERROR_MESSAGE =
      LOAD_ERROR_BLUEPRINT + "attempting to rewind.";
  public static final String NEXT_ERROR_MESSAGE =
      LOAD_ERROR_BLUEPRINT + "skipping to the next track.";
  public static final String PREV_ERROR_MESSAGE =
      LOAD_ERROR_BLUEPRINT + "returning to the previous track.";
  public static final String ADD_REMOVE_ERROR_MESSAGE =
      LOAD_ERROR_BLUEPRINT + "adding to or removing from the playlist.";
  public static final String LIKE_ERROR_MESSAGE = LOAD_ERROR_BLUEPRINT + "liking or unliking.";
}
