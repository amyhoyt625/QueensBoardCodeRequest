package cs3500.queensboard.strategy;

import cs3500.queensboard.model.Card;

/**
 * The {@code QueensMove} interface defines the structure for a Move object in a QueensBoard
 * game. It contains the information needed to execute a move, including the row, column
 * where the Move is made, and the Card object used to make the move. A Move can also be a pass.
 */
public interface QueensMove {

  /**
   * Returns the row where the Move was called.
   *
   * @return the row position of the Move.
   */
  int getRow();


  /**
   * Returns the column where the Move was called.
   *
   * @return the column position of the Move.
   */
  int getCol();

  /**
   * Returns the card that was placed on the board when the Move was made.
   *
   * @return the Card object that was played, or {@code null} if the move was a pass
   */
  Card getCard();

  /**
   * Determines if the move made was a pass.
   *
   * @return {@code true} if the move is a pass, otherwise {@code false}
   */
  boolean isPass();
}
