package app.audio.player.states;

import lombok.Getter;

/** Enum representing the different states of a player's play/pause functionality. */
@Getter
public enum PlayerPlayPauseStates {
  PLAYING(false),
  PAUSED(true),
  STOPPED(true);

  private final boolean paused;

  /**
   * Constructs a PlayerPlayPauseStates object with the given paused state.
   *
   * @param paused false if the track if playing, true otherwise
   */
  PlayerPlayPauseStates(final boolean paused) {
    this.paused = paused;
  }

}
