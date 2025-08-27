package cs3500.queensboard.provider.users;

import cs3500.queensboard.provider.model.Player;
import cs3500.queensboard.provider.model.QueensBloodModel;
import cs3500.queensboard.provider.model.ReadOnlyQueensBloodModel;
import cs3500.queensboard.provider.strategy.Move;
import java.util.Optional;

/**
 * Represents a human player. This player does not choose moves through code. Instead, it will
 * interact with the GUI, and so it always returns an empty move.
 */
public class HumanPlayer implements GameUser {

  /**
   * Constructs a human player with access to the read-only model.
   * Although the player does not use the model directly in this implementation,
   * it may be useful for testing or extensions.
   *
   * @param model the read-only game model
   * @throws IllegalArgumentException if the model is null
   */
  public HumanPlayer(ReadOnlyQueensBloodModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null.");
    }
  }


  @Override
  public Optional<Move> getMove(QueensBloodModel model, Player player) {
    return Optional.empty();
  }
}