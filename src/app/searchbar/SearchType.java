package app.searchbar;

public enum SearchType {
  SONG,
  PODCAST,
  PLAYLIST,
  ALBUM,
  ARTIST,
  HOST,
  NOT_INITIALIZED;

  public boolean isAudio() {
    return this == SONG || this == PLAYLIST || this == PODCAST || this == ALBUM;
  }
}
