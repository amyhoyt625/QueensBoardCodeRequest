package cs3500.queensboard;

import cs3500.queensboard.controller.QueensBoardControllerInterface;
import cs3500.queensboard.view.QueensBoardGUIView;

/**
 * A mock implementation of {@link QueensBoardGUIView} used for testing.
 * Logs specific view interactions into a {@link StringBuilder}.
 */
public class ConfirmInputView implements QueensBoardGUIView {
  private final StringBuilder log;

  /**
   * Constructs a ConfirmInputView that logs view method calls to the given StringBuilder.
   *
   * @param log the StringBuilder used to track interactions
   */

  public ConfirmInputView(StringBuilder log) {
    this.log = log;
  }

  @Override
  public void highlightCard(int index) {
    log.append(String.format("highlightCard(%d)\n", index));
  }

  @Override
  public void placeCard(int row, int col, int cardIndex) {
    //this is a stub for placeCard method
  }

  @Override
  public void showGameOver() {
    //stub
  }

  @Override
  public int getSelectedCardIndex() {
    return 0;
  }

  @Override
  public int getSelectedRow() {
    return 0;
  }

  @Override
  public int getSelectedCol() {
    return 0;
  }

  @Override
  public void clearHighlights() {
    //this is a stub for clearHighlights method
  }

  @Override
  public void disableInput() {
    //this is a stub for disableInput method
  }


  @Override
  public void repaint() {
    //this is a stub for repaint method
  }

  @Override
  public void addObserver(QueensBoardControllerInterface obs) {
    //this is a stub for addObserverMethod
  }

  @Override
  public void highlightCell(int row, int col) {
    log.append(String.format("highlightCell(%d, %d)\n", row, col));
  }
}

