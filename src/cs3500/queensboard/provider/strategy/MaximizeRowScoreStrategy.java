package cs3500.queensboard.provider.strategy;

import cs3500.queensboard.provider.model.Card;
import cs3500.queensboard.provider.model.Player;
import cs3500.queensboard.provider.model.QueensBloodModel;
import java.util.List;
import java.util.Optional;

/**
 * Strategy class will pick a move that increases the player's row score above the opponent's.
 * Checks rows from top-down and plays the first move that will enable the player to win that row.
 * If there is no play that would enable player to have a row-score greater than its opponent, the
 * player should pass.
 */
public class MaximizeRowScoreStrategy implements Strategy {
  @Override
  public Optional<Move> chooseMove(QueensBloodModel model, Player player) {
    Player opponent = (player == Player.RED) ? Player.BLUE : Player.RED;
    List<Card> hand = model.getPlayerHand(player);

    for (int row = 0; row < model.getHeight(); row++) {
      int currentScore = model.getRowScore(player, row);
      int opponentScore = model.getRowScore(opponent, row);

      if (currentScore > opponentScore) {
        continue; //Current player is already winning, skip to next row
      }

      for (int i = 0; i < hand.size(); i++) {
        for (int col = 0; col < model.getWidth(); col++) {
          if (model.canPlayCardAt(player, i, row, col)) {
            // Simulate placing this card to see if score increases
            int newScore = currentScore + hand.get(i).getValue();
            if (newScore > opponentScore) {
              return Optional.of(new Move(i, row, col));
            }
          }
        }
      }
    }

    return Optional.empty(); //If no valid move, then return null, should pass
  }
}