package cs3500.queensboard.provider.strategy;

import java.util.Objects;

/**
 * Represents a possible move in the game. Every move contains the coordinates, 0-indexed, of where
 * card should be placed, row and col, and a cardIndex, which represents the index on the hand's
 * list, 0-indexed, where card will try to be taken from.
 */
public class Move {
  private final int cardIndex;
  private final int row;
  private final int col;

  /**
   * Constructor for Move class, which will receive the params that define a valid move.
   *
   * @param cardIndex represents the number on hand with the card
   * @param row       represents the number row that card should be placed
   * @param col       represents the number col that card should be placed
   */
  public Move(int cardIndex, int row, int col) {
    this.cardIndex = cardIndex;
    this.row = row;
    this.col = col;
  }

  /**
   * Method to get the cardIndex.
   *
   * @return the cardIndex field
   */
  public int getCardIndex() {
    return cardIndex;
  }

  /**
   * Method to get the row.
   *
   * @return the row field
   */
  public int getRow() {
    return row;
  }

  /**
   * Mehthod to get the col.
   *
   * @return the col field
   */
  public int getCol() {
    return col;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Move)) {
      return false;
    }
    Move other = (Move) obj;
    return cardIndex == other.cardIndex &&
            row == other.row &&
            col == other.col;
  }

  @Override
  public int hashCode() {
    return Objects.hash(cardIndex, row, col);
  }
}