package cs3500.queensboard;

import java.io.FileWriter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import cs3500.queensboard.model.Board;
import cs3500.queensboard.model.Card;
import cs3500.queensboard.controller.DeckConfig;
import cs3500.queensboard.model.QueensBoard;
import cs3500.queensboard.strategy.FillFirstStrategy;
import cs3500.queensboard.strategy.MaxRowStrategy;
import cs3500.queensboard.strategy.Move;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Board Strategy Test Class to test all strategies with mocks.
 */
public class BoardStrategyTest {
  private QueensBoard game;
  DeckConfig deckConfig = new DeckConfig();
  private FillFirstStrategy strategy1;
  private MaxRowStrategy strategy2;
  private String redPath = "docs" + File.separator + "RedDeck.config";
  private String bluePath = "docs" + File.separator + "BlueDeck.config";
  private List<Card> redDeck;
  private List<Card> blueDeck;

  @Before
  public void setUp() {
    // Initialize the game board
    game = new Board(3, 5, false, deckConfig, redPath, bluePath);

    // Fetch the decks from the game instance after initialization
    redDeck = game.getDeckConfig().getRedDeck();
    blueDeck = game.getDeckConfig().getBlueDeck();
    // Create a DeckConfig instance to load the deck configuration
    deckConfig.loadDeck(redPath, bluePath);

    strategy1 = new FillFirstStrategy();
    strategy2 = new MaxRowStrategy();
  }

  // Log move coordinates and score to file
  private void logMove(String fileName, Move move, int score) {
    try (FileWriter writer = new FileWriter(fileName, true)) {
      writer.write("Move: Row " + move.getRow() + ", Col " + move.getCol() + "\n");
      writer.write("Score: " + score + "\n");
      writer.write("\n"); // Add a blank line between entries for clarity
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @Test
  public void testFillFirstValidMove() {
    game.startGame(redDeck, blueDeck, 5);
    Move move = strategy1.chooseMove(game);
    int cellScore = game.getScore(Board.Player.RED);

    // Log the strategy choice to file
    logMove("strategy-transcript-first.txt", move, cellScore);

    assertFalse(move.isPass());
    // Check if the first card is placed in the first valid position possible, e.g., 0,0
    assertEquals(0, move.getRow());
    assertEquals(0, move.getCol());
    assertTrue(cellScore > 0);
  }

  @Test
  public void testMaxRowScore() {
    game.startGame(redDeck, blueDeck, 5);
    game.pass();
    game.placeCardInPosition(0, 0, 4); // place a blue card in row 0
    Move move = strategy2.chooseMove(game);
    int redScore = game.getRedRowScore(0);
    int blueScore = game.getBlueRowScore(0);

    // Log the score to file
    logMove("strategy-transcript-score.txt", move, blueScore);

    assertFalse(move.isPass());
    // Ensure the strategy chooses the row with the highest score (blue row score in this case)
    assertEquals(0, move.getRow()); // It should place in the row with the higher score
    assertTrue(redScore >= blueScore);
  }

  @Test
  public void testMaxRowScoreWithLogging() {
    game.startGame(redDeck, blueDeck, 5);
    game.placeCardInPosition(0, 0, 0); // place a blue card in row 0
    Move move = strategy2.chooseMove(game);

    // Mocking the behavior by printing the rows considered by strategy
    logMove("strategy-transcript-row-logs.txt", move, game.getScore(Board.Player.RED));

    int redScore = game.getRedRowScore(0);
    int blueScore = game.getBlueRowScore(0);

    // Check if the chosen row is the one with a higher score
    assertFalse(move.isPass());
    assertTrue(redScore <= blueScore);
  }

}
