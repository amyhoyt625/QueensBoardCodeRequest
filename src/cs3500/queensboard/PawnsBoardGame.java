package cs3500.queensboard;

import cs3500.queensboard.controller.QueensBoardController;
import cs3500.queensboard.model.Board;
import cs3500.queensboard.controller.DeckConfig;
import cs3500.queensboard.model.Card;
import cs3500.queensboard.model.Board.Player;
import cs3500.queensboard.player.ComputerPlayer;
import cs3500.queensboard.player.HumanPlayer;
import cs3500.queensboard.player.PlayerActionsInterface;
import cs3500.queensboard.strategy.FillFirstStrategy;
import cs3500.queensboard.strategy.MaxRowStrategy;
import cs3500.queensboard.view.QueensBoardGUIViewClass;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.util.List;

/**
 * The entry point for the Queens Board game with GUI.
 * This class initializes the game board, loads the deck, and manages the game loop.
 */
public final class PawnsBoardGame {
  /**
   * The main method that starts the game for GUI.
   * It initializes the board, loads the deck configuration,
   * and runs the game loop until completion.
   *
   * @param args command-line arguments (not used in this program)
   */
  public static void main(String[] args) {
    System.out.println("Welcome to Queensboard!");
    System.out.println("Usage: java PawnsBoardGame <RedDeckFilePath> " +
            "<BlueDeckFilePath> <Player1Type> <Player2Type>");
    System.out.println("Player types: 'human', 'fillfirststrategy', " +
            "or 'maxrowstrategy'");

    if (args.length != 4) {
      System.err.println("Usage: java PawnsBoardGame <RedDeckFilePath> " +
              "<BlueDeckFilePath> <Player1Type> <Player2Type>");
      return;
    }


    String redDeckPath = args[0];
    String blueDeckPath = args[1];
    String playerType1 = args[2].toLowerCase();
    String playerType2 = args[3].toLowerCase();

    int boardWidth = 7;
    int boardHeight = 5;

    DeckConfig deckConfig = new DeckConfig();
    deckConfig.loadDeck(redDeckPath, blueDeckPath);

    Board gameBoard = new Board(boardHeight, boardWidth, false, deckConfig,
            redDeckPath, blueDeckPath);

    // Load decks and start game
    List<Card> redDeck = deckConfig.getRedDeck();
    List<Card> blueDeck = deckConfig.getBlueDeck();
    int handSize = 5;
    gameBoard.startGame(redDeck, blueDeck, handSize);

    // Create GUI frames for both players
    SwingUtilities.invokeLater(() -> {
      PlayerActionsInterface player1 = createPlayer1(playerType1);
      PlayerActionsInterface player2 = createPlayer2(playerType2);
      System.out.println("Player 1: " + player1);
      System.out.println("Player 2: " + player2);


      // Red player view and controller
      QueensBoardGUIViewClass redBoardView = new QueensBoardGUIViewClass(gameBoard, Player.RED);
      QueensBoardController redController = new QueensBoardController(redBoardView,
              gameBoard, player1);
      redBoardView.addObserver(redController);

      redController.update();

      JFrame redFrame = new JFrame("Red Player - Queens Board Game");
      redFrame.add(redBoardView);
      redFrame.pack();
      redFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      redFrame.setVisible(true);
      redBoardView.repaint();

      // Blue player view and controller
      QueensBoardGUIViewClass blueBoardView = new QueensBoardGUIViewClass(gameBoard, Player.BLUE);
      QueensBoardController blueController = new QueensBoardController(blueBoardView,
              gameBoard, player2);
      blueBoardView.addObserver(blueController);

      blueController.update();

      gameBoard.addListener(redController);
      gameBoard.addListener(blueController);

      JFrame blueFrame = new JFrame("Blue Player - Queens Board Game");
      blueFrame.add(blueBoardView);
      blueFrame.pack();
      blueFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      blueFrame.setVisible(true);


    });
  }

  /**
   * Creates a player strategy based on the given type.
   */
  private static PlayerActionsInterface createPlayer1(String playerType) {
    PlayerActionsInterface player1 = new HumanPlayer();
    if (playerType.equals("human")) {
      player1 = new HumanPlayer();
    } else if (playerType.equals("fillfirststrategy")) {
      player1 = new ComputerPlayer(new FillFirstStrategy(), Board.Player.RED);
    } else if (playerType.equals("maxrowstrategy")) {
      player1 = new ComputerPlayer(new MaxRowStrategy(), Board.Player.RED);
    } else {
      System.err.println("Invalid player type: " + playerType + ". Defaulting to human.");
    }
    return player1;
  }

  private static PlayerActionsInterface createPlayer2(String playerType) {
    PlayerActionsInterface player1 = new HumanPlayer();
    if (playerType.equals("human")) {
      player1 = new HumanPlayer();
    } else if (playerType.equals("fillfirststrategy")) {
      player1 = new ComputerPlayer(new FillFirstStrategy(), Board.Player.BLUE);
    } else if (playerType.equals("maxrowstrategy")) {
      player1 = new ComputerPlayer(new MaxRowStrategy(), Board.Player.BLUE);
    } else {
      System.err.println("Invalid player type: " + playerType + ". Defaulting to human.");
    }
    return player1;
  }
}


//if computer player is args 2 (aka red) then call redController.handleComputerPlayer