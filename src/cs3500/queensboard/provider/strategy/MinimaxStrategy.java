package cs3500.queensboard.provider.strategy;

import cs3500.queensboard.provider.model.Card;
import cs3500.queensboard.provider.model.Player;
import cs3500.queensboard.provider.model.QueensBloodModel;
import java.util.List;
import java.util.Optional;

/**
 * A strategy that simulates each valid move and chooses the one that minimizes the opponent's
 * best possible response. The opponents best possible response will be the one that gives the
 * player a better row-score. It assumes a known opponent strategy to evaluate the best move
 * the opponent can make after each candidate move.
 */
public class MinimaxStrategy implements Strategy {
  private final Strategy opponentStrategy;

  /**
   * Constructor for MinimaxStrategy that takes in a Strategy, which is the strategy that will
   * be assumed by MinimaxStrategy when defining the approach that minimizes opponent's best
   * possible move.
   *
   * @param opponentStrategy represents the opponent's strategy
   */
  public MinimaxStrategy(Strategy opponentStrategy) {
    this.opponentStrategy = opponentStrategy;
  }

  @Override
  public Optional<Move> chooseMove(QueensBloodModel model, Player player) {
    List<Card> hand = model.getPlayerHand(player);
    Move bestMove = null;
    int worstOpponentGain = Integer.MAX_VALUE;

    for (int index = 0; index < hand.size(); index++) {
      for (int row = 0; row < model.getHeight(); row++) {
        for (int col = 0; col < model.getWidth(); col++) {
          if (!model.canPlayCardAt(player, index, row, col)) {
            continue;
          }

          QueensBloodModel copy = model.copy();
          try {
            copy.placeCardInPosition(index, row, col);
          } catch (Exception e) {
            continue;
          }

          Player opponent = (player == Player.RED) ? Player.BLUE : Player.RED;
          Optional<Move> opponentMove = opponentStrategy.chooseMove(copy, opponent);
          int opponentScore = (opponent == Player.RED)
                  ? copy.redTotalScore()
                  : copy.blueTotalScore();

          if (opponentMove.isPresent()) {
            if (worstOpponentGain > opponentScore) {
              bestMove = new Move(index, row, col);
              worstOpponentGain = opponentScore;
            }
          }
        }
      }
    }
    return Optional.ofNullable(bestMove);
  }
}
