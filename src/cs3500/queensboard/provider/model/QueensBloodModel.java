package cs3500.queensboard.provider.model;

import java.util.List;

/**
 * Represents the behaviors for a queens blood board game. It assumes that
 * the constructed board will follow a rectangular pattern.
 *
 * <p>The game will start with an empty rectangle and each player will be given
 * a hand from the deck of cards to fill the board with
 *
 * <p>The players will only be able to draw cards from the deck if it is not empty.
 *
 * <p>At each turn, the players will have the option to pass or place a card until
 * the game ends, which ends when both players pass their turn.
 *
 * <p>Each model implementation of the interface QueensBloodModel should contain
 * their own configuration file that includes all the available cards for that
 * model of game. This way each implementation can only use cards that are
 * present in the configuration file.
 */
public interface QueensBloodModel<C extends Card> extends ReadOnlyQueensBloodModel {
  /**
   * Places the card from the hand to a given position on the rectangular board
   * and then, if there still are cards on deck, draws from deck and removes card
   * used on hand.
   *
   * <p>Legal Placement IF Cell contains pawns the player owns AND
   * Cell contains at least enough pawns to cover the cost of the card.
   * Then, pawns on that cell are removed and placed with a card.
   *
   * <p>After Legal Placement each card has an influence on the board. IF there is
   * a card placed on a cell that should be influenced, nothing happens. IF the cell is
   * empty, add 1 pawn to Cell. IF the cell to be influenced contains the pawns of
   * the current player, add 1 pawn to cell, given a maximum of 3 pawns per cell. IF the
   * cell to be influenced contains the other player's pawns on cell, the current player will
   * take ownership of the pawns present on that cell, meaning all pawns will change color.
   * Pawns in the cell of the card placed are removed and replaced with card. Also, after a legal
   * placement, the player's turn will be switched, hence, the next player will have the opportunity
   * to place a card.
   *
   * <p>Illegal Placement IF conditions above are not satisfied player must
   * choose another card that satisfies conditions. IF no cards satisfy the
   * conditions then player must {@code passCard()}. A card CANNOT be placed on an empty cell.
   *
   * @param cardIdx represents the index of the card in the hand (0-indexed).
   * @param row     represents the row it will be placed (0-indexed).
   * @param col     represents the col it will be placed (0-indexed).
   * @throws IllegalStateException    if none of the owner's cells pawns are greater than any of
   *                                  their cards' cost in hand or none of positions are empty or,
   *                                  if the game has not started or there is a card at the given
   *                                  position or,
   *                                  if the cell does not have enough of their own pawns to pay
   *                                  for the card's cost
   *                                  if the cell where the card is trying to be placed is empty
   * @throws IllegalArgumentException if cardIdx is smaller than 0 or out of hand's bounds or
   *                                  if row and col are smaller than 0 or doesn't indicate a valid
   *                                  position on the rectangle.
   */
  void placeCardInPosition(int cardIdx, int row, int col);

  /**
   * Pass card will be a method that enables the player to pass their turn to the next player
   * without having to place a card. IF both players pass their turn, the game ends.
   */
  void passCard();

  /**
   * Method will start the game where each player is dealt their cards at random from
   * the list of their respective decks. Each player is given one deck, sorted at random
   * and a general handSize. Red player always starts first. Cells on left-most column
   * are each set to one red pawn. Cells on right-most columns are each set to one blue pawn.
   *
   * @param row            int representing the number of rows.
   * @param col            int representing the number of columns.
   * @param deckRedPlayer  list of cards that the red player will play the game with
   * @param deckBluePlayer list of cards that the Blue player will play with.
   * @param handSize       hand size of both player's hand.
   * @throws IllegalStateException    if the game has already been started.
   * @throws IllegalArgumentException if the row is less than 0,
   *                                  if the col is smaller than 0 and not odd, meaning even,
   *                                  if any given deck is null or contains a null object,
   *                                  if deckRedPlayer or deckBluePlayer are empty,
   *                                  if the deckRedPlayer's size is not equal to deckBluePlayer's
   *                                  size,
   *                                  if any given deck contains more than two copies of same card,
   *                                  if handSize is smaller than 0,
   *                                  if handSize is greater than 1/3 of the deck's size,
   *                                  or if the deck does not contain enough cards to fill
   *                                  the board, AND fill the starting hand.
   */
  void startGame(int row, int col,
                 List<C> deckRedPlayer, List<C> deckBluePlayer, int handSize);

  /**
   * Method will consider the influence impact that placing a card has on the board.
   * The impact on the cells will be based of the board position that card is placed
   * and the grid of the card. This method will not be on abstract class since it will
   * its impact will depend on the type of game being played.
   *
   * @param card the card that was placed.
   * @param row  the row index where card was placed.
   * @param col  the col index where card was placed.
   */
  void influenceImpact(Card card, int row, int col);

  /**
   * Method that returns a copy of the Model with mutator and observer responsibilities, useful
   * for simulation, replay, branching, undo, or other model-level logic that involves mutation.
   *
   * @return the Model, of type QueensBloodModel, that contains mutator and observer behaviors.
   * @throws IllegalStateException if the game has not started
   */
  QueensBloodModel copy();

  List<Card> getRemainingRedDeck();

  List<Card> getRemainingBlueDeck();

  /**
   * Method will register a listener that will be notified of key model events,
   * such as turn changes and game completion.
   *
   * @param listener the ModelListener to be added
   */
  void addModelListener(ModelListener listener);

}
