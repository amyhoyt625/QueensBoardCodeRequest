package cs3500.queensboard.adapter;

import java.util.List;

import cs3500.queensboard.controller.QueensBoardControllerInterface;
import cs3500.queensboard.provider.controller.ViewListener;
import cs3500.queensboard.provider.model.Card;
import cs3500.queensboard.provider.model.Player;
import cs3500.queensboard.provider.model.ReadOnlyQueensBloodModel;
import cs3500.queensboard.provider.view.QueensBloodViewGUITraditional;
import cs3500.queensboard.view.QueensBoardGUIView;

/**
 * Represents an adapter that bridges the traditional GUI implementation of the QueensBlood view
 * with the expected QueensBoardGUIView interface. This allows the game to reuse or integrate
 * a traditional GUI while maintaining compatibility with the updated GUI abstraction.
 * This adapter extends QueensBloodViewGUITraditional and implements QueensBoardGUIView.
 */
public class ViewAdapter extends QueensBloodViewGUITraditional implements QueensBoardGUIView {

  private ReadOnlyQueensBloodModel model;

  /**
   * Constructs the GUI and initializes the visuals of the game within the given model.
   *
   * @param model  read-only model of the QueensBlood board game.
   * @param player player from QueensBlood
   * @throws IllegalArgumentException If model or player given are null.
   */
  public ViewAdapter(ReadOnlyQueensBloodModel model, Player player) {
    super(model, player);
  }

  @Override
  public void clearHighlights() {
    super.clearHighlights();
  }

  @Override
  public void disableInput() {
    //they don't have implemention
  }

  //TODO check if this works
  @Override
  public void addObserver(QueensBoardControllerInterface obs) {
    // Create a ViewListenerAdapter that wraps your controller
    ViewListener adapter = new ViewListenerAdapter(obs);
    // Add the adapter to the provider's view
    this.addListener(adapter);
  }

  @Override
  public void highlightCell(int row, int col) {
    super.highlightCell(row, col);
  }

  @Override
  public void highlightCard(int cardIndex) {
    super.highlightCard(cardIndex);
  }

  //cardgame --> card interface
  @Override
  public void placeCard(int row, int col, int cardIndex) {
    List<Card> providerHand = this.model.getCurrentPlayerHand();
    Card cardAtPosition = providerHand.get(cardIndex);
    this.placeCard(cardAtPosition, row, col);
  }

  @Override
  public void showGameOver() {
    super.showGameOver("Game Over");
  }

  @Override
  public int getSelectedCardIndex() {
    try {
      if (model == null) {
        return -1; // Safe default if model is null
      }

      List<Card> providerHand = this.model.getCurrentPlayerHand();
      if (providerHand == null) {
        return -1; // Safe default if hand is null
      }

      Card card = getSelectedCard();
      if (card == null) {
        return -1; // Safe default if selected card is null
      }

      int index = providerHand.indexOf(card);
      return index;
    } catch (Exception e) {
      System.out.println("Error getting selected card index: " + e.getMessage());
      return -1; // Safe default on any exception
    }
  }

  @Override
  public int getSelectedRow() {
    //the provider code doesn't properly analyze the view to get these values
    // their view doesn't display proper placement thus affecting this method
    //making us leave as a stub
    //return super.getSelectedRow();
    return 0;
  }

  @Override
  public int getSelectedCol() {
    //the provider code doesn't properly analyze the view to get these values
    //their view doesn't display proper placement thus affecting this method
    //making us leave as a stub
    //return super.getSelectedCol();
    return 0;
  }

}