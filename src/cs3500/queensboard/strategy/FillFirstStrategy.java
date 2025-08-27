package cs3500.queensboard.strategy;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import cs3500.queensboard.model.Card;
import cs3500.queensboard.model.ReadOnlyQueensBoard;

/**
 * FillFirstStrategy is a Strategy that the computer player can
 * use for game play.
 */
public class FillFirstStrategy implements Strategy {

  /**
   * Strategy #1: Choose first card from player's hand & place in first possible position
   * Possible position: pawns in cell == cost of card && owner of pawns in cell == card owner.
   * @return Move object, which is either a valid move or pass
   */
  @Override
  public Move chooseMove(ReadOnlyQueensBoard board) {
    // First card in hand
    Card card = board.getHand().get(0);

    // Initialize move
    Move fillMove = new Move(true);

    // Iterate through game board to find FIRST valid place
    for (int row = 0; row < board.getHeight(); row++) {
      for (int col = 0; col < board.getWidth(); col++) {
        logMove("strategy-transcript-first.txt",
                new Move(row, col, card), 0);  // Add row, col to log

        //Debug: print what the computer sees
        System.out.println("Computer checking cell: (" + row + ", " + col + ")");
        System.out.println("Has card already: " + board.getCell(row, col).hasCard());

        if (board.getCell(row, col).getPawnCount() <= card.getCost()
                && board.getCell(row, col).getOwner() == card.getInfluence()
                && !board.getCell(row,col).hasCard()) {
          return new Move(row, col, card);
        }
      }
    }
    return fillMove; // If no move found, return pass
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
}
