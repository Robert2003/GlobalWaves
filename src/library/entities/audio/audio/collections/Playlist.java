package library.entities.audio.audio.collections;


import app.io.nodes.input.InputNode;
import app.searchbar.SearchType;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a playlist, which is a type of audio collection. It contains a list of songs,
 * visibility state, number of followers, playing order, and creation date.
 */
@Getter
@Setter
public final class Playlist extends SongAudioCollection {
//  private List<Song> songs;
//  private PlaylistVisibilityState visibility = PlaylistVisibilityState.PUBLIC;
//  private Integer followers = 0;
//  private List<Integer> playingOrder = null;
//  private long creationDate = 0;

  public Playlist() {
    super();
    this.setType(SearchType.PLAYLIST);
  }

  public Playlist(final InputNode command) {
    super(command);
    this.setType(SearchType.PLAYLIST);
    this.setName(command.getPlaylistName());
  }
}
