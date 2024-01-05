package library.entities.audio.audio.collections;

import app.io.nodes.input.InputNode;
import app.searchbar.SearchType;
import java.util.ArrayList;
import java.util.Objects;
import library.entities.audio.audioFiles.Episode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Podcast extends AudioCollection {
  private ArrayList<Episode> episodes;

  public Podcast() {
    this.setType(SearchType.PODCAST);
  }

  public Podcast(final InputNode command) {
    this.setName(command.getName());
    this.setOwner(command.getUsername());
    this.setType(SearchType.PODCAST);
    this.setEpisodes(new ArrayList<>(command.getEpisodes()));
  }

  /**
   * Checks if the Podcast object is equal to another object.
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
    Podcast podcast = (Podcast) o;
    return Objects.equals(episodes, podcast.episodes) && super.equals(o);
  }

  /**
   * Returns the hash code value for the Podcast object.
   *
   * @return The hash code value for the Podcast object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(episodes);
  }

  /**
   * Returns the total duration of all episodes in the Podcast.
   *
   * @return The total duration of all episodes in the Podcast.
   */
  public Integer getDuration() {
    Integer duration = 0;

    for (Episode episode : episodes) {
      duration += episode.getDuration();
    }

    return duration;
  }

  /**
   * Returns the duration from the beginning of the Podcast to the end of the specified episode.
   *
   * @param episode The episode to get the duration for.
   * @return The duration from the beginning of the Podcast to the end of the specified episode.
   */
  public Integer getEndOfEpisodeDuration(final Episode episode) {
    Integer duration = 0;

    for (Episode ep : episodes) {
      duration += ep.getDuration();
      if (ep.equals(episode)) {
        return duration;
      }
    }

    return duration;
  }

  /**
   * Returns the duration from the beginning of the Podcast to the beginning of the specified
   * episode.
   *
   * @param episode The episode to get the duration for.
   * @return The duration from the beginning of the Podcast to the beginning of the specified
   *     episode.
   */
  public Integer getBeginningOfEpisodeDuration(final Episode episode) {
    Integer duration = 0;

    for (Episode ep : episodes) {
      if (ep.equals(episode)) {
        return duration;
      }
      duration += episode.getDuration();
    }

    return duration;
  }
}
