package cs3500.queensboard.player;

import cs3500.queensboard.model.Board;
import cs3500.queensboard.model.QueensBoard;
import cs3500.queensboard.strategy.Move;
import cs3500.queensboard.strategy.Strategy;

/**
 *  Computer Player is an AI player that handles strategy given
 *  from command line to implement for game play. Communicates with
 *  the controller, telling it the strategy to use.
 */
public class ComputerPlayer implements PlayerActionsInterface {

  private Strategy strategy;
  private Board.Player playerColor;

  public ComputerPlayer(Strategy strategy, Board.Player playerColor) {
    this.strategy = strategy;
    this.playerColor = playerColor;
  }

  // makes a move/strategy
  @Override
  public Move makeMove(QueensBoard board) {
    Move move = strategy.chooseMove(board);
    return move;
  }

  @Override
  public boolean isComputerTurn() {
    return true;
  }

  public Board.Player getPlayerColor() {
    return playerColor;
  }

}