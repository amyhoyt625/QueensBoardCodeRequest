package cs3500.queensboard.controller;

/**
 * The {@code QueensBoardControllerInterface} defines the methods that the
 * QueensBoardController should implement.
 */
public interface QueensBoardControllerInterface {

  /**
   * Handles the click event for a clicked card.
   * Highlights the selected card by calling the view.
   * @param cardIndex index of the card in a player's hand being selected.
   */
  public void handleCardClick(int cardIndex);

  /**
   * Handles the click event for a clicked cell.
   * Highlights the selected cell by calling the view.
   * @param row of the cell being selected
   * @param col of the cell being selected
   */
  public void handleCellClick(int row, int col);

  /**
   * Notifies the user that the action was done.
   * In this case, the selected card was placed on the game board.
   */
  public void handlePlaceKey(int row, int col, int cardIndex);

  /**
   * Notifies the user that the action was done.
   * In this case, the player chooses to pass their turn, so it is the other player's turn.
   */
  public void handleTurnPass();

  void handleComputerMove();

  void cardSelected();

  void cellSelected();

  int getSelectedCardIndex();

  int getSelectedRow();

  int getSelectedCol();
}