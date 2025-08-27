package cs3500.queensboard.provider.controller;

import cs3500.queensboard.provider.model.GameState;
import cs3500.queensboard.provider.model.ModelListener;
import cs3500.queensboard.provider.model.Player;
import cs3500.queensboard.provider.model.QueensBloodModel;
import cs3500.queensboard.provider.strategy.Move;
import cs3500.queensboard.provider.users.GameUser;
import cs3500.queensboard.provider.view.QueensBloodViewGUI;
import java.util.Optional;

/**
 * Represents a controller that manages a single player's interaction with the QueensBlood game.
 * It listens for player inputs (via a view or strategy), ensures actions are valid,
 * updates the model, and reacts to model events such as turn changes or game over.
 * This controller supports both human and machine players.`
 */
public class PlayerControllerTraditional implements ViewListener, ModelListener, PlayerController {

  private final QueensBloodModel model;
  private final QueensBloodViewGUI view;
  private final Player player;
  private final GameUser user;
  private boolean isTurn;
  private Optional<Integer> selectedCardIdx = Optional.empty();
  private Optional<int[]> selectedCell = Optional.empty();

  /**
   * Constructs a controller for a specific player in the QueensBlood game.
   * The controller connects the given model, view, and user logic either human or computer,
   * and registers itself as a listener to both model and view events.
   *
   * @param model  the shared game model
   * @param view   the view for this player, GUI interface
   * @param player the player this controller manages (RED or BLUE)
   * @param user   the GameUser representing either a human or strategy-based player
   * @throws IllegalArgumentException if any argument is null
   */
  public PlayerControllerTraditional(QueensBloodModel model,
                                     QueensBloodViewGUI view,
                                     Player player,
                                     GameUser user) {
    if (model == null || view == null || player == null || user == null) {
      throw new IllegalArgumentException("None of the controller arguments can be null.");
    }

    this.model = model;
    this.view = view;
    this.player = player;
    this.user = user;
    this.isTurn = false;

    this.view.addListener(this);
    this.model.addModelListener(this);
  }

  @Override
  public void playGame() {
    if (model.turn() == player) {
      turnStarted(player);
    }
  }

  @Override
  public void turnStarted(Player currentTurnPlayer) {
    isTurn = currentTurnPlayer == this.player;
    view.setTurnHighlight(isTurn);

    if (!isTurn) {
      return;
    }

    // Get potential move to play, if there is no move, meaning it is empty, then player is human
    Optional<Move> moveOpt = user.getMove(model, player);

    if (moveOpt.isPresent()) { //player is computer
      Move move = moveOpt.get();
      try {
        model.placeCardInPosition(move.getCardIndex(), move.getRow(), move.getCol());
      } catch (Exception e) {
        // This shouldn't happen since valid moves are guaranteed by the strategy
        view.showError("Unexpected error placing strategy move: " + e.getMessage());
        model.passCard();
      }
    } else {
      // For human player just wait for interaction
      view.showGame(); // and re-show interface
    }
  }

  @Override
  public void selectCard(int cardIdx, Player player) {
    if (!isTurn || this.player != player) {
      return;
    }
    selectedCardIdx = Optional.of(cardIdx);
    view.highlightCard(cardIdx);
  }

  @Override
  public void selectCell(int row, int col) {
    if (!isTurn) {
      return;
    }
    selectedCell = Optional.of(new int[]{row, col});
    view.highlightCell(row, col);
  }

  @Override
  public void confirmMove() {
    if (!isTurn) {
      view.showError("It's not your turn.");
      return;
    }
    if (selectedCardIdx.isEmpty() || selectedCell.isEmpty()) {
      view.showError("Select both a card and a cell before confirming.");
      return;
    }

    int cardIdx = selectedCardIdx.get();
    int row = selectedCell.get()[0];
    int col = selectedCell.get()[1];

    try {
      model.placeCardInPosition(cardIdx, row, col);
      clearSelections();
      view.updateRowScores();
      view.placeCard(model.getPlayerHand(player).get(cardIdx), row, col);
    } catch (Exception e) {
      view.showError("Invalid move: " + e.getMessage());
    }
  }

  @Override
  public void passTurn() {
    if (!isTurn) {
      view.showError("You can't pass — it's not your turn.");
      return;
    }
    clearSelections();
    model.passCard();
  }

  /**
   * Clears the selected card and cell, and removes highlights from the view.
   * Called after confirming a move or passing the turn.
   */
  private void clearSelections() {
    selectedCardIdx = Optional.empty();
    selectedCell = Optional.empty();
    view.clearHighlights();
  }

  @Override
  public void gameOver(GameState state) {
    view.showGameOver(state.toString());
  }
}
