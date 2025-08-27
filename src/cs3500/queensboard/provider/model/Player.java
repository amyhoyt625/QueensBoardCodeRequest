package cs3500.queensboard.provider.model;

/**
 * Enum represents the possible players in the game, either RED or BLUE.
 */
public enum Player {
  RED,
  BLUE;

  @Override
  public String toString() {
    return this == RED ? "Red" : "Blue";
  }
}
