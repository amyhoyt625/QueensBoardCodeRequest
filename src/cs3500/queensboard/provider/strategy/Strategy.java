package cs3500.queensboard.provider.strategy;

import cs3500.queensboard.provider.model.Player;
import cs3500.queensboard.provider.model.QueensBloodModel;
import java.util.Optional;

/**
 * Represents a strategy of the game, which is how the game will choose to add its cards. Any
 * strategy to be created in the game will contain certain of rules that will help the program to
 * define where to place the card. The interface contains a single method, chooseMove that describes
 * that behavior. Follows strategy pattern, where interface does not know the infinite
 * implementations that chooseMove could have, only that, each strategy will need to create a logic
 * for chooseMove method.
 */
public interface Strategy {

  /**
   * Method that will implement the behavior for a specific Strategy class. Each class implementing
   * Strategy will contain a set of rules to determine what are the viable moves within that
   * implementation. If, no viable moves are identified, will return an null or Optional.empty().
   *
   * @param model  represents the readonly  model on which move will be made
   * @param player represents player making the move
   * @return an optional move, which is a empty, null or a valid move
   */
  Optional<Move> chooseMove(QueensBloodModel model, Player player);
}
