package cs3500.queensboard.model;

import java.util.List;

/**
 * This interface marks the idea of a Game Board. This interface is implemented by Board
 * in order to create instance of a Game Board.
 * The gameboard is represented as a 2D array of cells with their individual behaviors
 * and characteristics.
 */
public interface QueensBoard extends ReadOnlyQueensBoard {
  /**
   * Places a card from the hand to a given position on the polygonal board and then
   * draws a card from the deck if able.
   *
   * @param cardIdx index of the card in hand to place (0-index based)
   * @param row     row to place the card in (0-index based)
   * @param col     column to place the card in (0-index based)
   * @throws IllegalStateException    if the game has not started or there is a card at the given
   *                                  position
   * @throws IllegalArgumentException if cardIdx is out of bounds of the hand or
   *                                  row and col do not indicate a position on the polygon
   * @throws IllegalStateException    if the target cell is invalid
   * @throws IllegalStateException    if not enough pawns to place card
   */
  void placeCardInPosition(int cardIdx, int row, int col);


  /**
   * handle passing instead of placing card.
   */
  void pass();

  /**
   * Starts the game with the given deck and hand size. If shuffle is set to true,
   * then the deck is shuffled prior to dealing the hand.
   * Note that modifying the deck given here outside this method should have no effect
   * on the game itself.
   *
   * @param redDeck  list of red cards to play the game with
   * @param blueDeck list of blue cards to play the game with
   * @param handSize maximum hand size for the game
   * @throws IllegalStateException    if the game has already been started
   * @throws IllegalArgumentException if the deck is null or contains a null object,
   *                                  if handSize is not positive (i.e. 0 or less),
   *                                  or if the deck does not contain enough cards to fill the board
   *                                  AND fill a starting hand
   */
  void startGame(List<Card> redDeck, List<Card> blueDeck, int handSize);


  /**
   * Apply the influence grid to the cells influenced by the placed card.
   *
   * @param card that is being placed on the game board.
   * @param row  of cell that is influenced
   * @param col  of cell that is influenced
   */
  void applyInfluence(Card card, int row, int col);


}
