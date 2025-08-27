package cs3500.queensboard.provider.model;

import java.util.List;

/**
 * Missing javadoc (given to us from provider).
 */
public interface IPlayerState {

  /**
   * Getter method for player in PlayerState.
   *
   * @return player assigned to PlayerState
   */
  Player getPlayer();

  /**
   * Getter method for this player's hand, in PlayerState.
   *
   * @return hand assigned to PlayerState
   */
  List<Card> getHand();

  List<Card> getDeck();

}
