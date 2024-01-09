package library.entities.audio.audio.collections;


import app.io.nodes.input.InputNode;
import app.searchbar.SearchType;
import library.entities.audio.audioFiles.Song;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a playlist, which is a type of audio collection. It contains a list of songs,
 * visibility state, number of followers, playing order, and creation date.
 */
@Getter
@Setter
public final class Playlist extends SongAudioCollection {
  public Playlist() {
    super();
    this.setType(SearchType.PLAYLIST);
  }

  public Playlist(final InputNode command) {
    super(command);
    this.setType(SearchType.PLAYLIST);
    this.setName(command.getPlaylistName());
  }

  public Playlist(final String name, final List<Song> songs, final User user) {
    this.setType(SearchType.PLAYLIST);
    this.setName(name);
    this.setSongs(new ArrayList<>(songs));
    this.setOwner(user.getUsername());
  }
}
