package cs3500.queensboard.provider.view;

import cs3500.queensboard.provider.controller.ViewListener;
import cs3500.queensboard.provider.model.Card;

/**
 * Represents the view interface responsible for displaying the game
 * to the user and enabling interaction. The view communicates with the controller
 * by publishing events, as clicks and key presses, through the ViewListener interface.
 */
public interface QueensBloodViewGUI {

  /**
   * Gets the listener that manages the handling of user input events from this view.
   * The listener is the main communication path between the view and the controller.
   *
   * @return the listener that handles user inputs.
   */
  ViewListener getListener();

  /**
   * Registers a listener that will handle player actions, as card selection and move confirmation.
   * The listener will notify the controller of changes to the view
   *
   * @param listener the listener implementing the ViewListener interface
   */
  void addListener(ViewListener listener);

  /**
   * Visually highlights the selected card in the view.
   *
   * @param cardIdx the index of the selected card
   */
  void highlightCard(int cardIdx);

  /**
   * Visually highlights the selected board cell in the view.
   *
   * @param row the row index of the selected cell
   * @param col the column index of the selected cell
   */
  void highlightCell(int row, int col);

  /**
   * Clears all current card and cell highlights in the view.
   */
  void clearHighlights();

  /**
   * Displays an error message to the player, typically due to an invalid action or move.
   *
   * @param message the error message to display
   */
  void showError(String message);

  /**
   * Displays the final result of the game to the player.
   *
   * @param message a message describing the game's outcome (winner or tie)
   */
  void showGameOver(String message);

  /**
   * Initializes and displays the view for interaction at the beginning of the game.
   */
  void showGame();

  /**
   * Highlights whether it's currently this player's turn in the view.
   *
   * @param isActive true if it's the player's turn; false otherwise
   */
  void setTurnHighlight(boolean isActive);

  /**
   * Gets the selected card that is clicked on.
   *
   * @return the selected card clicked on if any.
   */
  Card getSelectedCard();

  /**
   * Sets the card object selected when it is clicked.
   *
   * @param card the card selected from the player's hand.
   */
  void selectCard(Card card);

  void updateRowScores();

  void placeCard(Card card, int row, int col);

}
