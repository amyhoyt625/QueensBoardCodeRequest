package cs3500.queensboard.provider.controller;

import cs3500.queensboard.provider.model.Player;

/**
 * This class is used to print out the interactions passed from the view of the game.
 * Its purpose is the verify the view is functioning as it should be connected to the controller.
 */
public class StubController implements ViewListener {

  @Override
  public void confirmMove() {
    System.out.println("Move is confirmed");
  }

  @Override
  public void selectCard(int cardIdx, Player player) {
    System.out.println("Selected card at:" + cardIdx + ", Player: " + player);
  }

  @Override
  public void selectCell(int row, int col) {
    System.out.println("Selected cell at:" + row + ", col: " + col);
  }

  @Override
  public void passTurn() {
    System.out.println("Pass turn");
  }

}