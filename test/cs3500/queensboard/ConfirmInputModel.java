package cs3500.queensboard;

import java.util.List;

import cs3500.queensboard.controller.DeckConfig;
import cs3500.queensboard.controller.ModelStatus;
import cs3500.queensboard.model.Board;
import cs3500.queensboard.model.Card;
import cs3500.queensboard.model.Cell;
import cs3500.queensboard.model.QueensBoard;
import cs3500.queensboard.model.QueensCard;

/**
 * A mock implementation of the {@link QueensBoard} interface that logs method calls
 * for the purpose of testing controller behavior without requiring a full game model.
 */
public class ConfirmInputModel implements QueensBoard {
  private final StringBuilder log;

  /**
   * Constructs a ConfirmInputModel that logs actions to the provided StringBuilder.
   *
   * @param log the StringBuilder used to record method calls.
   */
  public ConfirmInputModel(StringBuilder log) {
    this.log = log;
  }

  @Override
  public void placeCardInPosition(int cardIndex, int row, int col) {
    log.append(String.format("placeCardInPosition(%d, %d, %d)\n", cardIndex, row, col));
  }

  @Override
  public void pass() {
    log.append("pass()\n");
  }

  @Override
  public void startGame(List<Card> redDeck, List<Card> blueDeck, int handSize) {
    //this is a stub for starting the game
  }

  @Override
  public void applyInfluence(Card card, int row, int col) {
    // this is a stub for applying influence
  }


  @Override
  public int getWidth() {
    return 5;
  }

  @Override
  public int getHeight() {
    return 5;
  }

  @Override
  public Card getCardAt(int row, int col) {
    return null;
  }

  @Override
  public List<Card> getHand() {
    return List.of(
            new QueensCard("TestCard", 1, 2, Board.Player.BLUE, new char[5][5]));
  }

  @Override
  public int getScore(Board.Player player) {
    return 0;
  }

  @Override
  public int getRemainingDeckSize(Board.Player player) {
    return 0;
  }

  @Override
  public Board.Player getWinner() {
    return null;
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public Board.Player getTurn() {
    return null;
  }

  @Override
  public int getEmptySpaces() {
    return 0;
  }

  @Override
  public int getRowScore(int row) {
    return 0;
  }

  @Override
  public int getRedRowScore(int row) {
    return 0;
  }

  @Override
  public int getBlueRowScore(int row) {
    return 0;
  }

  @Override
  public DeckConfig getDeckConfig() {
    return null;
  }

  @Override
  public boolean getShuffle() {
    return false;
  }

  @Override
  public Cell getCell(int row, int col) {
    // Only the last column is valid
    if (col == 4) {
      return new Cell(1, Board.Player.BLUE); // 1 pawn, BLUE owner, no card
    }
    return new Cell();
  }

  @Override
  public Boolean isValidCell(int row, int col) {
    return null;
  }

  @Override
  public Board copy() {
    return null;
  }

  @Override
  public List<Card> getRedHand() {
    return null;
  }

  @Override
  public List<Card> getBlueHand() {
    return null;
  }

  @Override
  public void addListener(ModelStatus listener) {
    //this is a stub for adding a listener
  }
}
