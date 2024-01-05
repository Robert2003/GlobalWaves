package library.entities.audio.audio.collections;

import app.io.nodes.input.InputNode;
import app.searchbar.SearchType;
import java.util.ArrayList;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a playlist, which is a type of audio collection. It contains a list of songs,
 * visibility state, number of followers, playing order, and creation date.
 */
@Getter
@Setter
public final class Album extends SongAudioCollection {
  private String description;

  public Album() {
    super();
    this.setType(SearchType.ALBUM);
  }

  public Album(final InputNode command) {
    super(command);
    this.setType(SearchType.ALBUM);
    this.setName(command.getName());
    this.setSongs(new ArrayList<>(command.getSongs()));
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), description);
  }
}
