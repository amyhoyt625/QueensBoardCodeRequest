package cs3500.queensboard;

import org.junit.Before;
import org.junit.Test;

import cs3500.queensboard.controller.QueensBoardController;
import cs3500.queensboard.controller.QueensBoardControllerInterface;
import cs3500.queensboard.model.Board;
import cs3500.queensboard.model.QueensBoard;
import cs3500.queensboard.player.PlayerActionsInterface;
import cs3500.queensboard.strategy.FillFirstStrategy;
import cs3500.queensboard.strategy.Move;
import cs3500.queensboard.strategy.Strategy;
import cs3500.queensboard.view.QueensBoardGUIView;
import static org.junit.Assert.assertEquals;

/**
 * A test class for the QueensBoard controller, ensuring methods of the controller work in specific
 * game instances.
 * This class isolates the controller by using mock objects to simulate specific scenarios of the
 * game, including handling player and computer inputs, as well as clicking cards and cells.
 */
public class ControllerTest {
  private final PlayerActionsInterface playerHuman = new PlayerActionsInterface() {
    @Override
    public Move makeMove(QueensBoard board) {
      return null;
    }

    @Override
    public boolean isComputerTurn() {
      return false;
    }

    @Override
    public Board.Player getPlayerColor() {
      return Board.Player.RED;
    }
  };
  private final PlayerActionsInterface playerComp = new PlayerActionsInterface() {
    @Override
    public Move makeMove(QueensBoard board) {
      Strategy strat = new FillFirstStrategy();
      return strat.chooseMove(board);
    }

    @Override
    public boolean isComputerTurn() {
      return true;
    }

    @Override
    public Board.Player getPlayerColor() {
      return Board.Player.BLUE;
    }
  };
  private final PlayerActionsInterface playerCompPass = new PlayerActionsInterface() {
    @Override
    public Move makeMove(QueensBoard board) {
      Move move = new Move(true);
      return move;
    }

    @Override
    public boolean isComputerTurn() {
      return true;
    }

    @Override
    public Board.Player getPlayerColor() {
      return Board.Player.BLUE;
    }
  };
  private StringBuilder log;
  private QueensBoardControllerInterface controller;
  private QueensBoardControllerInterface controller2;
  private QueensBoardControllerInterface controllerPass;

  @Before
  public void setup() {
    log = new StringBuilder();
    ConfirmInputModel model = new ConfirmInputModel(log);
    QueensBoardGUIView view = new ConfirmInputView(log);
    controller = new QueensBoardController(view, model, playerHuman);
    controller2 = new QueensBoardController(view, model, playerComp);
    controllerPass = new QueensBoardController(view, model, playerCompPass);
  }

  @Test
  public void testHandlePlaceKey() {
    controller.handlePlaceKey(0, 0, 0);
    assertEquals("placeCardInPosition(0, 0, 0)\n", log.toString());
  }

  @Test
  public void testHandleCardClick() {
    controller.handleCardClick(0);
    assertEquals("highlightCard(0)\n", log.toString());
  }

  @Test
  public void testHandleCellClick() {
    controller.handleCellClick(2, 2);
    assertEquals("highlightCell(2, 2)\n", log.toString());
  }

  @Test
  public void testHandleTurnPass() {
    controller.handleTurnPass();
    assertEquals("pass()\n", log.toString());
  }

  @Test
  public void testHandleComputerMove() {
    controller2.handleComputerMove();
    assertEquals("placeCardInPosition(0, 0, 4)\n", log.toString());
  }

  @Test
  public void testHandleCompPass() {
    controllerPass.handleComputerMove();
    assertEquals("pass()\n", log.toString());
  }

}
