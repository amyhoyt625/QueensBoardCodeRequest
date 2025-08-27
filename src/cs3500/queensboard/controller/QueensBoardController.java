package cs3500.queensboard.controller;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import cs3500.queensboard.model.Board;
import cs3500.queensboard.model.Card;
import cs3500.queensboard.model.QueensBoard;
import cs3500.queensboard.player.PlayerActionsInterface;
import cs3500.queensboard.strategy.Move;
import cs3500.queensboard.view.QueensBoardGUIView;

/**
 * The {@code QueensBoardController} class is responsible for handling user input
 * and updating the model and view in response to given input. It acts as an intermediate
 * between the QueensBoardGUIViewClass and the BoardObserver interface.
 * <p>
 *   This controller listens for user actions, highlighting a card or cell, and updating
 *   the view of the game accordingly by refreshing it to represent the current state of the game.
 * </p>
 */
public class QueensBoardController implements QueensBoardControllerInterface,
        PlayerActionsInterface, ModelStatus {
  private QueensBoardGUIView view;
  private QueensBoard model;
  private PlayerActionsInterface player;

  /**
   * this controller listens for user actions, highlighting a card or cell, and updating
   * the view of the game accordingly by refreshing it to represent the current state of the game.
   */
  public QueensBoardController(QueensBoardGUIView view, QueensBoard model,
                               PlayerActionsInterface player) {
    if (view == null) {
      throw new IllegalArgumentException("View cannot be null");
    }
    this.view = view;
    this.model = model;
    this.player = player;
    model.addListener(this);
    this.view.addObserver(this); // Add observer when view is initialized
  }

  /**
   * Setter method for view.
   * @param view queens board view
   */
  public void setView(QueensBoardGUIView view) {
    if (view == null) {
      throw new IllegalArgumentException("View cannot be null");
    }
    this.view = view;
    this.view.addObserver(this); // Ensure observer is added when view is set
  }

  /**
   * Handles the click event for a clicked card.
   * Highlights the selected card by calling the view.
   * @param cardIndex index of the card in a player's hand being selected.
   */
  public void handleCardClick(int cardIndex) {
    try {
      System.out.println("Card clicked: Index " + cardIndex);
      view.highlightCard(cardIndex);
    } catch (IllegalStateException | IllegalArgumentException e) {
      // Show an error message dialog based on the exception type
      JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
    }
  }

  /**
   * Handles the click event for a clicked cell.
   * Highlights the selected cell by calling the view.
   * @param row of the cell being selected
   * @param col of the cell being selected
   */
  public void handleCellClick(int row, int col) {
    try {
      view.highlightCell(row, col);
      System.out.println("Cell clicked: Row " + row + ", Col " + col);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null,
              "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

  }

  /**
   * handles the space key for placing a card.
   * @param cardIndex index of card being placed
   * @param row of the cell being selected
   * @param col of the cell being selected
   */
  public void handlePlaceKey(int cardIndex, int row, int col) {
    try {
      model.placeCardInPosition(cardIndex, row, col);
      view.repaint(); // or repaint GUI, etc.

    } catch (IllegalArgumentException | IllegalStateException e) {
      JOptionPane.showMessageDialog(null,
              "Cannot place card: " + e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
      System.out.println("Cannot place card: " + e.getMessage());
    }
  }


  /**
   * Notifies the user that the action was done.
   * In this case, the player chooses to pass their turn, so it is the other player's turn.
   */
  public void handleTurnPass() {
    System.out.println("Attempting to pass turn. Game state: " + model.isGameOver());
    try {
      model.pass();
      System.out.println("turn passed.");

      // More logic to update view, etc.
    } catch (IllegalStateException e) {
      System.out.println("Error while passing turn: " + e.getMessage());
    }

    view.clearHighlights();
  }

  //handles when the computer makes a move
  @Override
  public void handleComputerMove() {
    System.out.println("Handle computer move.");
    System.out.println("It's Computer's turn!!!!!!!");
    Move move = player.makeMove(model);
    int row = move.getRow();
    int col = move.getCol();
    Card card = move.getCard();
    if (move.isPass()) {
      try {
        model.pass();
        System.out.println("Turn passed.");
      } catch (IllegalStateException e) {
        System.out.println("Error while passing turn: " + e.getMessage());
      }
    }
    else {
      try {
        int cardIndex = model.getHand().indexOf(card);
        model.placeCardInPosition(cardIndex, row, col);
        view.repaint();
      } catch (IllegalArgumentException | IllegalStateException e) {
        System.out.println("Cannot place card: " + e.getMessage());
      }
    }
  }


  /**
   * Notifies the user that the action was done.
   * In this case, the card was selected (highlighted).
   */
  public void cardSelected() {
    System.out.println("Card selected, updating view.");
  }

  /**
   * Notifies the user that the action was done.
   * In this case, the cell was selected (highlighted).
   */
  public void cellSelected() {
    System.out.println("Cell selected, updating view.");
  }

  @Override
  public int getSelectedCardIndex() {
    return view.getSelectedCardIndex();
  }

  @Override
  public int getSelectedRow() {
    return view.getSelectedRow();
  }

  @Override
  public int getSelectedCol() {
    return view.getSelectedCol();
  }

  @Override
  public Move makeMove(QueensBoard board) {
    return player.makeMove(board);
  }

  @Override
  public boolean isComputerTurn() {
    return false;
  }

  @Override
  public Board.Player getPlayerColor() {
    return player.getPlayerColor();
  }

  private void makeComputerMove() {
    Move move = player.makeMove(model);
    System.out.println("MakeComputerMove");
    int row = move.getRow();
    int col = move.getCol();
    Card card = move.getCard();
    int cardIndex = model.getHand().indexOf(card);

    if (move == null) {
      System.out.println("Computer returned null move.");
      return;
    }

    Timer timer = new Timer(1000, e -> {
      if (move != null) {
        try {
          model.placeCardInPosition(cardIndex, row, col);
        } catch (Exception ex) {
          System.out.println("Computer move failed: " + ex.getMessage());
        }
      }
    });
    timer.setRepeats(false);
    timer.start();
  }


  @Override
  public void update() {

    // Debugging: Check what is causing update to be called
    System.out.println("Game over? " + model.isGameOver());
    System.out.println("Current turn: " + model.getTurn() +
            ", Player color/controller: " + player.getPlayerColor());
    System.out.println(player);
    System.out.println("isComputer: " + player.isComputerTurn());

    view.repaint();

    boolean isMyTurn = model.getTurn() == player.getPlayerColor();
    boolean isComputer = player.isComputerTurn();

    //make sure can't interact w/ view if computer
    if (isComputer) {
      view.disableInput();
    }

    if (isMyTurn && isComputer) {
      // Debugging: Before computer makes the move
      System.out.println("Before computer move: ");
      makeComputerMove();

      // Add a delay after the move to simulate a pause
      try {
        Thread.sleep(500); // 500 milliseconds (half a second) delay
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      // Debugging: After computer makes the move
      System.out.println("After computer move: ");
    }


    if (model.isGameOver()) {
      System.out.println("Game over");
      Board.Player winner = model.getWinner(); // assumes your model has a getWinner() method
      int score = model.getScore(winner);      // assumes your model has a getScore(Player) method

      view.showGameOver();

      view.disableInput(); // Optional: disable input after game over
    }
  }


}
