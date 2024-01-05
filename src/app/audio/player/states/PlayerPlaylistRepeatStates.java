package app.audio.player.states;

import lombok.Getter;

/**
 * Enum representing the different repeat states for a playing playlist.
 */
@Getter
public enum PlayerPlaylistRepeatStates {
  NO_REPEAT("No Repeat"),
  REPEAT_ALL("Repeat All"),
  REPEAT_CURRENT_SONG("Repeat Current Song");

  private final String description;

  /**
   * Constructs a PlayerPlaylistRepeatStates enum with the given description.
   *
   * @param description the description of the repeat state
   */
  PlayerPlaylistRepeatStates(final String description) {
    this.description = description;
  }

  /**
   * Returns the next repeat state in the enum.
   *
   * @return the next repeat state
   */
  public PlayerPlaylistRepeatStates next() {
    return values()[(this.ordinal() + 1) % values().length];
  }
}
