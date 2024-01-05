package app.audio.player.states;

import lombok.Getter;

/**
 * Enum representing the different repeat states for a playing song or podcast.
 */
@Getter
public enum PlayerSongPodcastRepeatStates {
  NO_REPEAT("No Repeat"),
  REPEAT_ONCE("Repeat Once"),
  REPEAT_INFINITE("Repeat Infinite");

  private final String description;

  /**
   * Constructs a PlayerSongPodcastRepeatStates object with the given description.
   *
   * @param description the description of the repeat state
   */
  PlayerSongPodcastRepeatStates(final String description) {
    this.description = description;
  }

  /**
   * Returns the next repeat state in the enum.
   *
   * @return the next repeat state
   */
  public PlayerSongPodcastRepeatStates next() {
    return values()[(this.ordinal() + 1) % values().length];
  }
}
