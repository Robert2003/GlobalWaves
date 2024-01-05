package library.entities.audio.audio.collections;

import app.io.nodes.input.InputNode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;
import library.entities.audio.AudioEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents an audio collection. Inherits from the AudioEntity class and adds additional
 * properties and methods specific to audio collections.
 */
@Getter
@Setter
public class AudioCollection extends AudioEntity {
  private String owner;
  @JsonIgnore private Integer currentPlayingIndex = 0;

  public AudioCollection() {
    super();
  }

  public AudioCollection(final AudioCollection audioCollection) {
    super(audioCollection);
    this.setOwner(audioCollection.getOwner());
    this.setCurrentPlayingIndex(audioCollection.getCurrentPlayingIndex());
  }

  public AudioCollection(final InputNode command) {
    super(command);
    this.setOwner(command.getUsername());
  }

  /**
   * Checks if this AudioCollection is equal to another object.
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
    if (!super.equals(o)) {
      return false;
    }
    AudioCollection that = (AudioCollection) o;
    return Objects.equals(owner, that.owner);
  }

  /**
   * Generates a hash code for this AudioCollection.
   *
   * @return The hash code value for this AudioCollection.
   */
  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), owner);
  }
}
