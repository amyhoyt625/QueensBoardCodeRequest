package cs3500.queensboard.provider.model;

/**
 * Represents a card within the QueenBlood board game. This will include the
 * card's name, the cost (number of paws (1-3) needed to place a card in a cell), the value
 * (points added to the player's score when placed on board), and a 5x5 influence grid
 * (determines in what way the board will be affected when the card is to be placed).
 * On the grid, "X" represents no affect, "I" represents cell affected, and "C" represents
 * the card's position on the board. The card's position will always be shown in the center.
 */
public interface Card extends BoardPiece {

  /**
   * This method will return a string representation of the card. This will include the
   * card's name, the cost, the value, and a 5x5 influence grid.
   * (e.g.)
   * Card: Security Cost: 1 Value: 2
   * Influence Grid:
   * XXXXX
   * XXIXX
   * XICIX
   * XXIXX
   * XXXXX
   *
   * @return formatted string with all card information.
   */
  String toString();

  /**
   * Compares this card to another object for purposes of equality.
   * Cards are equal if they include the same exact name, cost, value, and influence grids.
   *
   * @return {@code true} if the cards have the same name, cost, value, influence grid.
   */
  boolean equals(Object obj);

  /**
   * Generates an integer hashcode for this card that is based on its
   * name, cost, value, and influence grid
   * Two cards will have the same hashcode if they are equal from {@code equals()}.
   *
   * @return integer hashcode representing the card.
   */
  int hashCode();

  /**
   * This method gets the pawn cost of the card.
   * The cost will be between 1 and 3.
   *
   * @return cost of the card. (e.g. 1, 2, 3)
   */
  int getCost();

  /**
   * Returns a 5x5 grid of how the card will affect the board.
   * "C" represents the card's position.
   * "I" represents the board affects.
   * "X" represents no effect to the board.
   * If card's owner is {@code Player.RED} then will return influence grid.
   * If card's owner is {@code Player.BLUE} then will return influence grid mirrored to Red's grid
   * across the columns (y-axis).
   *
   * @return a 2D 5x5 grid of char representing how placing card will influence board
   */
  char[][] getInfluenceGrid();

  /**
   * Gets the name of a card in the form of a string.
   *
   * @return name of a card.
   */
  String getName();
}
