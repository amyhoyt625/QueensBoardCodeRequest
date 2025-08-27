package cs3500.queensboard.adapter;

import cs3500.queensboard.model.Board;
import cs3500.queensboard.model.QueensCard;
import cs3500.queensboard.provider.model.BoardPiece;
import cs3500.queensboard.provider.model.Card;
import cs3500.queensboard.provider.model.CardGame;
import cs3500.queensboard.provider.model.Player;
import cs3500.queensboard.provider.model.PlayerState;

/**
 * Represents an adapter class that bridges a QueensCard from the model
 * with the Card interface expected by the provider's game framework.
 * This allows integration between the user's card logic and the provider's game logic.
 */
public class CardtoCardGame extends QueensCard implements Card {

  /**
   * Constructs a CardtoCardGame object using the given card details.
   *
   * @param name the name of the card
   * @param cost the cost of the card
   * @param value the value associated with the card
   * @param influence the player this card gives influence to
   * @param influenceGrid a 2D grid representing the card's influence pattern
   */
  public CardtoCardGame(String name, int cost, int value, Board.Player influence,
                        char[][] influenceGrid) {
    super(name, cost, value, influence, influenceGrid);
  }

  /**
   * Defines how this card interacts with the game board when placed.
   * This method is currently not implemented in this adapter.
   *
   * @param card the card being placed
   * @param board the current state of the board
   * @param row the row index of the placement
   * @param col the column index of the placement
   * @param playerState the state of the player placing the card
   */
  @Override
  public void cardInteraction(Card card, BoardPiece[][] board, int row, int col,
                              PlayerState playerState) {
    // Not implemented in this adapter
  }

  /**
   * Determines whether the card can be placed by the given player.
   * This implementation always returns false.
   *
   * @param card the card being evaluated
   * @param player the player attempting to place the card
   * @return false, indicating the card cannot be placed
   */
  @Override
  public boolean canPlaceCard(Card card, Player player) {
    return false;
  }

  /**
   * Helper method to convert a model player to a provider player.
   *
   * @param player the model's Board.Player enum to convert
   * @return the equivalent Player enum from the provider
   */
  private Player convertToPlayer(Board.Player player) {
    return Player.valueOf(player.toString());
  }

  /**
   * Returns the owner of this card, converted from the model's representation
   * to the provider's Player representation.
   *
   * @return the Player who owns this card
   */
  @Override
  public Player getOwner() {
    Board.Player player = getInfluence();
    return convertToPlayer(player);
  }

  /**
   * Handles the influence effect when the card is placed.
   * This method is a no-op in this adapter.
   *
   * @param owner the player who owns the card
   */
  @Override
  public void handleInfluenceInteraction(Player owner) {
    // Nothing happens
  }

  /**
   * Switches ownership of the card. This method is a no-op in this adapter.
   */
  @Override
  public void switchOwner() {
    // Nothing happens
  }

  /**
   * Returns the value of the card for display purposes.
   *
   * @return the display value of the card
   */
  @Override
  public int getDisplayValue() {
    return getValue();
  }

  /**
   * Creates a deep copy of this card, preserving all properties.
   *
   * @return a new instance of CardGame with the same attributes
   */
  @Override
  public BoardPiece copy() {
    return new CardGame(getName(), getCost(), getValue(), getInfluenceGrid(), getOwner());
  }
}
