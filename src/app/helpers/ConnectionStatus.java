package app.helpers;

public enum ConnectionStatus {
  ONLINE,
  OFFLINE;

  /**
   * Returns the next ConnectionStatus in the enumeration. <br>
   * This ensures that the method will return ONLINE if the current ConnectionStatus is OFFLINE, and
   * vice versa.
   *
   * @return The next ConnectionStatus in the enumeration.
   */
  public ConnectionStatus next() {
    return values()[(this.ordinal() + 1) % values().length];
  }
}
