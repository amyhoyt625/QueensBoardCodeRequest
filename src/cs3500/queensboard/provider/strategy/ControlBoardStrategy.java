package cs3500.queensboard.provider.strategy;

import cs3500.queensboard.provider.model.Card;
import cs3500.queensboard.provider.model.Player;
import cs3500.queensboard.provider.model.QueensBloodModel;
import java.util.List;
import java.util.Optional;

/**
 * Strategy class will pick the move that gives the player ownership of the most cells. Considering
 * the case of a tie between positions, it will break tie by choosing the uppermost-leftmost
 * (so the uppermost first, then leftmost) position, where the leftmost will be chosen on a tie
 * between cards.
 */
public class ControlBoardStrategy implements Strategy {

  @Override
  public Optional<Move> chooseMove(QueensBloodModel model, Player player) {
    List<Card> hand = model.getPlayerHand(player);
    Move bestMove = null;
    int maxControlledCells = -1;

    for (int i = 0; i < hand.size(); i++) {
      Card card = hand.get(i);
      for (int row = 0; row < model.getHeight(); row++) {
        for (int col = 0; col < model.getWidth(); col++) {
          if (model.canPlayCardAt(player, i, row, col)) {
            int controlled = countControlledCellsAfterMove(model, player, i, row, col);
            if (controlled > maxControlledCells ||
                    (controlled == maxControlledCells && isBetterMove(bestMove, i, row, col))) {
              bestMove = new Move(i, row, col);
              maxControlledCells = controlled;
            }
          }
        }
      }
    }

    return Optional.ofNullable(bestMove);
  }

  /**
   * Simulates placing a card and counts how many cells would be controlled by the player
   * after the move. If the move is invalid, returns -1.
   *
   * @param model   the current game model to copy and simulate on
   * @param player  the player making the move
   * @param cardIdx the index of the card to be played
   * @param row     the row to place the card
   * @param col     the column to place the card
   * @return number of cells controlled by the player after the move, or -1 if move is invalid
   */
  private int countControlledCellsAfterMove(QueensBloodModel model, Player player,
                                            int cardIdx, int row, int col) {
    // Simulate the move on a copy of the model
    QueensBloodModel copy = model.copy();
    try {
      copy.placeCardInPosition(cardIdx, row, col);
    } catch (Exception ignored) {
      return -1;
    }

    int count = 0;
    for (int r = 0; r < copy.getHeight(); r++) {
      for (int c = 0; c < copy.getWidth(); c++) {
        Optional<Player> owner = copy.getOwnerAt(r, c);
        if (owner.isPresent() && owner.get() == player) {
          count++;
        }
      }
    }
    return count;
  }

  /**
   * Determines if a move is better than the current best based on row, column, and card index
   * priority. The preference order follows: upper rows then left columns then earlier cards in
   * hand.
   *
   * @param best the current best move
   * @param i    the card index of the new move
   * @param row  the row of the new move
   * @param col  the column of the new move
   * @return true if the new move is considered better, false otherwise
   */
  private boolean isBetterMove(Move best, int i, int row, int col) {
    if (best == null) {
      return true;
    }
    if (row < best.getRow()) {
      return true;
    }
    if (row == best.getRow() && col < best.getCol()) {
      return true;
    }
    return row == best.getRow() && col == best.getCol() && i < best.getCardIndex();
  }
}
