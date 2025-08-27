package cs3500.queensboard.provider.users;

import cs3500.queensboard.provider.model.Player;
import cs3500.queensboard.provider.model.QueensBloodModel;
import cs3500.queensboard.provider.strategy.Move;
import cs3500.queensboard.provider.strategy.Strategy;
import java.util.Optional;

/**
 * A computer-controlled player that selects moves based on a given strategy.
 * Used to automate gameplay decisions in the game.
 */
public class ComputerPlayer implements GameUser {
  private final Strategy strategy;

  /**
   * Constructs a computer player with the specified strategy.
   *
   * @param strategy the strategy used to choose moves
   */
  public ComputerPlayer(Strategy strategy) {
    this.strategy = strategy;
  }

  @Override
  public Optional<Move> getMove(QueensBloodModel model, Player player) {
    return strategy.chooseMove(model, player);
  }
}
