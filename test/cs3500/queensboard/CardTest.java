package cs3500.queensboard;

import org.junit.Test;

import cs3500.queensboard.model.Board;
import cs3500.queensboard.model.QueensCard;

import static cs3500.queensboard.model.QueensCard.reflectInfluence;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * A test class for the Queens Board game cards.
 * This class contains unit tests to verify the functionality of the QueensCard class.
 */
public class CardTest {
  @Test
  public void testInvalidCard() {
    assertThrows(IllegalArgumentException.class, () -> new QueensCard("TestCard",
            10, 2, Board.Player.RED, new char[5][5]));
  }

  @Test
  public void testGetValue() {
    QueensCard testCard = new QueensCard("TestCard", 1, 2, Board.Player.RED, new char[5][5]);
    assertEquals(testCard.getValue(), 2);
  }

  @Test
  public void testGetCost() {
    QueensCard testCard = new QueensCard("TestCard", 1, 2, Board.Player.RED, new char[5][5]);
    assertEquals(testCard.getCost(), 1);
  }

  @Test
  public void testGetName() {
    QueensCard testCard = new QueensCard("TestCard", 1, 2, Board.Player.RED, new char[5][5]);
    assertEquals(testCard.getName(), "TestCard");
  }

  @Test
  public void testGetInfluenceGrid() {
    QueensCard testCard = new QueensCard("TestCard", 1, 2, Board.Player.RED, new char[5][5]);
    char[][] array = new char[5][5];
    assertEquals(testCard.getInfluenceGrid(), array);
  }

  @Test
  public void testToString() {
    QueensCard testCard = new QueensCard("TestCard", 1, 2, Board.Player.RED, new char[5][5]);
    assertEquals(testCard.toString(), "R");
  }

  @Test
  public void testReflectInfluence() {
    char[][] redArray = {{'X', 'X', 'X', 'X', 'X'},
      {'X', 'X', 'I', 'X', 'X'},
      {'X', 'X', 'C', 'I', 'I'},
      {'X', 'X', 'X', 'X', 'X'},
      {'X', 'X', 'X', 'X', 'X'}};
    char[][] blueArray = {{'X', 'X', 'X', 'X', 'X'},
      {'X', 'X', 'I', 'X', 'X'},
      {'I', 'I', 'C', 'X', 'X'},
      {'X', 'X', 'X', 'X', 'X'},
      {'X', 'X', 'X', 'X', 'X'}};

    assertEquals(reflectInfluence(redArray), blueArray);
  }
}
