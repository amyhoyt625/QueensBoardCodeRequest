package cs3500.queensboard.model;

/**
 * This interface marks the idea of cells. This interface is implemented by Cell
 * in order to create instances of Cells.
 * Cells are used to make up the playable game board to allow access to Cell behaviors.
 */
public interface CellInterface {
  /**
   * Returns the score of the cell (value of card placed).
   */
  int getCellScore();

  /**
   * Returns the player (RED or BLUE) that owns the cell (w/ Pawns or cards).
   */
  Board.Player getOwner();

  /**
   * Returns the number of pawns in a cell.
   */
  int getPawnCount();

  /**
   * Returns whether a cell contains a card.
   */
  boolean hasCard();

  /**
   * Returns whether a cell is empty (no card or pawns).
   */
  boolean isEmpty();

  /**
   * Adds the given amount of pawns to a cell.
   * @param card owner must match owner of pawns
   * @param i number of pawns being added
   * @throws IllegalArgumentException if the cell has a card
   * @throws IllegalArgumentException if the cell already has 3 pawns
   */
  void addPawn(Card card, int i);

  /**
   * Returns the owner (RED or BLUE) of the pawns in a cell.
   */
  Board.Player getPawnOwner();

  /**
   * Changes the owner of a cell to the other player.
   */
  void changeOwnership();

  /**
   * Return the card at the cell.
   *
   * @throws IllegalArgumentException if the cell does not contain a card
   */
  Card getCard();

  /**
   * Format the string based on the element in the cell.
   */
  String toString();
}
