package cs3500.queensboard.strategy;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import cs3500.queensboard.model.Board;
import cs3500.queensboard.model.Card;
import cs3500.queensboard.model.ReadOnlyQueensBoard;

/**
 * MaxRowStrategy is a Strategy that the computer player can
 * use for game play.
 */
public class MaxRowStrategy implements Strategy {

  /**
   * Strategy #2: Given a row, maximize the row score of the current player.
   * Max score by choosing cell of player with most pawns + adding card from hand w/ that cost
   * @return Move object, which is either a valid move or pass
   */
  @Override
  public Move chooseMove(ReadOnlyQueensBoard board) {
    BestCardInfo bestCardData = getBestCard(board.getHand());
    int bestIndex = bestCardData.index;
    Card bestCard = bestCardData.card;

    // If no valid card was found, return a pass move
    if (bestCard == null || bestIndex == -1) {
      return new Move(true);
    }

    Board.Player currentPlayer = board.getTurn();
    int bestValue = bestCard.getValue();
    int bestCost = bestCard.getCost();

    // Iterate through each row
    for (int row = 0; row < board.getHeight(); row++) {
      // Get the current and opponent's row scores
      int currRowScore = (currentPlayer == Board.Player.RED)
              ? board.getRedRowScore(row)
              : board.getBlueRowScore(row);
      int oppRowScore = (currentPlayer == Board.Player.RED)
              ? board.getBlueRowScore(row)
              : board.getRedRowScore(row);

      // Log row scores for debugging
      logScore("strategy-transcript-score.txt", row, currRowScore, oppRowScore);

      // If the current player has a lower or equal row-score than their opponent on that row,
      // this strategy chooses the first card and location option that increases their row-score
      // to be greater than or equal to the opponent’s row-score
      if (currRowScore <= oppRowScore && (currRowScore + bestValue) >= oppRowScore) {
        // Find a valid column to place the card in
        for (int col = 0; col < board.getWidth(); col++) {
          logMove("strategy-transcript-score.txt", new Move(row, col, bestCard), currRowScore);
          if (board.getCell(row, col).getPawnCount() <= bestCost
                  && board.getCell(row, col).getOwner() == currentPlayer) {
            return new Move(row, col, bestCard);
          }
        }
      }
    }

    // If no valid move was found, return a pass move
    return new Move(true);
  }

  //helper to get best card for max score
  private BestCardInfo getBestCard(List<Card> hand) {
    Card bestCard = null;
    int bestIndex = -1;
    int maxValue = -1;

    for (int i = 0; i < hand.size(); i++) {
      Card card = hand.get(i);
      if (card.getValue() > maxValue) {
        maxValue = card.getValue();
        bestCard = card;
        bestIndex = i;
      }
    }
    return new BestCardInfo(bestIndex, bestCard);
  }



  /**
   * Constructs a BoardStrategy with the given game board.
   *
   * @param fileName the file name
   * @param move Move object
   * @param rowScore Int of row score
   */
  public void logMove(String fileName, Move move, int rowScore) {
    try {
      PrintWriter logWriter = new PrintWriter(new FileWriter(fileName, true));
      // Open log file in append mode
      logWriter.println("Checked Position - Row: " + move.getRow() + ", Col: " +
              move.getCol() + ", Card: " + move.getCard().getCost());
      logWriter.flush();
      logWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Constructs a BoardStrategy with the given game board.
   *
   * @param fileName the file name
   * @param row integer row
   * @param currScore Int of current score
   * @param oppScore Int of opponent score
   */
  public void logScore(String fileName, int row, int currScore, int oppScore) {
    try {
      PrintWriter logWriter = new PrintWriter(new FileWriter(fileName, true));
      logWriter.println("Row: " + row +
              ", Current Player Score: " + currScore +
              ", Opponent Score: " + oppScore);
      logWriter.flush();
      logWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
