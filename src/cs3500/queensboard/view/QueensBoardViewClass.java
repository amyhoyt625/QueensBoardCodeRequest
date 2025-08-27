package cs3500.queensboard.view;

import cs3500.queensboard.model.Board;

/**
 * Represents the textual representation the game board state.
 */
public class QueensBoardViewClass implements QueensBoardView {

  private final Board board;

  /**
   * Constructor to create a view object, which is the current state of the game.
   * @param board board being played on
   */
  public QueensBoardViewClass(Board board) {
    if (board == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    this.board = board;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    for (int row = 0; row < board.getHeight(); row++) {
      sb.append(board.getRedRowScore(row)).append(" "); // Leftmost Red row score
      for (int col = 0; col < board.getWidth(); col++) {
        sb.append(board.getCell(row, col).toString());
      }
      sb.append(" ").append(board.getBlueRowScore(row)).append("\n"); // Rightmost Blue row score
    }
    return sb.toString().trim();
  }

}
