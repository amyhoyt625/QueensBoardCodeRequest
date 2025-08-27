package cs3500.queensboard.view;

/**
 * This interface marks the idea of a Textual Rendering of the current game
 * state of the gameboard.
 * This interface is implemented by QueensBoardViewClass in order to
 * create an instance of the game view.
 */
public interface QueensBoardView {
  /**
   * Formats and appends the row scores to the current elements in the gameboard.
   * @return textual representation of the game
   */
  String toString();
}
