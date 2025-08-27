package cs3500.queensboard.provider.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents the Card implementation for QueensBlood game, defining its attributes
 * such as name, cost, value, and influence grid. The implementation will enforce
 * validation rules for proper card creation.
 */
public class CardGame implements Card {
  private final String name;
  private final int cost;
  private final int value;
  private final char[][] influenceGrid;
  private final Player owner;

  /**
   * Constructor for the CardGame class. Ensures that all parameters follow the specifications
   * for creating a valid card for the QueensBloodGame.
   *
   * @param name          represents the name of the card
   * @param cost          represents how much it will cost to add the card, which is directly
   *                      related to
   *                      the number of pawns on the cell.
   * @param value         represents the value that adding that card to board will contribute
   *                      to the score
   *                      of player.
   * @param influenceGrid represents a 5x5 grid with the influence the card will have on the
   *                      cells around it
   * @throws IllegalArgumentException if the name is empty,
   *                                  if the cost is negative, smaller than 0 or bigger than 3
   *                                  pawns,
   *                                  if the value is negative, smaller than 0,
   *                                  if the influence grid row is not 5,
   *                                  if the influence grid col is not 5
   * @throws NullPointerException     if the name, cost, value or influenceGrid are null
   */
  public CardGame(String name, int cost, int value, char[][] influenceGrid, Player owner) {
    if (name.isEmpty()) {
      throw new IllegalArgumentException("Name cannot be empty");
    }
    if (cost < 1 || cost > 3) {
      throw new IllegalArgumentException("Cost needs to be between 1 and 3");
    }
    if (value < 0) {
      throw new IllegalArgumentException("Value cannot be negative");
    }
    if (influenceGrid.length != 5) {
      throw new IllegalArgumentException("Influence grid cannot be null and should have 5 rows");
    }
    for (char[] row : influenceGrid) {
      if (row.length != 5) {
        throw new IllegalArgumentException("Influence grid should have 5 columns");
      }
    }
    this.name = Objects.requireNonNull(name, "Name cannot be null");
    //Since int and char are primitive types it cannot be null, therefore, do not need to
    //check if any of the values on cost, value or on the influence grid are null.
    this.cost = cost;
    this.value = value;
    this.influenceGrid = Objects.requireNonNull(influenceGrid,
            "Influence grid cannot be null");
    this.owner = Objects.requireNonNull(owner, "Owner cannot be null");
  }

  /**
   * Private constructor used for creating duplicates.
   *
   * @param other the CardGame instance that is copied.
   */
  private CardGame(CardGame other) {
    this.name = other.name;
    this.cost = other.cost;
    this.value = other.value;
    this.owner = other.owner;

    //Then, copy the 5x5 influence grid
    char[][] gridCopy = new char[5][5];
    for (int i = 0; i < 5; i++) {
      System.arraycopy(other.influenceGrid[i], 0, gridCopy[i], 0, 5);
    }
    this.influenceGrid = gridCopy;
  }

  @Override
  public void cardInteraction(Card card, BoardPiece[][] board, int row, int col,
                              PlayerState playerState) {
    throw new IllegalStateException("Cell already contains a card");
  }

  @Override
  public boolean canPlaceCard(Card card, Player player) {
    return false; //Not an attempt to interact so return false since a card cannot be
    // replaced with another card
  }

  @Override
  public Player getOwner() {
    return owner;
  }

  // Card has no reaction to an external interaction.
  @Override
  public void handleInfluenceInteraction(Player owner) {
    //NOTHING SHOULD happen since a card cannot be influenced. The idea is that the throwing an
    //exception here will not work because our influenceImpact() method on model will check through
    //the board cells to compare to attempt an influence impact. Since the method needs to check
    //every cell with "I", that should be influenced, some of these cells might contain cards,
    //therefore, potentially leading to a loop of thrown IllegalStateExceptions.
  }

  @Override
  public void switchOwner() {
    //NOTHING SHOULD happen since a card cannot switch an owner. Once a card has been placed onto
    //the board the owner will remain the same during the duration of the entire rest of the game.
    //Throwing an exception in this case is unnecessary. This is because switchOwner() is an
    //universal method that is applied to pawn and card elements. Since the influenceImpact() method
    //on the model will check through the board cells and could attempt to change ownership of
    //elements that are influenced by a card, having this method do nothing makes sure that the
    //logic of game will stay the same and be consistent, preventing any unwanted and needed errors
    //when faced with a card.
  }

  @Override
  public int getDisplayValue() {
    return getValue();
  }

  @Override
  public String toString() {
    return owner == Player.RED ? "R" : "B";
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CardGame)) {
      return false;
    }
    CardGame other = (CardGame) obj;
    for (int row = 0; row < 5; row++) {
      for (int col = 0; col < 5; col++) {
        if (this.influenceGrid[row][col] != other.influenceGrid[row][col]) {
          return false;
        }
      }
    }
    return this.name.equals(other.name) && this.value == other.value && this.cost == other.cost;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, cost, value, Arrays.deepHashCode(influenceGrid));
  }

  @Override
  public int getCost() {
    return cost;
  }

  @Override
  public int getValue() {
    return value;
  }

  @Override
  public char[][] getInfluenceGrid() {
    return influenceGrid;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public BoardPiece copy() {
    return new CardGame(this);
  }

}