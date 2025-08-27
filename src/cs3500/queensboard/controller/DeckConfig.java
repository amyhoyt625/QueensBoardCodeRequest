package cs3500.queensboard.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import cs3500.queensboard.model.Board;
import cs3500.queensboard.model.Card;
import cs3500.queensboard.model.QueensCard;

/**
 * Represents the configuration for loading card decks from a configuration file.
 */
public class DeckConfig implements DeckInterface {
  //main has fixed path, two inputs for diff color files


  //String path = "/Users/amyhoyt/Desktop/OOD/Homework 5/docs/RedDeck.config";
  private String redPath = "docs" + File.separator + "RedDeck.config";
  private String bluePath = "docs" + File.separator + "BlueDeck.config";

  private File redConfig = new File(redPath);
  private File blueConfig = new File(bluePath);
  private List<Card> redDeck = new ArrayList<>();
  private List<Card> blueDeck = new ArrayList<>();

  /**
   * Calls the loadDecks function to load decks for each player from given files.
   */
  @Override
  public void loadDeck(String redDeckPath, String blueDeckPath) {
    File redConfig = new File(redDeckPath);
    File blueConfig = new File(blueDeckPath);
    loadDecks(redConfig, redDeck, Board.Player.RED, false);
    loadDecks(blueConfig, blueDeck, Board.Player.BLUE, true);
  }

  //TODO should take in two paths? should still flip for second path aka blue?
  /**
   * Loads the deck configuration for each file, populating the Red and Blue decks.
   * If blue file/blue deck, make sure to reflect the Influence grid
   * @param file File of cards being read
   * @param deck Deck that stores the red and blue cards read in
   * @param player Red or Blue player the file is read for
   * @param reflect If blue deck, then reflect the influence grid
   */
  @Override
  public void loadDecks(File file, List<Card> deck, Board.Player player, boolean reflect) {
    try (Scanner scanner = new Scanner(file)) {
      while (scanner.hasNextLine()) {
        Scanner lineScanner = new Scanner(scanner.nextLine());
        String cardName = lineScanner.next();
        int cost = lineScanner.nextInt();
        int value = lineScanner.nextInt();
        lineScanner.close();

        char[][] influenceGrid = new char[5][5];
        for (int i = 0; i < 5; i++) {
          influenceGrid[i] = scanner.nextLine().toCharArray();
        }

        if (reflect) {
          influenceGrid = QueensCard.reflectInfluence(influenceGrid);
        }

        QueensCard card = new QueensCard(cardName, cost, value, player, influenceGrid);
        if (countOccurrences(deck, card) < 2) {
          deck.add(card);
        }
      }
    } catch (FileNotFoundException e) {
      System.err.println("Error: File not found at " + file.getPath());
    }
  }

  /**
   * Returns the list of cards in the Red Deck.
   */
  @Override
  public List<Card> getRedDeck() {
    return redDeck;
  }

  /**
   * Returns the list of cards in the Blue Deck.
   */
  public List<Card> getBlueDeck() {
    return blueDeck;
  }

  /**
   * Returns number of occurrences of the card in a deck.
   */
  @Override
  public int countOccurrences(List<Card> deck, QueensCard card) {
    int count = 0;
    for (Card c : deck) {
      if (c.getName().equals(card.getName())) {
        count++;
      }
    }
    return count;
  }

  /**
   * Returns the maximum size of a hand (1/3 of the deck).
   */
  @Override
  public int getMaxHandSize() {
    return redDeck.size() / 3;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    DeckConfig that = (DeckConfig) obj;
    return Objects.equals(redDeck, that.redDeck) &&
            Objects.equals(blueDeck, that.blueDeck);
  }

  @Override
  public int hashCode() {
    return Objects.hash(redDeck, blueDeck);
  }

}