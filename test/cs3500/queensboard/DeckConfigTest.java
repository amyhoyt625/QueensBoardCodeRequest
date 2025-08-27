package cs3500.queensboard;

import org.junit.Test;
import org.junit.Before;

import java.io.File;

import cs3500.queensboard.controller.DeckConfig;

import static org.junit.Assert.assertNotNull;

/**
 * A test class for the DeckConfig component of the Queens Board game.
 * This class contains unit tests to verify the functionality of deck configuration,
 * including loading decks, retrieving cards, and ensuring proper initialization.
 */
public class DeckConfigTest {

  private String redPath = "docs" + File.separator + "RedDeck.config";
  private String bluePath = "docs" + File.separator + "BlueDeck.config";
  DeckConfig deckConfig = new DeckConfig();

  @Before
  public void setUp() {
    deckConfig.loadDeck(redPath, bluePath);
  }

  @Test
  public void testGetRedDeck() {
    assertNotNull(deckConfig.getRedDeck());
  }

  @Test
  public void testGetBlueDeck() {
    assertNotNull(deckConfig.getBlueDeck());
  }

}
