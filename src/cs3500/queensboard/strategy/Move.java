package cs3500.queensboard.strategy;

import cs3500.queensboard.model.Card;

/**
 * The {@code Move} class represents a possible move in a QueensBoard game. A move can either be:
 * a placement of a card at specific row, column, or a pass, meaning the turn is the other
 * player's.
 * <p>
 *   This class is responsible for:
 *   <ul>
 *     <li> Storing the row and column coordinates of the move on the board </li>
 *     <li> Storing the card associated with the move </li>
 *     <li> Determining if the move is a pass </li>
 *   </ul>
 * </p>
 */
public class Move implements QueensMove {

  // can be either (row, col, card) or pass
  private int row;
  private int col;
  private Card card;
  private boolean pass;

  /**
   * Creates an instance of {@code Move} that places a card at a specific row, column.
   *
   * @param row The row position for the move
   * @param col The col position for the move
   * @param card  The card to be placed at the given position
   */
  public Move(int row, int col, Card card) {
    this.row = row;
    this.col = col;
    this.card = card;
    this.pass = false;
  }

  /**
   * Creates an instance of {@code Move} that represents a pass (no card is placed).
   *
   * @param pass {@code true} is the move is pass, otherwise {@code false}
   */
  public Move(boolean pass) {

    this.pass = true;
  }

  /**
   * Returns the row where the Move was called.
   *
   * @return the row position of the Move.
   */
  public int getRow() {
    return row;
  }

  /**
   * Returns the column where the Move was called.
   *
   * @return the column position of the Move.
   */
  public int getCol() {
    return col;
  }

  /**
   * Returns the card that was placed on the board when the Move was made.
   *
   * @return the Card object that was played, or {@code null} if the move was a pass
   */
  public Card getCard() {
    return card;
  }

  /**
   * Determines if the move made was a pass.
   *
   * @return {@code true} if the move is a pass, otherwise {@code false}
   */
  public boolean isPass() {
    return pass;
  }

}

