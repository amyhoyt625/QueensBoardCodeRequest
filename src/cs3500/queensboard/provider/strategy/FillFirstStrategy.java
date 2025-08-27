package cs3500.queensboard.provider.strategy;

import cs3500.queensboard.provider.model.Card;
import cs3500.queensboard.provider.model.Player;
import cs3500.queensboard.provider.model.QueensBloodModel;
import java.util.List;
import java.util.Optional;

/**
 * Strategy class will pick the first valid card and position that can be played.
 * Always plays the earliest legal move going from top-down, left-right order.
 */
public class FillFirstStrategy implements Strategy {

  @Override
  public Optional<Move> chooseMove(QueensBloodModel model, Player player) {
    List<Card> hand = model.getPlayerHand(player);

    for (int i = 0; i < hand.size(); i++) {
      for (int row = 0; row < model.getHeight(); row++) {
        for (int col = 0; col < model.getWidth(); col++) {
          if (model.canPlayCardAt(player, i, row, col)) {
            return Optional.of(new Move(i, row, col));
          }
        }
      }
    }

    return Optional.empty(); //if null, no valid move, pass
  }
}
