package cs3500.queensboard;

import cs3500.queensboard.model.Board;
import cs3500.queensboard.controller.DeckConfig;
import cs3500.queensboard.model.Card;
import cs3500.queensboard.model.Board.Player;
import cs3500.queensboard.view.QueensBoardViewClass;

import java.util.List;
import java.util.Scanner;

/**
 * The entry point for the Queens Board game.
 * This class initializes the game board, loads the deck, and manages the game loop.
 */
public class Main {
  /**
   * The main method that starts the game.
   * It initializes the board, loads the deck configuration,
   * and runs the game loop until completion.
   *
   * @param args command-line arguments (not used in this program)
   */
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    // Create a new Board object with specified width and height (3x5 board)
    int boardWidth = 5;
    int boardHeight = 3;

    if (args.length != 2) {
      System.err.println("Usage: java Main <RedDeckFilePath> <BlueDeckFilePath>");
      return;
    }

    String redDeckPath = args[0];
    String blueDeckPath = args[1];

    // Create a DeckConfig instance to load the deck configuration
    DeckConfig deckConfig = new DeckConfig();
    deckConfig.loadDeck(redDeckPath, blueDeckPath);
    Board gameBoard = new Board(boardHeight, boardWidth, false,
            deckConfig, redDeckPath, blueDeckPath);

    QueensBoardViewClass boardView = new QueensBoardViewClass(gameBoard);

    // Get the red and blue decks from the DeckConfig
    List<Card> redDeck = deckConfig.getRedDeck();
    List<Card> blueDeck = deckConfig.getBlueDeck();

    // Define the hand size (5 cards for each player)
    int handSize = 5;

    // Start the game with the red and blue decks, and hand size
    gameBoard.startGame(redDeck, blueDeck, handSize);

    // Play the game until no cards can be placed on the board
    while (!gameBoard.isGameOver()) {
      //render view
      System.out.println(boardView.toString());
      handlePlayerTurn(gameBoard, scanner);

    }
    System.out.println("Game Over!");
  }

  /**
   * Handles a single player's turn, allowing them to place a card or pass.
   *
   * @param gameBoard the game board
   * @param scanner   the scanner for user input
   */
  private static void handlePlayerTurn(Board gameBoard, Scanner scanner) {
    Player currentPlayer = gameBoard.getTurn();
    System.out.println(currentPlayer + "'s turn!");

    List<Card> hand = gameBoard.getHand();
    if (hand.isEmpty()) {
      System.out.println("No cards left. " + currentPlayer + " passes.");
      gameBoard.pass();
      return;
    }

    boolean validMove = false;
    while (!validMove) {
      try {
        System.out.println("Select a card index (0-" + (hand.size() - 1) + ")" +
                " or enter -1 to pass:");
        int cardIndex = scanner.nextInt();

        if (cardIndex == -1) {
          gameBoard.pass();
          System.out.println(currentPlayer + " passes.");
          validMove = true;
        } else if (cardIndex >= 0 && cardIndex < hand.size()) {
          System.out.println("Enter row and column (e.g., '0 1'):");
          int row = scanner.nextInt();
          int col = scanner.nextInt();

          gameBoard.placeCardInPosition(cardIndex, row, col);
          validMove = true;
        } else {
          System.out.println("Invalid choice! Try again.");
        }
      } catch (IllegalStateException e) {
        System.out.println("Invalid move: " + e.getMessage() + ". Try again.");
        scanner.nextLine(); // Clear invalid input
      } catch (Exception e) {
        System.out.println("Unexpected error: " + e.getMessage() + ". Try again.");
        scanner.nextLine(); // Clear invalid input
      }
    }
  }
}


