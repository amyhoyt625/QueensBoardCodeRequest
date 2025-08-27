package cs3500.queensboard.player;

import cs3500.queensboard.model.Board;
import cs3500.queensboard.model.QueensBoard;
import cs3500.queensboard.strategy.Move;

/**
 *  Human player is a stub that represents a Human player that can interact
 *  with the board. It's functionality is built in already and communicates
 *  with the controller through interaction with the GUI view.
 */
public class HumanPlayer implements PlayerActionsInterface {

  @Override
  public Move makeMove(QueensBoard board) {
    return null;
  }

  @Override
  public boolean isComputerTurn() {
    return false;
  }

  @Override
  public Board.Player getPlayerColor() {
    return null;
  }


}
