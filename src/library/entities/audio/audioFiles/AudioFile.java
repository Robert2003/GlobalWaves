package library.entities.audio.audioFiles;

import java.util.Objects;
import library.entities.audio.AudioEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents an audio file. Inherits from the AudioEntity class and adds additional
 * properties and methods specific to audio files.
 */
@Getter
@Setter
public class AudioFile extends AudioEntity {
  private Integer duration;

  public AudioFile() {
  }

  public AudioFile(final AudioFile audioFile) {
    super(audioFile);
    this.setDuration(audioFile.getDuration());
  }

  /**
   * Checks if the given object is equal to this AudioFile object.
   *
   * @param o The object to compare.
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
    AudioFile audioFile = (AudioFile) o;
    return Objects.equals(duration, audioFile.duration) && super.equals(o);
  }

  /**
   * Generates a hash code for this AudioFile object.
   *
   * @return The hash code value.
   */
  @Override
  public int hashCode() {
    return Objects.hash(duration);
  }
}
