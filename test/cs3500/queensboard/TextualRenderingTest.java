package cs3500.queensboard;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cs3500.queensboard.model.Board;
import cs3500.queensboard.model.Card;
import cs3500.queensboard.model.Cell;
import cs3500.queensboard.controller.DeckConfig;
import cs3500.queensboard.model.QueensCard;
import cs3500.queensboard.view.QueensBoardViewClass;

import static org.junit.Assert.assertEquals;

/**
 * A test class for verifying the textual rendering of the QueensBoard game.
 * This class ensures that the board state is correctly displayed as a string representation.
 */
public class TextualRenderingTest {

  private Board board;
  private String redPath = "docs" + File.separator + "RedDeck.config";
  private String bluePath = "docs" + File.separator + "BlueDeck.config";

  /**
   * Sets up the test environment before each test.
   * Initializes the board and loads the deck configuration.
   */
  @Before
  public void setup() {
    DeckConfig deckConfig = new DeckConfig();
    deckConfig.loadDeck(redPath, bluePath);
    board = new Board(3, 5, false, deckConfig, redPath, bluePath);
  }

  @Test
  public void renderEmptyBoard() {
    DeckConfig deckConfig = new DeckConfig();
    Board board = new Board(3, 3, false, deckConfig, redPath, bluePath);
    QueensBoardViewClass view = new QueensBoardViewClass(board);

    for (int row = 0; row < board.getHeight(); row++) {
      for (int col = 0; col < board.getWidth(); col++) {
        Cell cell = new Cell();
      }
    }
    String expected = "0 1_1 0\n" +
            "0 1_1 0\n" +
            "0 1_1 0";
    assertEquals(expected, view.toString());
  }

  @Test
  public void renderBoardWithPawns() {
    QueensCard card1 = new QueensCard("Card1", 1, 2, Board.Player.RED, new char[5][5]);
    List<Card> redDeck = new ArrayList<>();
    List<Card> blueDeck = new ArrayList<>();
    for (int i = 0; i < 15; i++) {
      redDeck.add(card1);
      blueDeck.add(card1); // need enough cards to fill all positions
    }
    board.startGame(redDeck, blueDeck, 1);
    System.out.println(board.toString()); // debugging step

    board.placeCardInPosition(0, 1, 0);
    String expected = "0 2___1 0\n" +
            "2 R1__1 0\n" +
            "0 2___1 0";

    QueensBoardViewClass view = new QueensBoardViewClass(board);
    assertEquals(expected, view.toString());
  }


}