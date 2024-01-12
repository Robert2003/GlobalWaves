package app.commands;

import lombok.Getter;

@Getter
public enum CommandTypes {
  SEARCH("search"),
  SELECT("select"),
  LOAD("load"),
  PLAY_PAUSE("playPause"),
  REPEAT("repeat"),
  SHUFFLE("shuffle"),
  LIKE("like"),
  STATUS("status"),
  FORWARD("forward"),
  BACKWARD("backward"),
  CREATE_PLAYLIST("createPlaylist"),
  ADD_REMOVE_IN_PLAYLIST("addRemoveInPlaylist"),
  SHOW_PLAYLISTS("showPlaylists"),
  SHOW_PREFERRED_SONGS("showPreferredSongs"),
  FOLLOW("follow"),
  GET_TOP_5_PLAYLISTS("getTop5Playlists"),
  SWITCH_VISIBILITY("switchVisibility"),
  GET_TOP_5_SONGS("getTop5Songs"),
  NEXT("next"),
  PREV("prev"),
  SWITCH_CONNECTION_STATUS("switchConnectionStatus"),
  GET_ONLINE_USERS("getOnlineUsers"),
  ADD_USER("addUser"),
  ADD_ALBUM("addAlbum"),
  SHOW_ALBUMS("showAlbums"),
  PRINT_CURRENT_PAGE("printCurrentPage"),
  CHANGE_PAGE("changePage"),
  GET_ALL_USERS("getAllUsers"),
  ADD_EVENT("addEvent"),
  ADD_MERCH("addMerch"),
  DELETE_USER("deleteUser"),
  ADD_PODCAST("addPodcast"),
  ADD_ANNOUNCEMENT("addAnnouncement"),
  REMOVE_ANNOUNCEMENT("removeAnnouncement"),
  SHOW_PODCASTS("showPodcasts"),
  REMOVE_ALBUM("removeAlbum"),
  REMOVE_PODCAST("removePodcast"),
  REMOVE_EVENT("removeEvent"),
  GET_TOP_5_ALBUMS("getTop5Albums"),
  GET_TOP_5_ARTISTS("getTop5Artists"),
  WRAPPED("wrapped"),
  BUY_MERCH("buyMerch"),
  SEE_MERCH("seeMerch"),
  UPDATE_RECOMMENDATIONS("updateRecommendations"),
  NEXT_PAGE("nextPage"),
  PREVIOUS_PAGE("previousPage"),
  LOAD_RECOMMENDATIONS("loadRecommendations"),
  SUBSCRIBE("subscribe"),
  GET_NOTIFICATIONS("getNotifications"),
  BUY_PREMIUM("buyPremium"),
  CANCEL_PREMIUM("cancelPremium"),
  DEFAULT("default");

  private final String description;

  CommandTypes(final String description) {
    this.description = description;
  }
}
