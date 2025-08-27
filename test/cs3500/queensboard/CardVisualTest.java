package cs3500.queensboard;

import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.GridLayout;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Point;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import cs3500.queensboard.model.Board;
import cs3500.queensboard.model.QueensCard;
import cs3500.queensboard.view.CardPanelBlue;
import cs3500.queensboard.view.CardPanelRed;

/**
 * Card Test Class to test card functionality;.
 */
public class CardVisualTest {
  private JFrame frame;
  private CardPanelRed cardComp;
  private CardPanelBlue cardComp2;

  @Before
  public void setUp() throws Exception {
    SwingUtilities.invokeAndWait(() -> {
      frame = new JFrame("Test");
      frame.setLayout(new GridLayout(1, 2));

      cardComp = new CardPanelRed(
              new QueensCard("Trooper", 2, 3, Board.Player.RED,
                      new char[][] {
                              {'X', 'X', 'X', 'X', 'X'},
                              {'X', 'X', 'I', 'X', 'X'},
                              {'X', 'X', 'C', 'I', 'X'},
                              {'X', 'X', 'X', 'X', 'X'},
                              {'X', 'X', 'X', 'X', 'X'}
                      })
      );

      cardComp2 = new CardPanelBlue(
              new QueensCard("Trooper", 2, 3, Board.Player.BLUE,
                      new char[][] {
                              {'X', 'X', 'X', 'X', 'X'},
                              {'X', 'X', 'I', 'X', 'X'},
                              {'X', 'I', 'C', 'X', 'X'},
                              {'X', 'X', 'X', 'X', 'X'},
                              {'X', 'X', 'X', 'X', 'X'}
                      })
      );

      frame.add(cardComp);
      frame.add(cardComp2);

      frame.setSize(400, 400);
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.setVisible(true);
    });
  }

  @Test
  public void testCardDisplayRED() throws AWTException {
    Robot robot = new Robot();

    robot.delay(20000);
    robot.waitForIdle();

    Point cardLocation = cardComp.getLocationOnScreen();
    Point pixelPoint = new Point(cardLocation.x + 20, cardLocation.y + 20);
    Color pixelColor = robot.getPixelColor(pixelPoint.x, pixelPoint.y);

    assertEquals(new Color(255, 102, 102), pixelColor);
  }

  @Test
  public void testCardDisplayBLUE() throws AWTException {
    Robot robot = new Robot();

    robot.delay(20000);
    robot.waitForIdle();

    Point cardLocation = cardComp2.getLocationOnScreen();
    Point pixelPoint = new Point(cardLocation.x + 40, cardLocation.y + 40);
    Color pixelColor = robot.getPixelColor(pixelPoint.x, pixelPoint.y);

    assertEquals(new Color(135, 206, 235), pixelColor);
  }

  @After
  public void tearDown() {
    frame.dispose();
  }
}
