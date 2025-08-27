package cs3500.queensboard.strategy;

import cs3500.queensboard.model.Card;

/**
 * The {@code BestCardInfo} class stores information about the best possible card selection
 * in a QueensBoard game. It keeps track of the index of the card in the player's hand and
 * the card itself.
 * <p>
 *   This class is responsible for:
 *   <ul>
 *     <li> Storing the index of the selected card in a player's hand </li>
 *     <li> Storing the card object </li>
 *   </ul>
 * </p>
 */
public class BestCardInfo {
  int index;
  Card card;

  /**
   * Creates an instance of {@code BestCardInfo} object with the given index and card.
   *
   * @param index The index of the selected card in the player's hand
   * @param card The Card object being selected
   */
  BestCardInfo(int index, Card card) {
    this.index = index;
    this.card = card;
  }
}
