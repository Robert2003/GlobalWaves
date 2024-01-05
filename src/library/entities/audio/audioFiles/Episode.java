package library.entities.audio.audioFiles;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Episode extends AudioFile {
  private String description;

  public Episode() {
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Episode episode = (Episode) o;
    return Objects.equals(description, episode.description) && super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), description);
  }
}
