package cs3500.queensboard.view;

import cs3500.queensboard.controller.QueensBoardControllerInterface;

/**
 * Represents the graphical user interface (GUI) view for the QueensBoard game.
 *
 * <p>This interface defines methods for rendering and interacting with the visual
 * components of the game board, such as highlighting cells or cards, updating the board,
 * and responding to game events. It also facilitates communication with the controller
 * through observer registration.
 *
 * <p>Implemented by classes such as {@code QueensBoardViewClass}.
 */
public interface QueensBoardGUIView {

  /**
   * Clears any visual highlights on the board (such as highlighted cells or cards).
   * Typically used to reset selection visuals between moves or turns.
   */
  void clearHighlights();

  /**
   * Disables all forms of user input on the GUI.
   * Used during the opponent's turn or when the game is over.
   */
  void disableInput();

  /**
   * Repaints or refreshes the GUI view to reflect the current game state.
   * Should be called after any update to the board or input handling.
   */
  void repaint();

  /**
   * Registers a controller as an observer of this view. The observer will be notified
   * of relevant input events or user interactions.
   *
   * @param obs the {@link QueensBoardControllerInterface} instance to observe this view
   */
  void addObserver(QueensBoardControllerInterface obs);

  /**
   * Highlights a specific cell on the game board, typically with a yellow outline,
   * to indicate selection or a possible move.
   *
   * @param row the row index of the cell to highlight
   * @param col the column index of the cell to highlight
   */
  void highlightCell(int row, int col);

  /**
   * Highlights a specific card in the player's hand to indicate it is currently selected.
   *
   * @param cardIndex the index of the card to highlight
   */
  void highlightCard(int cardIndex);

  /**
   * Places the selected card onto the specified cell of the board.
   * This updates the visual representation of the board accordingly.
   *
   * @param row the row index where the card is being placed
   * @param col the column index where the card is being placed
   * @param cardIndex the index of the card in the player's hand to place
   */
  void placeCard(int row, int col, int cardIndex);

  /**
   * Displays a message or visual indication that the game is over.
   * May include highlighting the final board state or showing a popup.
   */
  void showGameOver();

  /**
   * Retrieves the index of the currently selected card in the player's hand.
   *
   * @return the selected card index, or -1 if no card is selected
   */
  int getSelectedCardIndex();

  /**
   * Retrieves the row index of the currently selected board cell.
   *
   * @return the selected row index, or -1 if no cell is selected
   */
  int getSelectedRow();

  /**
   * Retrieves the column index of the currently selected board cell.
   *
   * @return the selected column index, or -1 if no cell is selected
   */
  int getSelectedCol();
}
