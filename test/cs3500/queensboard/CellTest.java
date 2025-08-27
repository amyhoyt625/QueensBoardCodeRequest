package cs3500.queensboard;

import org.junit.Test;

import cs3500.queensboard.model.Board;
import cs3500.queensboard.model.Card;
import cs3500.queensboard.model.Cell;
import cs3500.queensboard.model.QueensCard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * A test class for the Cell component of the Queens Board game.
 * This class contains unit tests to verify the functionality of the Cell class,
 * including ownership, score calculations, and interactions with cards.
 */
public class CellTest {

  @Test
  public void withPawn() {
    Cell pawnCell = new Cell(3, Board.Player.RED);
    assertNotNull(pawnCell);
    assertFalse(pawnCell.hasCard()); // Make sure there is no card
  }

  @Test
  public void withCard() {
    QueensCard testCard = new QueensCard("Test Card", 1, 2, Board.Player.RED, new char[5][5]);
    Cell cardCell = new Cell(testCard, 2, Board.Player.RED);
    assertNotNull(cardCell);
  }

  @Test
  public void emptyCell() {
    Cell emptyCell = new Cell();
    assertFalse(emptyCell.hasCard());
    assertEquals(emptyCell.getPawnCount(), 0);
    assertTrue(emptyCell.isEmpty());
  }

  @Test
  public void invalidPawnValue() {
    QueensCard testCard = new QueensCard("Test Card", 1, 2, Board.Player.RED, new char[5][5]);
    assertThrows(IllegalArgumentException.class, () -> new Cell(testCard, 4, Board.Player.RED));
  }

  @Test
  public void testNonZeroGetCellScore() {
    QueensCard testCard = new QueensCard("Test Card", 1, 2, Board.Player.RED, new char[5][5]);
    Cell cardCell = new Cell(testCard, 2, Board.Player.RED);
    assertEquals(cardCell.getCellScore(), 2);
  }

  @Test
  public void testZeroGetCellScore() {
    Cell pawnCell = new Cell(3, Board.Player.RED);
    Cell emptyCell = new Cell();
    assertEquals(pawnCell.getCellScore(), 0);
    assertEquals(emptyCell.getCellScore(), 0);
  }

  @Test
  public void testGetOwnerCard() {
    QueensCard testCard = new QueensCard("Test Card", 1, 2, Board.Player.RED, new char[5][5]);
    Cell cardCell = new Cell(testCard, 2, Board.Player.RED);
    assertEquals(cardCell.getOwner(), Board.Player.RED);
  }

  @Test
  public void testGetOwnerPawn() {
    Cell pawnCell = new Cell(3, Board.Player.BLUE);
    assertEquals(pawnCell.getOwner(), Board.Player.BLUE);
  }

  @Test
  public void testGetOwnerEmpty() {
    Cell emptyCell = new Cell();
    assertEquals(emptyCell.getOwner(), Board.Player.NONE);
  }

  @Test
  public void testGetPawnCountPawn() {
    Cell pawnCell = new Cell(3, Board.Player.BLUE);
    assertEquals(pawnCell.getPawnCount(), 3);
  }

  @Test
  public void TrueHasCard() {
    QueensCard testCard = new QueensCard("Test Card", 1, 2, Board.Player.RED, new char[5][5]);
    Cell cardCell = new Cell(testCard, 2, Board.Player.RED);
    assertTrue(cardCell.hasCard());
  }

  @Test
  public void FalseHasCard() {
    Cell pawnCell = new Cell(3, Board.Player.BLUE);
    assertFalse(pawnCell.hasCard());
  }

  @Test
  public void TrueIsEmpty() {
    Cell emptyCell = new Cell();
    assertTrue(emptyCell.isEmpty());
  }

  @Test
  public void FalseIsEmpty() {
    QueensCard testCard = new QueensCard("Test Card", 1, 2, Board.Player.RED, new char[5][5]);
    Cell cardCell = new Cell(testCard, 2, Board.Player.RED);
    assertFalse(cardCell.isEmpty());
    Cell pawnCell = new Cell(3, Board.Player.BLUE);
    assertFalse(pawnCell.isEmpty());
  }

  @Test
  public void testValidAddPawn() {
    QueensCard testCard = new QueensCard("Test Card", 1, 2, Board.Player.RED, new char[5][5]);
    Cell pawnCell = new Cell(1, Board.Player.BLUE);
    pawnCell.addPawn(testCard, 2);
    assertEquals(pawnCell.getPawnCount(), 3);
  }

  @Test
  public void testAddPawnToCard() {
    QueensCard testCard = new QueensCard("Test Card", 1, 2, Board.Player.RED, new char[5][5]);
    Cell cardCell = new Cell(testCard, 2, Board.Player.RED);
    assertThrows(IllegalArgumentException.class, () -> cardCell.addPawn(testCard, 1));
  }

  @Test
  public void testAddPawnToInvalidCell() {
    QueensCard testCard = new QueensCard("Test Card", 1, 2, Board.Player.RED, new char[5][5]);
    Cell pawnCell = new Cell(3, Board.Player.BLUE);
    assertThrows(IllegalArgumentException.class, () -> pawnCell.addPawn(testCard, 3));
  }

  @Test
  public void changeOwnershipToBlue() {
    Cell pawnCell = new Cell(3, Board.Player.BLUE);
    pawnCell.changeOwnership();
    assertEquals(pawnCell.getOwner(), Board.Player.RED);
  }

  @Test
  public void changeOwnershipToRed() {
    Cell pawnCell2 = new Cell(3, Board.Player.RED);
    pawnCell2.changeOwnership();
    assertEquals(pawnCell2.getOwner(), Board.Player.BLUE);
  }

  @Test
  public void testGetCardWithCard() {
    QueensCard testCard = new QueensCard("Test Card", 1, 2, Board.Player.RED, new char[5][5]);
    Cell cardCell = new Cell(testCard, 2, Board.Player.RED);
    Card resultingCard = cardCell.getCard();
    assertNotNull(resultingCard);
  }

  @Test
  public void testGetCardWithoutCard() {
    Cell pawnCell = new Cell(3, Board.Player.BLUE);
    assertThrows(IllegalArgumentException.class, () -> pawnCell.getCard());
    Cell emptyCell = new Cell();
    assertThrows(IllegalArgumentException.class, () -> emptyCell.getCard());
  }


  @Test
  public void testToStringEmpty() {
    Cell emptyCell = new Cell();
    assertEquals("_", emptyCell.toString());
  }

  @Test
  public void testToStringCard() {
    QueensCard testCard = new QueensCard("Test Card", 1, 2, Board.Player.RED, new char[5][5]);
    Cell cardCell = new Cell(testCard, 2, Board.Player.RED);
    assertEquals(cardCell.toString(), "R");
  }

  @Test
  public void testToStringPawn() {
    Cell pawnCell = new Cell(3, Board.Player.BLUE);
    assertEquals(pawnCell.toString(), "3");
  }
}
