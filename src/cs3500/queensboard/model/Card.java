package cs3500.queensboard.model;

/**
 * This interface marks the idea of cards. This interface is implemented by QueensCard
 * in order to create instances of Cards.
 * Cards are read from a file using the Deck Configuration class, but are assigned to objects
 * using this interface to allow for manipulation of data and accessing characteristics.
 */
public interface Card {
  /**
   * Returns the value of the Card (how much it adds to the score).
   */
  int getValue();

  /**
   * Returns the cost of the Card (how many pawns it costs to place).
   */
  int getCost();

  /**
   * Returns the player that owns the card (RED or BLUE).
   */
  Board.Player getInfluence();

  /**
   * Returns the name of the Card.
   */
  String getName();

  /**
   * Returns the 5x5 Influence Grid that displays the Card's influence.
   */
  char[][] getInfluenceGrid();

  /**
   * Renders a card so that ony the owner of the card is known.
   * RED Player -> "R"
   * BLUE Player -> "B"
   * @return the formatted card
   */
  String toString();

  /**
   * Determines whether two cards are the same.
   * @param obj another Card object
   * @return true if ALL card characteristics are the same
   */
  boolean equals(Object obj);

  /**
   * Determines the hashcode (place in memory) of the card.
   * @return the hashcode of the card
   */
  public int hashCode();

}
