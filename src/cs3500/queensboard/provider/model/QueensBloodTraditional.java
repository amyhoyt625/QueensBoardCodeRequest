package cs3500.queensboard.provider.model;

import java.util.List;

/**
 * All the cards available for this model of game will be present in the
 * QueensBloodTraditionalCards configuration file.
 */
public class QueensBloodTraditional extends AbstractQueensBloodModel {
  /**
   * Constructor for the QueensBloodTraditional model type of game.
   *
   * @param row represents the number of rows game will contain, its width
   * @param col represents the number of cols game will contain, its height
   */
  public QueensBloodTraditional(int row, int col) {
    super(row, col);
  }

  /**
   * Adds the influence effect of the given placed card that is on the board.
   * Determines how the card should affect the surrounding 5x5 grid around it.
   * Affects are determined by 'X' and 'I'. 'X' has no effect to the position.
   * 'I' indicates the cell is influenced by the placed card. The influence
   * is only applied through a valid board positions. If a cell is empty a new
   * pawn is placed on the position of the card owner's color. If a cell contains
   * pawn(s) of the same owner, the number of pawns is increased by 1 with there being
   * a maximum of 3 pawns you can place in each cell. Finally, if a cell contains
   * pawns from the other player, the ownership of those pawns will switch (e.g.
   * 2 Blue pawns will now be 2 Red pawns in the cell).
   *
   * @param card the card that was placed.
   * @param row  the row index where card was placed.
   * @param col  the col index where card was placed.
   */
  @Override
  public void influenceImpact(Card card, int row, int col) {
    char[][] influenceGrid = card.getInfluenceGrid();
    Player owner = card.getOwner();

    // This will go through the 5x5 grid
    // -2 comes from getting center position with 5 positions
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        int influenceRow = row + (i - 2);
        int influenceCol = col + (j - 2);

        //Check if within board boundaries
        if (influenceRow < 0 || influenceRow >= getHeight() || influenceCol < 0
                || influenceCol >= getWidth()) {
          continue; //Then, will skip the out-of-bounds positions
        }

        BoardPiece piece = board[influenceRow][influenceCol];

        // Add pawn
        if (influenceGrid[i][j] == 'I') {
          if (piece instanceof BoardPosition) {
            board[influenceRow][influenceCol] = new Pawn(owner);
          } else {
            piece.handleInfluenceInteraction(owner);
            piece.switchOwner();
          }
        }
      }
    }
  }

  @Override
  public List<Card> getRemainingRedDeck() {
    return List.of();
  }

  @Override
  public List<Card> getRemainingBlueDeck() {
    return List.of();
  }

}
