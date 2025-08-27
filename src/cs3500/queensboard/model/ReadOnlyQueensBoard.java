package cs3500.queensboard.model;

import java.util.List;

import cs3500.queensboard.controller.DeckConfig;
import cs3500.queensboard.controller.ModelStatus;

/**
 * This interface marks the idea of a Game Board. This interface is implemented by Board
 * in order to create an instance of a Game Board.
 * The game board is represented as a 2D array of cells with their individual behaviors
 * and characteristics.
 */
public interface ReadOnlyQueensBoard {

  /**
   * Retrieve the number of cards that make up the width of the rectangle
   * that contains the polygon. (e.g. the number of columns in the widest row)
   *
   * @return the width of the board
   */
  int getWidth();

  /**
   * Retrieve the number of cards that make up the height of the rectangle
   * that contains the polygon. (e.g. the number of rows in the highest column)
   *
   * @return the height of the board
   */
  int getHeight();


  /**
   * Returns the card in the indicated position on the board. If there is no card on the board
   * and the position is valid, the method will return null.
   *
   * @param row the row to access
   * @param col the column to access
   * @return the card in the valid position or null if the position has no card
   * @throws IllegalArgumentException if the row and column are not a valid location
   *                                  for a card in the polygonal board
   */
  Card getCardAt(int row, int col);

  /**
   * Returns a copy of the player's current hand. If their hand is empty, then an empty
   * list is returned.
   *
   * @return a copy of the player's current hand
   * @throws IllegalStateException if the game has not started
   */
  List<Card> getHand();

  /**
   * Returns the current score of the game. The rules of scoring are determined
   * by the implementation.
   *
   * @param player the player that wants their score
   * @return the current score of the game
   * @throws IllegalStateException if the game has not started
   */
  int getScore(Board.Player player);

  /**
   * Returns the number of cards left in the deck being used during the game.
   *
   * @param player the player that wants their remaining deck size
   * @return the number of cards left in the deck used in game
   * @throws IllegalStateException if the game has not started
   */
  int getRemainingDeckSize(Board.Player player);

  /**
   * Returns the winner of the ENTIRE game, based on the winner of each row.
   */
  Board.Player getWinner();

  /**
   * Returns true if the game is over. The implementation must
   * describe what it means for the game to be over.
   *
   * @return true if the game is over, false otherwise
   * @throws IllegalStateException if the game has not started
   */
  boolean isGameOver();

  /**
   * Returns the player (RED or BLUE) who's current turn it is.
   */
  Board.Player getTurn();

  /**
   * Return the amount of emptySpace are available on the gameboard for gameplay.
   */
  int getEmptySpaces();

  /**
   * Return the score of the row based on who has the higher overall row score.
   *
   * @param row row number that score is needed for
   * @return row score
   */
  public int getRowScore(int row);

  /**
   * Return the score of red for the given row.
   *
   * @param row row number that score is needed for
   * @return Red's row score
   */
  int getRedRowScore(int row);

  /**
   * Return the score of blue for the given row.
   *
   * @param row row number that score is needed for
   * @return Blue's row score
   */
  int getBlueRowScore(int row);

  /**
   * Return the deckConfiguration for a file of cards.
   *
   * @return a deckConfiguration instance
   */
  DeckConfig getDeckConfig();

  /**
   * Determine if the deck should be shuffled.
   * For now, this is always FALSE
   *
   * @return if deck is shuffled
   */
  boolean getShuffle();

  /**
   * Return the cell at a specific coordinate.
   *
   * @param row position of the cell
   * @param col position of the cell
   * @return a cell object at that position
   */
  Cell getCell(int row, int col);

  /**
   * Determine if the cell at a specific coordinate is a valid instance of a cell.
   *
   * @param row position of the cell
   * @param col position of the cell
   * @return true if the cell at that position is valid
   */
  Boolean isValidCell(int row, int col);


  /**
   * Returns a copy of the board at its current state.
   */
  Board copy();

  /**
   * Returns a copy of the red player's current hand. If their hand is empty, then an empty
   * list is returned.
   *
   * @return a copy of the player's current hand
   * @throws IllegalStateException if the game has not started
   */
  List<Card> getRedHand();

  /**
   * Returns a copy of the blue player's current hand. If their hand is empty, then an empty
   * list is returned.
   *
   * @return a copy of the player's current hand
   * @throws IllegalStateException if the game has not started
   */
  List<Card> getBlueHand();


  void addListener(ModelStatus listener);
}
