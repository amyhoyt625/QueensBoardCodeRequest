package cs3500.queensboard.provider.model;

/**
 * The Pawn class represents a pawn in the game. Each pawn will be located on the board
 * within a specific cell and should have a certain owner. The owner should be either
 * player Red or player Blue. No more than 3 pawns can be on each cell. Pawns will affect
 * player's ability to place a card on a selected cell or no, depending on card's cost.
 */
public class Pawn implements BoardPiece {
  private Player owner;
  private int numberOfPawns;

  /**
   * Constructs the pawn given an owner. The owner should be player
   * Red or should be player Blue.
   *
   * @param owner represents who pawn belongs to {@code Player.RED} or {@code Player.BLUE}
   */
  public Pawn(Player owner) {
    this.numberOfPawns = 1;
    this.owner = owner;
  }

  /**
   * Private constructor to create a copy of another Pawn.
   *
   * @param other is the pawn instance that is copied.
   */
  private Pawn(Pawn other) {
    this.owner = other.owner;
    this.numberOfPawns = other.numberOfPawns;
  }

  @Override
  public void cardInteraction(Card card, BoardPiece[][] board, int row, int col,
                              PlayerState playerState) {
    // When a card interacts with a pawn, it needs to first check if the number
    // of pawns in the position is greater than or equal to the card's cost
    if (!canPlaceCard(card, playerState.getPlayer())) {
      throw new IllegalStateException("Cell does not have enough of their own " +
              "pawns to pay for the card's cost");
    }

    //Then replace card and remove it from hand
    board[row][col] = card;
  }

  @Override
  public boolean canPlaceCard(Card card, Player player) {
    return this.owner == player && this.numberOfPawns >= card.getCost();
  }

  @Override
  public int getValue() {
    return 0;
  }

  /**
   * This method will increase the number of pawns on the given cell by 1 pawn. There can be
   * only a maximum of 3 pawns due to the pawn restriction range of 1-3.
   *
   * @throws IllegalArgumentException if there is an attempt to increase the number of pawns for
   *                                  more than 3.
   */
  public void increasePawn() {
    if (numberOfPawns >= 3) {
      throw new IllegalArgumentException("You can't have more than 3 pawns");
    } else {
      numberOfPawns += 1;
    }
  }

  @Override
  public Player getOwner() {
    return owner;
  }

  @Override
  public void handleInfluenceInteraction(Player owner) {
    if (this.owner == owner) {
      this.increasePawn();
    } else {
      this.owner = owner;
    }
  }

  /**
   * Switches the owners of the pawns. If original owner is player Red,
   * pawn owner will now be player Blue. Else, player Blue's ownership of the pawns
   * switches to player Red's.
   */
  public void switchOwner() {
    if (owner == Player.RED) {
      owner = Player.BLUE;
    } else {
      owner = Player.RED;
    }
  }

  /**
   * Gets the number of pawns that is currently on the board position.
   *
   * @return number of pawns.
   */
  public int getNumberOfPawns() {
    return numberOfPawns;
  }

  @Override
  public String toString() {
    return String.valueOf(this.numberOfPawns);
  }

  @Override
  public int getDisplayValue() {
    return this.numberOfPawns;
  }

  @Override
  public BoardPiece copy() {
    return new Pawn(this);
  }

}
