package cs3500.queensboard.provider;

import cs3500.queensboard.provider.model.Card;
import cs3500.queensboard.provider.model.GameState;
import cs3500.queensboard.provider.model.Player;
import cs3500.queensboard.provider.model.QueensBloodTraditional;
import cs3500.queensboard.provider.model.Reader;
import cs3500.queensboard.provider.strategy.CompositeStrategy;
import cs3500.queensboard.provider.strategy.FillFirstStrategy;
import cs3500.queensboard.provider.strategy.MaximizeRowScoreStrategy;
import cs3500.queensboard.provider.strategy.Strategy;
import cs3500.queensboard.provider.view.QueensBloodView;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Creates game by reading the file configuration.
 */
public class PawnsBoard {

  /**
   * This main method is the entry point for the QueensBlood game. It starts by initializing the
   * model of the game, loads in the cards that are used for the deck in the configuration file,
   * and starts the game to play.
   *
   * @param args command line-arg.
   */
  public static void main(String[] args) {
    try {
      int rows = 3;
      int columns = 5;
      int handSize = 5;

      String filePath = "docs/deck.config";
      List<Card> redDeck = Reader.readDeck(filePath, Player.RED); //reads once for red
      List<Card> blueDeck = Reader.readDeck(filePath, Player.BLUE); //reads once for blue

      QueensBloodTraditional model = new QueensBloodTraditional(rows, columns);
      model.startGame(rows, columns, redDeck, blueDeck, handSize);

      QueensBloodView view = new QueensBloodView(model);

      // Composite strategies
      Strategy redStrategy = new CompositeStrategy(List.of(new MaximizeRowScoreStrategy(),
              new FillFirstStrategy()));
      Strategy blueStrategy = new CompositeStrategy(List.of(new FillFirstStrategy(),
              new MaximizeRowScoreStrategy()));

      System.out.println("-----------------------Game has started!-----------------------\n");

      System.out.println(view);
      while (model.isGameOver() == GameState.GAME_ONGOING) {
        System.out.println("Current turn: " + model.turn());
        Player currentPlayer = model.turn();
        Strategy strategy = (currentPlayer == Player.RED) ? redStrategy : blueStrategy;

        strategy.chooseMove(model, currentPlayer).ifPresentOrElse(move -> {
          try {
            model.placeCardInPosition(move.getCardIndex(), move.getRow(), move.getCol());
            System.out.println(view);
          } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("Failed to apply move: " + e.getMessage());
          }
        }, () -> {
            System.out.println(currentPlayer + " has no valid move, passing turn.");
            model.passCard();
          });

      }

      System.out.println("Game Over!");
      System.out.println(model.isGameOver());
      System.out.println(view);

    } catch (FileNotFoundException e) {
      System.out.println("Error: Deck configuration file not found.");
    } catch (IOException e) {
      System.out.println("Error: Could not read deck." + e.getMessage());
    }
  }
}
