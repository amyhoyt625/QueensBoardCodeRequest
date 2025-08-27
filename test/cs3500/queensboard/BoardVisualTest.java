package cs3500.queensboard;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import java.awt.Color;
import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;

import java.io.File;
import java.util.List;


import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import cs3500.queensboard.model.Board;
import cs3500.queensboard.controller.DeckConfig;
import cs3500.queensboard.model.Card;
import cs3500.queensboard.model.QueensCard;
import cs3500.queensboard.view.QueensBoardGUIViewClass;

/**
 * Board Test Class to test visuals for textual;.
 */
public class BoardVisualTest {
  private String redPath = "docs" + File.separator + "RedDeck.config";
  private String bluePath = "docs" + File.separator + "BlueDeck.config";
  private JFrame frame;
  private QueensBoardGUIViewClass boardView;
  private Board board;
  private List<Card> player1Deck;
  private List<Card> player2Deck;
  DeckConfig deckConfig;

  Card card1 = new QueensCard("Trooper", 2, 3, Board.Player.RED,
          new char[][]{
                  {'X', 'X', 'X', 'X', 'X'},
                  {'X', 'X', 'I', 'X', 'X'},
                  {'X', 'X', 'C', 'I', 'X'},
                  {'X', 'X', 'X', 'X', 'X'},
                  {'X', 'X', 'X', 'X', 'X'}
          });
  Card card2 = new QueensCard("Trooper", 2, 3, Board.Player.BLUE,
          new char[][]{
                  {'X', 'X', 'X', 'X', 'X'},
                  {'X', 'X', 'I', 'X', 'X'},
                  {'X', 'I', 'C', 'X', 'X'},
                  {'X', 'X', 'X', 'X', 'X'},
                  {'X', 'X', 'X', 'X', 'X'}
          });

  @Before
  public void setUp() throws Exception {
    SwingUtilities.invokeAndWait(() -> {
      deckConfig = new DeckConfig();
      deckConfig.loadDeck(redPath, bluePath);

      board = new Board(3, 5, false, deckConfig, redPath, bluePath);
      player1Deck = board.getDeckConfig().getRedDeck();
      player2Deck = board.getDeckConfig().getBlueDeck();

      try {
        board.startGame(player1Deck, player2Deck, 4);
      } catch (Exception e) {
        e.printStackTrace();
      }

      // Place pawns
      board.getCell(1, 2).addPawn(card1, 1);
      board.getCell(2, 1).addPawn(card2, 2);

      boardView = new QueensBoardGUIViewClass(board, Board.Player.RED);
      frame = new JFrame("Red Player Board");
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.add(boardView);
      frame.pack();
      frame.setVisible(true);

      QueensBoardGUIViewClass blueBoardView = new QueensBoardGUIViewClass(board, Board.Player.BLUE);
      JFrame blueFrame = new JFrame("Blue Player Board");
      blueFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      blueFrame.add(blueBoardView);
      blueFrame.pack();
      blueFrame.setVisible(true);
    });
  }

  @Test
  public void testBoardGrid() throws AWTException {
    Robot robot = new Robot();
    robot.delay(3000);
    robot.waitForIdle();

    // Check coloring
    Point boardLocation = boardView.getLocationOnScreen();
    Point gridPoint = new Point(boardLocation.x + 10, boardLocation.y + 10);
    Color pixelColor = robot.getPixelColor(gridPoint.x, gridPoint.y);

    // Check gridline position
    assertEquals(new Color(238, 238, 238), pixelColor);
  }



  @Test
  public void testRedPawn() throws AWTException {
    Robot robot = new Robot();
    robot.delay(2000);
    robot.waitForIdle();

    Point boardLocation = boardView.getLocationOnScreen();
    int cellSize = 130;

    // Red pawn at (1,2)
    Point redPawnPoint = new Point(
            boardLocation.x + 2 * cellSize + 65, // Column 2
            boardLocation.y + 1 * cellSize + 65  // Row 1
    );
    Color pixelColor = robot.getPixelColor(redPawnPoint.x, redPawnPoint.y);
    assertEquals(new Color(255, 102, 102), pixelColor);
  }

  @Test
  public void testBluePawn() throws AWTException {
    Robot robot = new Robot();
    robot.delay(2000);
    robot.waitForIdle();

    Point boardLocation = boardView.getLocationOnScreen();
    int cellSize = 130;

    // Blue pawn at (3,4)
    Point bluePawnPoint = new Point(
            boardLocation.x + 4 * cellSize + 65, // Column 4
            boardLocation.y + 3 * cellSize + 65  // Row 3
    );
    Color pixelColor = robot.getPixelColor(bluePawnPoint.x, bluePawnPoint.y);

    assertEquals(new Color(135, 206, 235), pixelColor);
  }

  @After
  public void tearDown() {
    frame.dispose();
  }
}
