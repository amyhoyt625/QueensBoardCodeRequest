package cs3500.queensboard.provider.strategy;

import cs3500.queensboard.provider.model.Player;
import cs3500.queensboard.provider.model.QueensBloodModel;
import java.util.List;
import java.util.Optional;

/**
 * Class will chain multiple strategies together. Idea is that there will be tries in order of the
 * strategies until a valid move is found, then breaks ties consistently.
 */
public class CompositeStrategy implements Strategy {
  private final List<Strategy> strategies;

  /**
   * Constructor for CompositeStrategy which enables coupling multiple strategies together, letting
   * players use other strategies when a given one does not find a valid move.
   *
   * @param strategies represents a list of strategies to be followed from first to last trying
   *                   valid moves
   */
  public CompositeStrategy(List<Strategy> strategies) {
    this.strategies = strategies;
  }

  @Override
  public Optional<Move> chooseMove(QueensBloodModel model, Player player) {
    for (Strategy strategy : strategies) {
      Optional<Move> move = strategy.chooseMove(model, player);
      if (move.isPresent()) {
        return move;
      }
    }
    return Optional.empty(); // None found a move
  }
}
