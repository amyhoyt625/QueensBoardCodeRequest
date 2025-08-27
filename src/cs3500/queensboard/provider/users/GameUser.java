package cs3500.queensboard.provider.users;

import cs3500.queensboard.provider.model.Player;
import cs3500.queensboard.provider.model.QueensBloodModel;
import cs3500.queensboard.provider.strategy.Move;
import java.util.Optional;

/**
 * This interface represents all possible game users for the QueensBloodModel game. Our game, will
 * implement two possible users, HumanPlayer and ComputerPlayer.
 */
public interface GameUser {
  /**
   * Called when it is this user's turn.
   *
   * @param model  read-only model so the user can examine the board state
   * @param player the player (RED or BLUE) this user represents
   * @return an optional move (empty if passing or still thinking)
   */
  Optional<Move> getMove(QueensBloodModel model, Player player);

}
