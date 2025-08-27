package cs3500.queensboard.provider;

import cs3500.queensboard.provider.controller.PlayerControllerTraditional;
import cs3500.queensboard.provider.model.Card;
import cs3500.queensboard.provider.model.Player;
import cs3500.queensboard.provider.model.QueensBloodModel;
import cs3500.queensboard.provider.model.QueensBloodTraditional;
import cs3500.queensboard.provider.model.ReadOnlyQueensBloodModelImpl;
import cs3500.queensboard.provider.model.Reader;
import cs3500.queensboard.provider.users.GameUser;
import cs3500.queensboard.provider.users.HumanPlayer;
import cs3500.queensboard.provider.view.QueensBloodViewGUITraditional;
import java.io.IOException;
import java.util.List;

/**
 * Main class for launching a two-player QueensBlood game using the GUI.
 */
public final class PawnsBoardGame {

  /**
   * Main method and entry point for the game.
   */
  public static void main(String[] args) {
    try {
      List<Card> redDeck = Reader.readDeck("docs/RedDeck.config", Player.RED);
      List<Card> blueDeck = Reader.readDeck("docs/BlueDeck.config", Player.BLUE);

      System.out.println("Red Deck: " + redDeck.size());

      QueensBloodModel model = new QueensBloodTraditional(5, 7);

      // Start the game with valid deck, board dimensions, and hand size
      model.startGame(5, 7, redDeck, blueDeck, 5);

      ReadOnlyQueensBloodModelImpl readOnlyModel = new ReadOnlyQueensBloodModelImpl(model);

      // Views for each player (use same model instance)
      QueensBloodViewGUITraditional viewRed =
              new QueensBloodViewGUITraditional(readOnlyModel, Player.RED);
      QueensBloodViewGUITraditional viewBlue =
              new QueensBloodViewGUITraditional(readOnlyModel, Player.BLUE);

      GameUser userRed = new HumanPlayer(readOnlyModel);
      GameUser userBlue = new HumanPlayer(readOnlyModel);
      // Or new ComputerPlayer(new FillFirstStrategy());

      // Controllers for each player
      PlayerControllerTraditional controllerRed =
              new PlayerControllerTraditional(model, viewRed, Player.RED, userRed);
      PlayerControllerTraditional controllerBlue =
              new PlayerControllerTraditional(model, viewBlue, Player.BLUE, userBlue);

      viewRed.setVisible(true);
      viewBlue.setVisible(true);

      controllerRed.playGame();
      controllerBlue.playGame();

    } catch (IOException e) {
      System.err.println("Failed to load deck: " + e.getMessage());
    }
  }
}
