package cs3500.queensboard.provider.model;

/**
 * Represents a piece on the game board. A board piece can be either a card or a pawn
 * and interacts with other pieces according to game rules.
 */
public interface BoardPiece {

  /**
   * Defines how a card interacts with this board piece when placed on the board.
   * If the interaction is invalid an exception is thrown.
   *
   * @param card        represents the card being placed
   * @param board       represents the 2D ArrayList of BoardPieces game board
   * @param row         represents the row of interaction (0-based index)
   * @param col         represents column of interaction (0-based index)
   * @param playerState represents the player's state who is performing the interaction
   * @throws IllegalStateException if the placement is invalid due to game rules
   */
  void cardInteraction(Card card, BoardPiece[][] board, int row, int col, PlayerState playerState);

  /**
   * Determines whether a given card can be placed on this board piece.
   *
   * @param card   represents the card being placed
   * @param player represents the owner of the card trying to be placed
   * @return {@code true} if the card can be placed, {@code false} otherwise
   */
  boolean canPlaceCard(Card card, Player player);

  /**
   * Returns the value of this BoardPiece. For cards, this represents their score contribution.
   * Pawns typically return a value of zero.
   *
   * @return the integer value of this BoardPiece
   */
  int getValue();

  /**
   * Returns the owner of the BoardPiece, which can be {@code Player.RED} or {@code Player.BLUE}.
   *
   * @return the owner of this BoardPiece
   */
  Player getOwner();

  /**
   * Sets how the influence will affect the board pieces around it.
   *
   * @param owner player applying interaction effect.
   */
  void handleInfluenceInteraction(Player owner);

  /**
   * Switches the owner of the board piece between {@code Player.RED} or {@code Player.BLUE}.
   * When {@code Player.RED} is the owner, the ownership switches to {@code Player.BLUE}, and
   * when {@code Player.BLUE} is the owner, the ownership switches to {@code Player.RED}.
   */
  void switchOwner();

  /**
   * Returns the amount that should be displayed on the game board. For a card this will
   * be a value, and for a pawn this will be the amount of pawns on the cell.
   *
   * @return number that will be displayed.
   */
  int getDisplayValue();

  /**
   * Method to get a copy of the BoardPiece.
   *
   * @return a BoardPiece's copy
   */
  BoardPiece copy();

}
