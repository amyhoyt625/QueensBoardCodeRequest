package cs3500.queensboard.provider.controller;

import cs3500.queensboard.provider.model.Player;

/**
 * Represents a listener interface for handling user interactions from the view
 * in the QueensBlood game. Implementations of this interface define how
 * the controller responds to inputs such as selecting cards, board cells,
 * confirming moves, and passing turns.
 */
public interface ViewListener {

  /**
   * Called when the user confirms their current move, by pressing "Enter", which
   * indicates that the current selections (card and board cell) should be finalized
   * and sent to the game model.
   */
  void confirmMove();

  /**
   * Called when the user selects a card from their hand.
   *
   * @param cardIdx index of the selected card.
   * @param player  owner of the selected card.
   */
  void selectCard(int cardIdx, Player player);

  /**
   * Called when a user chooses a cell on the game board.
   *
   * @param row the index of the row of the chosen cell.
   * @param col the index of the column of the chosen cell.
   */
  void selectCell(int row, int col);

  /**
   * Called when a user passes a turn by clicking on a specific key.
   */
  void passTurn();

}

