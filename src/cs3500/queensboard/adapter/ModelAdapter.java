package cs3500.queensboard.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cs3500.queensboard.model.Board;
import cs3500.queensboard.model.Cell;
import cs3500.queensboard.model.ReadOnlyQueensBoard;
import cs3500.queensboard.provider.model.BoardPiece;
import cs3500.queensboard.model.Card;
import cs3500.queensboard.provider.model.GameState;
import cs3500.queensboard.provider.model.Player;
import cs3500.queensboard.provider.model.PlayerState;
import cs3500.queensboard.provider.model.ReadOnlyQueensBloodModel;

/**
 * Represents an adapter that allows a ReadOnlyQueensBoard model
 * to be used as a ReadOnlyQueensBloodModel. This is useful for
 * interfacing between different versions or interpretations of the model
 * within the QueensBoard game system.
 */
public class ModelAdapter implements ReadOnlyQueensBloodModel {

  private final ReadOnlyQueensBoard model;

  /**
   * Constructs a ModelAdapter with the given read-only model.
   *
   * @param model the ReadOnlyQueensBoard instance to adapt
   */
  public ModelAdapter(ReadOnlyQueensBoard model) {
    this.model = model;
  }

  @Override
  public int getWidth() {
    return model.getWidth();
  }

  @Override
  public int getHeight() {
    return model.getHeight();
  }

  @Override
  public BoardPiece getItemAt(int row, int col) {
    //try to get the Card
    Card modelCard = model.getCardAt(row, col);

    if (modelCard != null) {
      return new CardtoCardGame(
              modelCard.getName(),
              modelCard.getCost(),
              modelCard.getValue(),
              modelCard.getInfluence(),
              modelCard.getInfluenceGrid()
      );
    }

    return null; // Or return new EmptyPiece() if you want a non-null fallback
  }

  @Override
  public int getRemainingRedDeckSize() {
    return model.getRemainingDeckSize(Board.Player.RED);
  }

  @Override
  public int getRemainingBlueDeckSize() {
    return model.getRemainingDeckSize(Board.Player.BLUE);
  }

  @Override
  public GameState isGameOver() {
    if (model.getWinner().equals(Board.Player.RED)) {
      return GameState.RED_WINS;
    }
    else if (model.getWinner().equals(Board.Player.BLUE)) {
      return GameState.BLUE_WINS;
    }
    else if (model.getWinner().equals(Board.Player.NONE)) {
      return GameState.GAME_ONGOING;
    }
    else {
      return GameState.TIE;
    }
  }

  @Override
  public int redTotalScore() {
    int redTotal = 0;
    for (int r = 0; r < getHeight(); r++) {
      if (model.getRedRowScore(r) > model.getBlueRowScore(r)) {
        redTotal += model.getRedRowScore(r);
      }
    }
    return redTotal;
  }

  @Override
  public int blueTotalScore() {
    int blueTotal = 0;
    for (int r = 0; r < getHeight(); r++) {
      if (model.getBlueRowScore(r) > model.getRedRowScore(r)) {
        blueTotal += model.getRedRowScore(r);
      }
    }
    return blueTotal;
  }

  private List<cs3500.queensboard.provider.model.Card> cardConverter(List<Card> cards) {
    List<cs3500.queensboard.provider.model.Card> adaptedHand = new ArrayList<>();
    for (cs3500.queensboard.model.Card qc : cards) {
      adaptedHand.add(new CardtoCardGame(
              qc.getName(),
              qc.getCost(),
              qc.getValue(),
              qc.getInfluence(),
              qc.getInfluenceGrid()));
    }
    return adaptedHand;
  }

  @Override
  public PlayerState getRedPlayerState() {
    return new PlayerState(Player.RED, cardConverter(model.getDeckConfig().getRedDeck()),
            getPlayerHand(Player.RED).size());
  }

  @Override
  public PlayerState getBluePlayerState() {
    return new PlayerState(Player.BLUE, cardConverter(model.getDeckConfig().getBlueDeck()),
            getPlayerHand(Player.BLUE).size());
  }

  @Override
  public Player turn() {
    Board.Player player = model.getTurn();
    if (player.equals(Board.Player.RED)) {
      return Player.RED;
    }
    else {
      return Player.BLUE;
    }
  }

  @Override
  public List<cs3500.queensboard.provider.model.Card> getPlayerHand(Player player) {
    if (player.equals(Player.RED)) {
      return cardConverter(model.getRedHand());
    } else {
      return cardConverter(model.getBlueHand());
    }
  }


  @Override
  public List<cs3500.queensboard.provider.model.Card> getCurrentPlayerHand() {
    return cardConverter(model.getHand());
  }


  @Override
  public Optional<Player> getOwnerAt(int row, int col) {
    Cell currCell = model.getCell(row, col);
    Board.Player owner = currCell.getOwner();
    if (owner.equals(Board.Player.RED)) {
      return Optional.ofNullable(Player.RED);
    }
    else {
      return Optional.ofNullable(Player.BLUE);
    }
  }

  @Override
  public boolean canPlayCardAt(Player player, int cardIdx, int row, int col) {
    // Check to make sure game was started
    if (!model.isGameOver()) {
      throw new IllegalStateException("Game not started.");
    }

    // Check to make sure cell is a valid location
    if (!model.isValidCell(row, col)) {
      throw new IllegalArgumentException("Invalid board position.");
    }

    // Get the current player's hand
    List<cs3500.queensboard.model.Card> hand = model.getHand();
    Card card = hand.get(cardIdx);

    // Check to make sure player can access that card index in their hand
    if (cardIdx < 0 || cardIdx >= hand.size()) {
      throw new IllegalArgumentException("Invalid card index.");
    }

    // Cannot place a card in a cell that already has a card
    if (model.getCell(row, col).hasCard()) {
      return false;
    } // Cannot place a card that has less pawns than the cost of the card
    else if (model.getCell(row, col).getPawnCount() < card.getCost()) {
      return false;
    } // Cannot place a card that does not have pawns owned by the player
    else if (model.getCell(row,col).getPawnOwner() != card.getInfluence()) {
      return false;
    }
    return true; // else return true
  }

  @Override
  public int getRowScore(Player player, int row) {
    if (player.equals(Player.RED)) {
      return model.getRedRowScore(row);
    }
    else {
      return model.getBlueRowScore(row);
    }
  }
}