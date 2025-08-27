package cs3500.queensboard.provider.model;

/**
 * Enum represents the possible states of the game, it either is ongoing or have finished and,
 * therefore, contains an end game state. For the end game state, those can be red wins, blue wins
 * or a tie between players.
 */
public enum GameState {
  RED_WINS,
  BLUE_WINS,
  TIE,
  GAME_ONGOING;

  @Override
  public String toString() {
    switch (this) {
      case RED_WINS:
        return "Red Wins!";
      case BLUE_WINS:
        return "Blue Wins!";
      case TIE:
        return "It's a Tie!";
      case GAME_ONGOING:
        return "Game is ongoing...";
      default:
        throw new IllegalStateException("Unexpected GameState: " + this);
    }
  }
}
