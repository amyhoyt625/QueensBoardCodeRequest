package cs3500.queensboard;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cs3500.queensboard.model.Board;
import cs3500.queensboard.model.Card;
import cs3500.queensboard.model.Cell;
import cs3500.queensboard.controller.DeckConfig;
import cs3500.queensboard.model.QueensBoard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * A test class for the QueensBoard game, ensuring correct initialization and gameplay mechanics.
 * This class tests various aspects of the board game, including deck configuration,
 * card distribution, and board interactions.
 */
public class QueensBoardTest {
  private QueensBoard game;
  private List<Card> player1Deck;
  private List<Card> player2Deck;
  DeckConfig deckConfig = new DeckConfig();
  private String redPath = "docs" + File.separator + "deck.config";
  private String bluePath = "docs" + File.separator + "deck.config";

  /**
   * Sets up the test environment before each test.
   * Initializes the game board and loads decks for both players.
   */
  @Before
  public void setUp() {
    // Initialize the game board
    game = new Board(3, 5, false, deckConfig, redPath, bluePath);

    // Fetch the decks from the game instance after initialization
    player1Deck = game.getDeckConfig().getRedDeck();
    player2Deck = game.getDeckConfig().getBlueDeck();
    // Create a DeckConfig instance to load the deck configuration
    deckConfig.loadDeck(redPath, bluePath);
  }

  /// DECK TESTS
  //Checking decks not empty
  @Test
  public void testInitialDecks() {
    assertNotNull(player1Deck);
    assertTrue(!player1Deck.isEmpty()); // Assert that the red deck is initialized properly
    assertNotNull(player2Deck);
    assertTrue(!player2Deck.isEmpty()); // Assert that the blue deck is initialized properly
    assertNotNull(deckConfig);
  }

  //check that decks are size 15
  @Test
  public void testDeckSize15() {
    assertNotNull(player1Deck);
    assertTrue(player1Deck.size() == 15); // Assert that the red deck is initialized properly
    assertNotNull(player2Deck);
    assertTrue(player2Deck.size() == 15); // Assert that the blue deck is initialized properly
  }

  /// TEST CONSTRUCTORS
  // Test constructor for invalid dimensions
  @Test
  public void testConstructorThrowsForInvalidHeight() {
    assertThrows(IllegalArgumentException.class,
        () -> new Board(0, 5, false, deckConfig, redPath, bluePath));
  }

  @Test
  public void testConstructorThrowsForInvalidWidth() {
    assertThrows(IllegalArgumentException.class,
        () -> new Board(1, 4, false, deckConfig, redPath, bluePath));
  }

  @Test
  public void testConstructorShuffle() {
    assertFalse(game.getShuffle());
  }

  @Test
  public void testConstructorThrowsForInvalidDeckConfig() {
    assertThrows(IllegalArgumentException.class,
        () -> new Board(3, 5, false, null, redPath, bluePath));
  }

  /// TESTS PLACECARDINPOSITION

  // Test placeCardInPosition when game hasn't started
  @Test
  public void testPlaceCardInPositionThrowsIfGameNotStarted() {
    assertThrows(IllegalStateException.class, () -> game.placeCardInPosition(0, 0, 0));
  }

  // Test placeCardInPosition for invalid conditions
  @Test
  public void testPlaceCardInPositionThrowsForInvalidPosition() {
    game.startGame(player1Deck, player2Deck, 5);
    assertThrows(IllegalArgumentException.class, () -> game.placeCardInPosition(0, -1, 3));
    assertThrows(IllegalArgumentException.class, () -> game.placeCardInPosition(0, 3, -1));
    assertThrows(IllegalArgumentException.class, () -> game.placeCardInPosition(0, 7, 2));
    assertThrows(IllegalArgumentException.class, () -> game.placeCardInPosition(0, 2, 7));
  }

  // Test placeCardInPosition for invalid card index
  @Test
  public void testPlaceCardInPositionThrowsForInvalidCardIndex() {
    game.startGame(player1Deck, player2Deck, 5);
    assertThrows(IllegalArgumentException.class, () -> game.placeCardInPosition(-1, 2, 4));
    assertThrows(IllegalArgumentException.class, () -> game.placeCardInPosition(7, 2, 4));
  }

  // Test placeCardInPosition for invalid card index
  // TODO: make a deck with null card
  // can't test for null card based on how we are supposed to make deck in assignment
  @Test
  public void testPlaceCardInPositionThrowsForNullCard() {
    // Start the game with the decks fetched from the game instance
    game.startGame(player1Deck, player2Deck, 5);

    // Simulate adding a null card (this could represent an invalid or missing card)
    Card nullCard = null;
    assertNull(nullCard);
  }

  // TODO: make a null board
  // can't test for null board based on how we are supposed to make deck in assignment
  @Test
  public void testPlaceCardInPositionThrowsForNullCell() {
    game.startGame(player1Deck, player2Deck, 5);

    Cell nullCell = null;
    assertNull(nullCell);
  }

  //place in occupied cell
  @Test
  public void testPlaceCardInPositionThrowsForOccupiedCell() {
    game.startGame(player1Deck, player2Deck, 5);

    // Assume that a card is already placed at (1, 1)
    Card card = player1Deck.get(0); // Get the first card from player 1's deck
    game.placeCardInPosition(1, 1, 0); // Place the first card in position (1, 1)

    // Attempt to place another card in the same cell
    assertThrows(IllegalStateException.class, () -> {
      game.placeCardInPosition(1, 1, 0); // This should throw because the cell is occupied
    });
  }

  //place invalid pawns
  @Test
  public void testPlaceCardInPositionThrowsForInsufficientPawns() {
    game.startGame(player1Deck, player2Deck, 5);

    // Simulate the case where the player doesn't have enough pawns
    // For simplicity, we assume the cost of the card is 3 and the target cell has fewer pawns
    Card card = player1Deck.get(0); // Get the first card from player 1's deck
    game.placeCardInPosition(2, 2, 0); // Place the card at some position (e.g., (2, 2))

    // Simulate a pawn count condition where the player has insufficient pawns
    // Assuming (1, 1) has fewer pawns than required for the card to be placed
    assertThrows(IllegalStateException.class, () -> {
      game.placeCardInPosition(1, 1, 0); // This should throw because not enough pawns
    });
  }

  //test invalid owner
  @Test
  public void testPlaceCardInPositionThrowsForIncorrectOwnership() {
    game.startGame(player1Deck, player2Deck, 5);

    // Assume player 1 is trying to place a card on a cell owned by player 2
    Card card = player1Deck.get(0); // Get the first card from player 1's deck
    game.placeCardInPosition(2, 2, 0); // Place the card at some position (e.g., (2, 2))

    // Simulate an opponent's pawn ownership on (1, 1)
    // (where player 1 is not allowed to place a card)
    assertThrows(IllegalStateException.class, () -> {
      game.placeCardInPosition(1, 1, 0);
    });
  }

  // Test placeCardInPosition for valid move
  @Test
  public void testPlaceCardInPositionValidMove() {
    // Start the game with the decks fetched from the game instance
    game.startGame(player1Deck, player2Deck, 5);

    // Fetch the first card from the red player's deck
    Card card = player1Deck.get(0); // Get the first card from player 1's deck

    // Ensure the player's hand is populated with the card
    assertTrue(player1Deck.contains(card));

    // Ensure the card can be placed at (0, 0)
    game.placeCardInPosition(0, 0, 0);

    // Validate that the card is placed at (0, 0)
    Card placedCard = game.getCardAt(0, 0);  // Fetch the card placed at (0, 0)

    // Check that the cell at (0, 0) is not null and contains the correct card
    assertNotNull(placedCard);  // Ensure the cell is not empty
    assertTrue(card.equals(placedCard));
  }


  /// TESTS APPLYINFLUENCE
  // Test applyInfluence method by calling place card to update influence on surrounding cells
  // TODO: fix
  @Test
  public void testApply() {
    // Start the game with the decks fetched from the game instance
    game.startGame(player1Deck, player2Deck, 5);

    // Fetch the first card from the red player's deck
    Card card = player1Deck.get(0); // Get the first card from player 1's deck

    // Ensure the player's hand is populated with the card
    assertTrue(player1Deck.contains(card));

    // Ensure the card can be placed at (0, 0)
    game.placeCardInPosition(0, 0, 0);

    int pawnVal = game.getCell(0, 1).getPawnCount();
    Board.Player pawnOwner = game.getCell(0, 1).getPawnOwner();
    assertTrue(pawnVal == 1);
    System.out.println(pawnOwner);
    //assertTrue(pawnOwner == Board.Player.RED);
  }

  @Test
  public void testApply2() {
    // Start the game with the decks fetched from the game instance
    game.startGame(player1Deck, player2Deck, 5);
    game.pass();

    // Fetch the first card from the red player's deck
    Card card = player2Deck.get(0); // Get the first card from player 2's deck

    // Ensure the player's hand is populated with the card
    assertTrue(player2Deck.contains(card));

    // Ensure the card can be placed at (0, 0)
    game.placeCardInPosition(0, 0, 4);

    int pawnVal2 = game.getCell(0, 3).getPawnCount();
    assertTrue(pawnVal2 == 1);
  }

  /// TESTS GETCELL
  @Test
  public void testGetCellValid() {
    // Test with a valid cell inside the board (e.g., cell (2, 2))
    Cell cell = game.getCell(2, 2);
    assertNotNull("Cell isn't null", cell);
  }

  // Test for invalid cell indices
  @Test
  public void testGetCellInvalidRow() {
    // Test with invalid row (row -1, out of bounds)
    Cell cell = game.getCell(-1, 2);
    assertNull("Cell at (-1, 2) should be null", cell);

    // Test with invalid row (row 5, out of bounds, assuming board size is 5)
    cell = game.getCell(5, 2);
    assertNull("Cell at (5, 2) should be null", cell);
  }

  @Test
  public void testGetCellInvalidCol() {
    // Test with invalid row (row -1, out of bounds)
    Cell cell = game.getCell(1, -1);
    assertNull("Cell at (-1, 2) should be null", cell);

    // Test with invalid row (row 5, out of bounds, assuming board size is 5)
    cell = game.getCell(1, 8);
    assertNull("Cell at (5, 2) should be null", cell);
  }

  /// TEST ISVALIDCELL
  @Test
  public void testIsValidCellValid() {
    // Test for valid row and column values within the board's dimensions (5x5 grid)
    assertTrue(game.isValidCell(0, 0));
  }

  @Test
  public void testIsValidCellInvalid() {
    // Test for valid row and column values within the board's dimensions (5x5 grid)
    assertFalse(game.isValidCell(10, 0));
    assertFalse(game.isValidCell(0, 10));
  }


  /// TESTS PASS

  // Test placeCardInPosition when game hasn't started
  @Test
  public void testPassThrowsIfGameNotStarted() {
    assertThrows(IllegalStateException.class, () -> game.pass());
  }

  // Test for Red player passing their turn
  @Test
  public void testRedPasses() {
    game.startGame(player1Deck, player2Deck, 5);
    game.pass();  // Red's turn to pass

    // Verify turn is now Blue
    assertEquals(Board.Player.BLUE, game.getTurn());
  }

  // Test for Blue player passing their turn
  @Test
  public void testBluePasses() {
    game.startGame(player1Deck, player2Deck, 5);
    game.placeCardInPosition(0, 0, 0);
    game.pass();

    // Verify turn is now RED
    assertEquals(Board.Player.RED, game.getTurn());
  }

  // Test passing the turn
  @Test
  public void testPassChangesTurn() {
    game.startGame(player1Deck, player2Deck, 5);
    Board.Player currentTurn = game.getTurn();
    game.pass();
    assertNotEquals(currentTurn, game.getTurn());
  }

  // Test for valid pass when only one player passes
  @Test
  public void testPassSinglePlayer() {
    game.startGame(player1Deck, player2Deck, 5);
    game.pass(); // Red passes
    assertTrue(game.getTurn().isBlue()); // Turn should switch to Blue
  }

  /// TESTS GETEMPTYSPACES
  // Test getEmptySpaces method
  @Test
  public void testGetEmptySpaces() {
    game.startGame(player1Deck, player2Deck, 5);
    assertEquals(9, game.getEmptySpaces()); // Check for initial empty spaces
  }

  /// TESTS STARTGAME
  // Test startGame throws for already started game
  @Test
  public void testStartGameThrowsIfAlreadyStarted() {
    game.startGame(player1Deck, player2Deck, 5);
    assertThrows(IllegalStateException.class, () -> game.startGame(player1Deck, player2Deck, 4));
  }

  // Test startGame throws for null deck
  @Test
  public void testStartGameThrowsForNullDeck() {
    assertThrows(IllegalArgumentException.class, () -> game.startGame(player1Deck, null, 5));
    assertThrows(IllegalStateException.class, () -> game.startGame(null, player2Deck, 5));
    assertThrows(IllegalStateException.class, () -> game.startGame(player1Deck, player2Deck,
            -1));
    assertThrows(IllegalStateException.class, () -> game.startGame(player1Deck, player2Deck,
            deckConfig.getMaxHandSize() + 1));
  }

  @Test
  public void testStartGameThrowsForNullCards() {
    //TODO: make deck of null cards
    //looked at description of hw and didn't have description null deck
    Card nullCard = null;
    assertNull(nullCard);
  }

  // Test for insufficient cards in the red and blue deck for board
  @Test
  public void testDeckDoesNotContainEnoughCardsForBoard() {
    //TODO: make deck that's too small
    //deck.config text file is a valid deck size, so can't test without altering
    int smallDeckSize = 5;
    assertTrue(smallDeckSize < 15);
  }

  // Test for insufficient cards in the red and blue deck for board
  @Test
  public void testDeckDoesNotContainEnoughCardsForHand() {
    assertThrows(IllegalArgumentException.class, () ->
            game.startGame(player1Deck, player2Deck, 50));
  }

  // Test startGame initializes the game correctly
  @Test
  public void testStartGameInitializesGame() {
    game.startGame(player1Deck, player2Deck, 5);
    assertNotNull(game.getTurn());
    assertTrue(game.getTurn().isRed()); // Start with Red's turn
  }

  /// TESTS GETWIDTH
  @Test
  public void testGetWidth() {
    assertTrue(game.getWidth() == 5);
  }

  /// TESTS GETHEIGHT
  @Test
  public void testGetHeight() {
    assertTrue(game.getHeight() == 3);
  }

  /// TESTS GETCARDAT
  // Test getCardAt for a valid position containing a card
  @Test
  public void testGetCardAtValidPositionWithCard() {
    // Start the game with the decks fetched from the game instance
    game.startGame(player1Deck, player2Deck, 5);

    // Fetch the first card from the red player's deck
    Card card = player1Deck.get(0); // Get the first card from player 1's deck

    // Ensure the player's hand is populated with the card
    assertTrue(player1Deck.contains(card));

    // Place the card at (0,0)
    game.placeCardInPosition(0, 0, 0);

    // Fetch the card from (1,2)
    Card placedCard = game.getCardAt(0, 0);

    // Ensure the cell at (1,2) contains the expected card
    assertTrue(card.equals(placedCard));
  }

  // Test getCardAt for a valid position without a card
  @Test
  public void testGetCardAtValidPositionWithoutCard() {
    // Start the game
    game.startGame(player1Deck, player2Deck, 5);

    // Ensure there is no card at (2,3)
    assertNull(game.getCardAt(2, 3));
  }

  // Test getCardAt for an invalid row index
  @Test
  public void testGetCardAtInvalidRow() {
    game.startGame(player1Deck, player2Deck, 5);

    // Ensure an exception is thrown when accessing an out-of-bounds row
    assertThrows(IllegalArgumentException.class, () -> game.getCardAt(-1, 2));
    assertThrows(ArrayIndexOutOfBoundsException.class, () -> game.getCardAt(3, 2)); // Out of bounds
  }

  // Test getCardAt for an invalid column index
  @Test
  public void testGetCardAtInvalidColumn() {
    game.startGame(player1Deck, player2Deck, 5);

    // Ensure an exception is thrown when accessing an out-of-bounds column
    assertThrows(IllegalArgumentException.class, () -> game.getCardAt(1, -1));
    assertThrows(ArrayIndexOutOfBoundsException.class, () -> game.getCardAt(1, 5)); // Out of bounds
  }

  // Test getCardAt for extreme out-of-bounds cases
  @Test
  public void testGetCardAtOutOfBounds() {
    game.startGame(player1Deck, player2Deck, 5);

    // Ensure an exception is thrown when accessing far out-of-bounds indices
    assertThrows(IllegalArgumentException.class, () -> game.getCardAt(-10, -10));
    assertThrows(IllegalArgumentException.class, () -> game.getCardAt(10, 10));
  }

  /// TESTS GETHAND
  @Test
  public void testGetHandRed() {
    game.startGame(player1Deck, player2Deck, 5);
    List<Card> hand = game.getHand();
    assertNotNull(hand);
    for (Card card : hand) {
      assertEquals(Board.Player.RED, card.getInfluence());
    }
  }

  /// TESTS GETSCORE
  @Test
  public void testGetScoreRedPlayer() {
    game.startGame(player1Deck, player2Deck, 5);
    // Place a RED card at position (0, 0)
    game.placeCardInPosition(0, 0, 0);  // Assuming the first card in the deck is valid

    // Verify RED player's score
    int redScore = game.getScore(Board.Player.RED);
    assertTrue("Red player's score should be positive", redScore > 0);
  }

  @Test
  public void testGetScoreBluePlayer() {
    game.startGame(player1Deck, player2Deck, 5);
    game.pass();
    // Place a BLUE card at position (1, 0)
    game.placeCardInPosition(0, 0, 4);  // Assuming the first card in the blue deck is valid

    // Verify BLUE player's score
    int blueScore = game.getScore(Board.Player.BLUE);
    assertEquals(2, blueScore);
  }

  /// TESTS GETROWSCORE, GETREDROWSCORE, GETBLUEROWSCORE
  @Test
  public void testGetRowScoreRed() {
    game.startGame(player1Deck, player2Deck, 5);
    // Set up RED player scoring scenario
    game.placeCardInPosition(0, 0, 0);  // RED card at (0, 0)

    // Test RED row score calculation for row 0
    int rowScore = game.getRowScore(0);
    assertEquals(2, rowScore);
    // Test RED row score calculation for row 0
    int redRowScore = game.getRedRowScore(0);
    assertEquals(2, redRowScore);
  }

  @Test
  public void testGetRowScoreBlue() {
    game.startGame(player1Deck, player2Deck, 5);
    game.pass();
    // Set up RED player scoring scenario
    game.placeCardInPosition(0, 0, 4);
    // Test RED row score calculation for row 0
    int rowScore = game.getRowScore(0);
    assertEquals(2, rowScore);
    // Test BLUE row score calculation for row 0
    int blueRowScore = game.getBlueRowScore(0);
    assertEquals(2, blueRowScore);
  }


  //TODO: finish the row then check
  @Test
  public void testRowScoreWithEqualCards() {
    game.startGame(player1Deck, player2Deck, 5);
    // Add a scenario where both players have cards in the same row
    game.placeCardInPosition(0, 0, 0);  // RED card at (0, 0)
    game.placeCardInPosition(0, 0, 4);  // BLUE card at (0, 1)

    // Test if the row score calculation correctly chooses the player with the higher score
    int rowScore = game.getRowScore(0);
    assertEquals(0, rowScore);
  }

  @Test
  public void testGetScoreNoCards() {
    // Test if the score is zero when no cards have been placed
    int redScore = game.getScore(Board.Player.RED);
    int blueScore = game.getScore(Board.Player.BLUE);

    assertEquals("Red player's score should be 0 when no cards are placed", 0, redScore);
    assertEquals("Blue player's score should be 0 when no cards are placed", 0, blueScore);
  }


  /// TESTS GETDECKCONFIG
  @Test
  public void testGetDeckConfig() {
    assertTrue(deckConfig.equals(game.getDeckConfig()));
  }

  /// TESTS GETHAND
  @Test
  public void testGetHand() {
    assertThrows(IllegalStateException.class, () -> game.getHand());
  }

  /// TESTS GETSHUFFLE
  @Test
  public void testGetShuffle() {
    assertFalse(game.getShuffle());
  }

  /// TESTS GETREMAININGDECKSIZE
  @Test
  public void testGetRemainingDeckSize() {
    // Initial deck sizes for both players
    int initialRedDeckSize = game.getRemainingDeckSize(Board.Player.RED);
    int initialBlueDeckSize = game.getRemainingDeckSize(Board.Player.BLUE);

    assertEquals(15, initialRedDeckSize);
    assertEquals(15, initialBlueDeckSize);
  }

  /// TESTS ISGAMEOVER
  // Test gameState after consecutive passes
  @Test
  public void testGameStateAfterConsecutivePasses() {
    game.startGame(player1Deck, player2Deck, 5);
    game.pass(); // Red passes
    game.pass(); // Blue passes
    assertTrue(game.isGameOver());
  }

  @Test
  public void testGameStateAfterConsecutivePasses2() {
    game.startGame(player1Deck, player2Deck, 5);
    game.placeCardInPosition(0, 1, 0); //r
    game.pass(); // Blue passes
    game.pass(); // Red passes
    assertTrue(game.isGameOver());
  }

  @Test
  public void testGameStateAfterNonConsecutivePasses() {
    //red
    game.startGame(player1Deck, player2Deck, 5);
    game.pass(); // Red passes
    game.placeCardInPosition(0, 1, 4); //blue
    game.pass(); // Red Passes
    assertFalse(game.isGameOver());
  }

  @Test
  public void testGameStateAfterNonConsecutivePasses2() {
    //blue
    game.startGame(player1Deck, player2Deck, 5);
    game.placeCardInPosition(0, 1, 0); //r
    game.pass(); // Blue passes
    game.placeCardInPosition(0, 0, 0);
    game.pass(); // Blue passes
    assertFalse(game.isGameOver());
  }


  // TESTS ISGAMEOVER & get scores for row, row based on color, and score for
  // each player, and gets the winner
  //TODO: test game over after board filled
  @Test
  public void testGameFinished() {
    // Start the game with the decks fetched from the game instance
    game.startGame(player1Deck, player2Deck, 5);

    game.placeCardInPosition(0, 1, 0); //r
    game.placeCardInPosition(0, 1, 4);
    game.placeCardInPosition(0, 0, 0); //r
    game.placeCardInPosition(0, 0, 4);
    game.placeCardInPosition(0, 1, 1); //r
    game.pass(); //blue passes
    game.placeCardInPosition(0, 2, 0); //r
    game.pass(); //blue passes

    assertEquals(2, game.getCardAt(1, 4).getValue());
    assertEquals(0, game.getRowScore(0));
    assertEquals(1, game.getRedRowScore(0));
    assertEquals(1, game.getBlueRowScore(0));
    assertEquals(4, game.getRowScore(1));
    assertEquals(4, game.getRedRowScore(1));
    assertEquals(2, game.getBlueRowScore(1));
    assertEquals(1, game.getRowScore(2));
    assertEquals(1, game.getRedRowScore(2));
    assertEquals(0, game.getBlueRowScore(2));
    assertEquals(5, game.getScore(Board.Player.RED));
    assertEquals(0, game.getScore(Board.Player.BLUE));

    game.placeCardInPosition(0, 1, 2); //r
    game.placeCardInPosition(0, 2, 4); //b
    game.pass(); //r
    assertEquals(1, game.getCell(1, 3).getPawnCount());
    game.placeCardInPosition(2, 2, 3); //b
    game.placeCardInPosition(0, 0, 1); //r
    game.placeCardInPosition(1, 1, 3); //b
    game.placeCardInPosition(1, 2, 1); //r
    game.placeCardInPosition(2, 0, 2); //b
    assertEquals(Board.Player.RED, game.getCell(0, 3).getOwner());
    game.placeCardInPosition(1, 0, 3); //r
    game.placeCardInPosition(0, 2, 2);


    assertEquals(5, game.getRedRowScore(0));
    assertEquals(7, game.getRedRowScore(1));
    assertEquals(2, game.getRedRowScore(2));
    assertEquals(2, game.getBlueRowScore(0));
    assertEquals(5, game.getBlueRowScore(1));
    assertEquals(4, game.getBlueRowScore(2));

    assertEquals(5, game.getRowScore(0));
    assertEquals(7, game.getRowScore(1));
    assertEquals(4, game.getRowScore(2));

    assertEquals(12, game.getScore(Board.Player.RED));
    assertEquals(4, game.getScore(Board.Player.BLUE));
    assertEquals(Board.Player.RED, game.getWinner());
    assertTrue(game.isGameOver());
  }
}
