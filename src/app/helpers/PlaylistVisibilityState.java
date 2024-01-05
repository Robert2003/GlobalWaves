package app.helpers;

import lombok.Getter;

/**
 * Enum representing the visibility state of a playlist. The possible states are public and private.
 */
@Getter
public enum PlaylistVisibilityState {
  PUBLIC("public"),
  PRIVATE("private");

  private final String description;

  /**
   * Constructs a PlaylistVisibilityState with the given description.
   *
   * @param description the description of the visibility state
   */
  PlaylistVisibilityState(final String description) {
    this.description = description;
  }

  /**
   * Returns the next PlaylistVisibilityState in the enum.
   *
   * @return the next PlaylistVisibilityState
   */
  public PlaylistVisibilityState next() {
    return values()[(this.ordinal() + 1) % values().length];
  }
}
