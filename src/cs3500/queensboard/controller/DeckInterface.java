package cs3500.queensboard.controller;


import java.io.File;
import java.util.List;

import cs3500.queensboard.model.Board;
import cs3500.queensboard.model.Card;
import cs3500.queensboard.model.QueensCard;

/**
 * Interface for deck configuration.
 */
public interface DeckInterface {

  /**
   * Calls the loadDecks function to load decks for each player from given files.
   */
  void loadDeck(String redDeckPath, String blueDeckPath);

  /**
   * Loads the deck configuration for each file, populating the Red and Blue decks.
   * If blue file/blue deck, make sure to reflect the Influence grid
   * @param file File of cards being read
   * @param deck Deck that stores the red and blue cards read in
   * @param player Red or Blue player the file is read for
   * @param reflect If blue deck, then reflect the influence grid
   */
  void loadDecks(File file, List<Card> deck, Board.Player player, boolean reflect);

  /**
   * Returns the list of cards in the Red Deck.
   */
  List<Card> getRedDeck();

  /**
   * Returns the list of cards in the Blue Deck.
   */
  List<Card> getBlueDeck();

  /**
   * Returns number of occurrences of the card in a deck.
   */
  int countOccurrences(List<Card> deck, QueensCard card);

  /**
   * Returns the maximum size of a hand (1/3 of the deck).
   */
  int getMaxHandSize();
}
