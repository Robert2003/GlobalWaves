package library.entities.audio;

import app.io.nodes.input.InputNode;
import app.searchbar.SearchType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * Base class for all playable entities (Song, Episode, Podcast, Playlist)
 */
@Getter
@Setter
public class AudioEntity {
  private String name = null;
  @JsonIgnore private SearchType type = SearchType.NOT_INITIALIZED;
  @JsonIgnore private Long startTimestamp = 0L;

  public AudioEntity() {
  }

  public AudioEntity(final AudioEntity entity) {
    this.setName(entity.getName());
    this.setStartTimestamp(entity.getStartTimestamp());
  }

  public AudioEntity(final InputNode command) {
    this.setName(command.getName());
    this.setStartTimestamp(command.getTimestamp());
  }

  /**
   * Checks if this AudioEntity is equal to another object.
   *
   * @param o The object to compare with.
   * @return true if the objects are equal, false otherwise.
   */
  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AudioEntity that = (AudioEntity) o;
    return Objects.equals(name, that.name);
  }

  /**
   * Generates a hash code for this AudioEntity.
   *
   * @return The hash code value for this AudioEntity.
   */
  @Override
  public int hashCode() {
    return Objects.hash(name, type);
  }
}
