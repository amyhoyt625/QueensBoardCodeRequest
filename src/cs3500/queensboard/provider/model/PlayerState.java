package cs3500.queensboard.provider.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Represents the current state player is at. Class will contain fields of player, Red or Blue,
 * its respective deck and hand. Throughout the game, these cards will be changed reflecting
 * on the PlayerState.
 */
public class PlayerState implements IPlayerState {
  private final Player player;
  private final List<Card> hand;
  private final List<Card> deck;

  /**
   * Constructs a PlayerState with the given player, deck, and hand size.
   *
   * @param player   represents the player assigned to this player state
   * @param deck     represents the deck of cards assigned to this player state
   * @param handSize represents the size of hand
   * @throws IllegalArgumentException if the deck is null, contains null values, is empty,
   *                                  or has more than two copies of the same card.
   */
  public PlayerState(Player player, List<Card> deck, int handSize) {
    this.player = player;
    //create a copy of deck to avoid aliasing, this way, when altering the given deck
    //this.deck will not be affected
    if (deck == null || deckContainsNull(deck)) {
      throw new IllegalArgumentException("Any given deck can't be null or contain a null object");
    }
    if (deck.isEmpty()) {
      throw new IllegalArgumentException("Given decks cannot be empty");
    }
    if (!isDeckCardAmountInRange(deck)) {
      throw new IllegalArgumentException("Decks can't have more than two copies of the same card");
    }
    this.deck = new ArrayList<>(deck);
    this.hand = new ArrayList<>();
    drawInitialHand(handSize);
  }

  /**
   * Private constructor used internally.
   *
   * @param player   the player the state belongs to - Either Player Red or Player Blue.
   * @param deck     the list of cards that remain in the player's deck.
   * @param hand     current hand of cards of the player.
   * @param skipDraw if true, it will skip and draw the initial hand.
   */
  private PlayerState(Player player, List<Card> deck, List<Card> hand, boolean skipDraw) {
    this.player = player;
    this.deck = deck;
    this.hand = hand;
    // No drawInitialHand() here
  }

  /**
   * Will check if any of the values in the given deck are null.
   *
   * @param deck represents a list of Card
   * @return true if any of the values in given deck are null
   */
  private boolean deckContainsNull(List<Card> deck) {
    for (Card game : deck) {
      if (game == null) {
        return true;
      }
    }
    return false;
  }

  /**
   * Will check if the given deck contains more than two copies of same card.
   *
   * @param deck of cards given to player
   * @return true if deck contains more than two copies of same card
   */
  private boolean isDeckCardAmountInRange(List<Card> deck) {
    Map<Card, Integer> frequency = new HashMap<>();
    for (Card game : deck) {
      if (!frequency.containsKey(game)) {
        frequency.put(game, 1);
      } else {
        frequency.put(game, frequency.get(game) + 1);
        if (frequency.get(game) > 2) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Getter method for player in PlayerState.
   *
   * @return player assigned to PlayerState
   */
  @Override
  public Player getPlayer() {
    return player;
  }

  /**
   * Getter method for this player's hand, in PlayerState.
   *
   * @return hand assigned to PlayerState
   */
  @Override
  public List<Card> getHand() {
    return hand;
  }

  /**
   * Getter method for this player's deck, in PlayerState.
   *
   * @return deck assigned to PlayerState
   */
  public List<Card> getDeck() {
    return deck;
  }

  /**
   * Method will check conditions for a valid cardIdx, being bigger than 1, as hand and deck are
   * 0-indexed, and smaller than {@code hand.size()}.
   *
   * @param cardIdx represents the index of card on hand that will be removed
   * @throws IllegalArgumentException if cardIdx is smaller or equal to 0 or greater than hand size
   */
  public void removeCardFromHand(int cardIdx) {
    if (cardIdx < 0 || cardIdx >= hand.size()) {
      throw new IllegalArgumentException("cardIdx cannot be smaller " +
              "than 0 and smaller than hand size");
    }
    hand.remove(cardIdx);
  }

  /**
   * Replaces a card from hand with another card from the deck. Card replacement will be chosen
   * randomly from the deck.
   *
   * @throws IllegalStateException if there is an attempt to drawCard on empty deck.
   */
  public void drawCard() {
    if (deck.isEmpty()) {
      throw new IllegalStateException("Cannot draw a card since the deck is empty.");
    }

    //random.nextInt(list.size()) creates a random index between 0 and list.size() - 1.
    Random random = new Random();
    Card randomCard = deck.get(random.nextInt(deck.size()));
    deck.remove(randomCard);
    hand.add(randomCard);
  }

  /**
   * Method will deal the initial hand of the Player at random. The size of hand will
   * be hand size, 0-indexed.
   *
   * @param handSize represents the size of the hand determined in model's startGame.
   */
  private void drawInitialHand(int handSize) {
    for (int i = 0; i < handSize; i++) {
      drawCard();
    }
  }

  /**
   * Method to get a copy of the PlayerState.
   *
   * @return a copy of the PlayerState
   */
  public PlayerState copy() {
    List<Card> deckCopy = new ArrayList<>(this.deck);
    List<Card> handCopy = new ArrayList<>(this.hand);

    return new PlayerState(this.player, deckCopy, handCopy, true);
  }
}

