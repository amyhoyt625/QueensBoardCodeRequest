package cs3500.queensboard.provider.model;

/**
 * Represents an observer for the QueensBloodModel that listens for key events
 * such as turn changes and game completion. The Controllers will use this interface
 * to react to game state changes.
 */
public interface ModelListener {

  /**
   * Notifies the listener that a new turn has started for the specified player.
   *
   * @param player the player whose turn has started
   */
  void turnStarted(Player player);

  /**
   * Notifies the listener that the game has ended and provides the final result.
   *
   * @param state the game state containing the winner or tie information
   */
  void gameOver(GameState state);
}