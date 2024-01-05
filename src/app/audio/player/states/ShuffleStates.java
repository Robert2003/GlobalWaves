package app.audio.player.states;

import lombok.Getter;

/** Enum representing the shuffle states of an audio player. */
@Getter
public enum ShuffleStates {
  ON(true),
  OFF(false);

  private final boolean shuffled;

  /**
   * Constructs a ShuffleStates enum with the given shuffled state.
   *
   * @param shuffled true if the playlist is on shuffle, false otherwise
   */
  ShuffleStates(final boolean shuffled) {
    this.shuffled = shuffled;
  }

  /**
   * Returns the next shuffle state in the enum.
   *
   * @return the next shuffle state
   */
  public ShuffleStates next() {
    return values()[(this.ordinal() + 1) % values().length];
  }
}
