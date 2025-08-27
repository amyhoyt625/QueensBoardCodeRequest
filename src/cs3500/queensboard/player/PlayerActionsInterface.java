package cs3500.queensboard.player;

import cs3500.queensboard.model.Board;
import cs3500.queensboard.model.QueensBoard;
import cs3500.queensboard.strategy.Move;

/**
 * Represents a player in the QueensBoard game, which can be either a human or a computer.
 *
 * <p>This interface defines the basic actions that a player can perform during the game,
 * such as making a move, checking if it's the computer's turn, and retrieving the player's color.
 */
public interface PlayerActionsInterface {

  /**
   * Makes a move on the given board. The implementation of this method will vary depending
   * on whether the player is a human or a computer.
   *
   * @param board the current state of the QueensBoard
   * @return the Move chosen by the player
   */
  Move makeMove(QueensBoard board);

  /**
   * Determines whether this player is a computer player.
   *
   * @return true if it is the computer's turn, false otherwise
   */
  boolean isComputerTurn();

  /**
   * Gets the color associated with this player.
   *
   * @return the Board.Player color of the player
   */
  Board.Player getPlayerColor();
}
