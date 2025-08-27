package cs3500.queensboard.provider.model;

import java.util.List;
import java.util.Optional;

/**
 * ReadOnlyQueensBloodModelImpl is the implementation of the interface ReadOnlyQueensBloodModel
 * that will enable client to access only the read-only portion of the interface, without giving
 * it mutation power. This class will follow the Adapter pattern since there already exists a
 * full-featured QueensBloodModel, but the intention is to expose only read-only methods. The
 * adapter, ReadOnlyQueensBloodModelImpl, will act as a wrapper that uses the main model,
 * QueensBloodModel, and delegates the implementation of the read-only methods to it, hiding the
 * rest.
 */
public class ReadOnlyQueensBloodModelImpl implements ReadOnlyQueensBloodModel {
  QueensBloodModel model;

  /**
   * Constructor for ReadOnlyQueensBloodModelImpl that takes in a QueensBloodModel for the method
   * implementations.
   *
   * @param model represents the QueensBloodModel given for the method implementations
   */
  public ReadOnlyQueensBloodModelImpl(QueensBloodModel model) {
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
    return model.getItemAt(row, col);
  }

  @Override
  public int getRemainingRedDeckSize() {
    return model.getRemainingRedDeckSize();
  }

  @Override
  public int getRemainingBlueDeckSize() {
    return model.getRemainingBlueDeckSize();
  }

  @Override
  public GameState isGameOver() {
    return model.isGameOver();
  }

  @Override
  public int redTotalScore() {
    return model.redTotalScore();
  }

  @Override
  public int blueTotalScore() {
    return model.blueTotalScore();
  }

  @Override
  public PlayerState getRedPlayerState() {
    return model.getRedPlayerState();
  }

  @Override
  public PlayerState getBluePlayerState() {
    return model.getBluePlayerState();
  }

  @Override
  public Player turn() {
    return model.turn();
  }

  @Override
  public List<Card> getPlayerHand(Player player) {
    return model.getPlayerHand(player);
  }

  @Override
  public List<Card> getCurrentPlayerHand() {
    return model.getCurrentPlayerHand();
  }

  @Override
  public Optional<Player> getOwnerAt(int row, int col) {
    return model.getOwnerAt(row, col);
  }

  @Override
  public boolean canPlayCardAt(Player player, int cardIdx, int row, int col) {
    return model.canPlayCardAt(player, cardIdx, row, col);
  }

  @Override
  public int getRowScore(Player player, int row) {
    return model.getRowScore(player, row);
  }
}
