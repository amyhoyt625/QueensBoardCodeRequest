package cs3500.queensboard.provider.model;

import java.util.List;
import java.util.Optional;

/**
 * Represents a read-only view of the Queens Blood game model. This interface exposes
 * only observation methods and prevents mutation of the game state. Views and other
 * components can use this interface to safely query the state of the game without
 * being able to alter it.
 */
public interface ReadOnlyQueensBloodModel {

  /**
   * Retrieve the number of cards that make up the width of the rectangle
   * that contains the polygon. (e.g. the number of columns in the widest row)
   *
   * @return the width of the board.
   */
  int getWidth();

  /**
   * Retrieve the number of cards that make up the height of the rectangle
   * that contains the polygon. (e.g. the number of rows in the highest column)
   *
   * @return the height of the board.
   */
  int getHeight();

  /**
   * Returns the item, which is a BoardPiece, in the indicated position on the board. If there
   * is no card on the board and the position is valid, the method will return null.
   *
   * @param row the row to access
   * @param col the column to access
   * @return the card in the valid position or null if the position has no card
   * @throws IllegalArgumentException if the row and column are not a valid location
   *                                  for a card in the polygonal board
   * @throws IllegalStateException    if the game has not started
   */
  BoardPiece getItemAt(int row, int col);

  /**
   * Returns the number of cards left in Red's deck being used during the game.
   *
   * @return the number of cards left in Red's deck used in game
   * @throws IllegalStateException if the game has not started
   */
  int getRemainingRedDeckSize();

  /**
   * Returns the number of cards left in Blue's deck being used during the game.
   *
   * @return the number of cards left in Blue's deck used in game
   * @throws IllegalStateException if the game has not started
   */
  int getRemainingBlueDeckSize();

  /**
   * Returns GameState to check if game is over. The game is only over IF both players
   * {@code passCard()} consecutively or if board is filled. Since method returns a type
   * GameState, it contains the functionality of both, determining if the game is over or ongoing,
   * and to indicate who is the winner, if game is over.
   *
   * @return a GameState which can be: Red wins, Blue wins, Tie or Ongoing
   * @throws IllegalStateException if the game has not started
   */
  GameState isGameOver();

  /**
   * Calculates player Red's total score at any point during the game.
   * Method will check each row's left-most number (Red's row-score) and
   * right-most number (Blue's row-score) and compare the two.
   * If Red's row-score is higher than Blue's row-score, Red's row-score will be
   * added to {@code redTotalScore()}, else nothing is added. Once all
   * rows are iterated through return the total score for Red.
   * The row-score is determined by the sum of the value of all red card's
   * on that row, considering that pawns do not affect the row-score.
   *
   * @return player red's current total score.
   * @throws IllegalStateException if the game has not started
   */
  int redTotalScore();

  /**
   * Calculates player Blue's total score at any point during the game.
   * Method will check each row's left-most number (Red's row-score) and
   * right-most number (Blue's row-score) and compare the two.
   * If Blue's row-score is higher than Red's row-score, Blue's row-score will be
   * added to {@code blueTotalScore()}, else nothing is added. Once all
   * rows are iterated through return the total score for Blue.
   * The row-score is determined by the sum of the value of all blue card's
   * on that row, considering that pawns do not affect the row-score.
   *
   * @return player red's current total score.
   * @throws IllegalStateException if the game has not started.
   */
  int blueTotalScore();

  /**
   * This method gives access to the current state of player Red by retrieving it.
   *
   * @return current state of player Red.
   * @throws IllegalStateException if the game has not started
   */
  PlayerState getRedPlayerState();

  /**
   * This method gives access to the current state of player Blue by retrieving it.
   *
   * @return current state of player Blue.
   * @throws IllegalStateException if the game has not started
   */
  PlayerState getBluePlayerState();

  /**
   * Method to determine who currently has the turn.
   *
   * @return the Player whose turn it is.
   * @throws IllegalStateException if the game has not started
   */
  Player turn();

  /**
   * Returns a copy of the specified player's hand. This method provides access to either
   * Red or Blue player's hand, regardless of whose turn it is.
   *
   * @param player represents the player whose hand is to be retrieved
   * @return a list of cards in the specified player's hand
   * @throws IllegalArgumentException if the player is null
   * @throws IllegalStateException    if the game has not started
   */
  List<Card> getPlayerHand(Player player);

  /**
   * Returns a copy of the current player's hand.
   * This method was intended for allowing views or controllers
   * to quickly retrieve the hand of the player whose turn it currently is.
   *
   * @return a list of cards in the current player's hand
   * @throws IllegalStateException if the game has not started
   */
  List<Card> getCurrentPlayerHand();

  /**
   * Method returns the owner of the BoardPiece on the given coordinates, row and col.
   *
   * @param row represents the row for the cell (0-indexed)
   * @param col represents the col for the cell (0-indexed)
   * @return null if cell is empty or the player owner of the pawn or card on that cell
   * @throws IllegalArgumentException if the row or col are smaller than 0 or bigger than or equal
   *                                  to the given row and col for game's grid.
   * @throws IllegalStateException    if the game has not started
   */
  Optional<Player> getOwnerAt(int row, int col);

  /**
   * Determines whether the current player can legally play the card at the given index
   * from their hand at the specified board position.
   *
   * <p>A placement is legal if:
   * - The row and col are within the given borders of the game
   * - The cell at (row, col) contains only pawns of the current player
   * - The number of pawns at that cell is greater than or equal to the cost of the card
   * - The target cell does not already contain a card
   * - The given card index is valid within the current player's hand
   *
   * @param player  represents the player attempting to place card at given index
   * @param cardIdx the index of the card in the current player's hand (0-based)
   * @param row     the row on the board to attempt placement
   * @param col     the column on the board to attempt placement
   * @return true if the card can be legally placed at the position; false otherwise
   * @throws IllegalArgumentException if the card index is not within the hand's size borders,
   *                                  if the board position is not within the game grid's borders
   * @throws IllegalStateException    if the game has not started
   */
  boolean canPlayCardAt(Player player, int cardIdx, int row, int col);


  /**
   * Calculates the row-score for a specific player on a specific row.
   * The row score is determined by summing the values of all cards owned
   * by the given player in that row. Pawns do not contribute to the row score.
   *
   * @param player the player whose row score is to be calculated
   * @param row    the row index for which to calculate the score
   * @return the row score for the specified player on the given row
   * @throws IllegalArgumentException if the player is null or the row is invalid
   * @throws IllegalStateException    if the game has not started
   */
  int getRowScore(Player player, int row);


}
