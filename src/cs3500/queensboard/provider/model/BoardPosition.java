package cs3500.queensboard.provider.model;

/**
 * A panel that visually represents a single card in the player's hand.
 * Used in the game's UI to display and interact with cards.
 */
public class BoardPosition implements BoardPiece {
  @Override
  public void cardInteraction(Card card, BoardPiece[][] board, int row, int col,
                              PlayerState playerState) {
    //Cannot place Card so throw IllegalStateException
    throw new IllegalStateException("Card cannot be placed on empty cell");
  }

  @Override
  public boolean canPlaceCard(Card card, Player player) {
    return false;
  }

  @Override
  public int getValue() {
    return 0;
  }

  @Override
  public Player getOwner() {
    return null;
  }

  @Override
  public void handleInfluenceInteraction(Player owner) {
    //nothing happens since influence impact will deal with adding pawns
  }

  @Override
  public void switchOwner() {
    //no owner, empty cell
  }

  @Override
  public int getDisplayValue() {
    return 0;
  }

  @Override
  public BoardPiece copy() {
    return null;
  }
}
