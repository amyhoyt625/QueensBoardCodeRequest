package cs3500.queensboard.strategy;


import cs3500.queensboard.model.ReadOnlyQueensBoard;

/**
 * The {@code QueensBoardStrategy} interface defines the strategies available to players to
 * optimize the moves they make in a QueensBoard game. Strategies are based on various factors,
 * including maximizing a player's row score, increasing number of cells owned by a player, and
 * minimizing the gain made by an opponent.
 * <p>
 *   Strategies include:
 *   <ul>
 *     <li> {@code fillFirst}: Choose the first card from a player's hand and
 *     place it in the first valid, available position based on the card's cost
 *     and player ownership. </li>
 *     <li> {@code maxRowScore}: Maximize the row score of the current player by
 *     placing a specific card in a specific row to outscore the opponent. </li>
 *     <li> {@code maxCellOwnership}: Maximize the number of cells the current player owns
 *     by selecting a card from their hand that influences the most cells. </li>
 *     <li> {@code minimax}: A recursive strategy that attempts to anticipate the opponents
 *     moves and minimize their gain, while also maximizing the current player's score. </li>
 *   </ul>
 * </p>
 */
public interface Strategy {

  /**
   * Returns the Move created by a Strategy.
   *
   * @param board a ReadOnlyBoard
   * @return the move for a strategy
   */
  Move chooseMove(ReadOnlyQueensBoard board);

}
