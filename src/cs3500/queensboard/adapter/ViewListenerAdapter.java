package cs3500.queensboard.adapter;

import cs3500.queensboard.controller.QueensBoardControllerInterface;
import cs3500.queensboard.provider.controller.ViewListener;
import cs3500.queensboard.provider.model.Player;
import cs3500.queensboard.model.Board;

/**
 * Represents an adapter that implements the ViewListener interface.
 * This class serves as a bridge between different event-handling systems,
 * translating or adapting user input and GUI events from the view layer
 * into a form usable by the controller or game logic.
 */
public class ViewListenerAdapter implements ViewListener {

  private final QueensBoardControllerInterface controller;

  public ViewListenerAdapter(QueensBoardControllerInterface controller) {
    this.controller = controller;
  }

  @Override
  public void confirmMove() {
    // This is called when the user confirms a move (probably by pressing space)
    // Get the current selected card and cell from your controller
    int cardIndex = controller.getSelectedCardIndex();
    int row = controller.getSelectedRow();
    int col = controller.getSelectedCol();

    // Call the appropriate handler in your controller
    if (cardIndex != -1 && row != -1 && col != -1) {
      controller.handlePlaceKey(cardIndex, row, col);
    } else {
      System.out.println("Cannot confirm move: no card or cell selected");
    }
  }

  @Override
  public void selectCard(int cardIdx, Player player) {
    // Call your controller's card selection handler
    controller.handleCardClick(cardIdx);
  }

  @Override
  public void selectCell(int row, int col) {
    // Call your controller's cell selection handler
    controller.handleCellClick(row, col);
  }

  @Override
  public void passTurn() {
    // Call your controller's pass turn handler
    controller.handleTurnPass();
  }

  // Helper method to convert provider's Player to your Board.Player
  private Board.Player convertPlayer(Player providerPlayer) {
    if (providerPlayer == Player.RED) {
      return Board.Player.RED;
    } else {
      return Board.Player.BLUE;
    }
  }
}