package cs3500.queensboard.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * Class defining the behavior of a card object used in gameplay.
 */
public class QueensCard implements Card {
  private String name;
  private int cost;
  private int value;
  private char[][] influenceGrid;
  private Board.Player influence;

  /**
   * Constructor to create a playable Card that is used during game play.
   *
   * @param name          of the card
   * @param cost          of pawns the card requires to be placed on the baord
   * @param value         the card adds to the score for the player
   * @param influence     owner (player RED or BLUE) of the card
   * @param influenceGrid card's influence on surrounding cells in a 5x5 grid
   */
  public QueensCard(String name, int cost, int value, Board.Player influence,
                    char[][] influenceGrid) {
    if (cost < 1 || cost > 3) {
      throw new IllegalArgumentException("Cost must be between 1 and 3");
    }
    this.name = name;
    this.cost = cost;
    this.value = value;
    this.influenceGrid = influenceGrid;
    this.influence = influence;

  }

  /**
   * Copy constructor for QueensCard.
   *
   * @param other The QueensCard to copy.
   */
  public QueensCard(QueensCard other) {
    this.name = other.name;
    this.cost = other.cost;
    this.value = other.value;
    this.influence = other.influence;

    // Deep copy of the influenceGrid
    this.influenceGrid = new char[other.influenceGrid.length][other.influenceGrid[0].length];
    for (int i = 0; i < other.influenceGrid.length; i++) {
      this.influenceGrid[i] = other.influenceGrid[i].clone(); // Clones each row
    }
  }

  /**
   * Reflects the influence of a card's influence grid for Player BLUE.
   * Mirrors influence of cells across the y-axis for same cards.
   *
   * @param grid InfluenceGrid belonging to a card
   * @return an InfluenceGrid with mirrored influenced cells
   */
  public static char[][] reflectInfluence(char[][] grid) {
    char[][] reflected = new char[grid.length][grid[0].length];
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        reflected[i][j] = grid[i][grid[0].length - 1 - j];
      }
    }
    return reflected;
  }

  /**
   * Returns the value of the Card (how much it adds to the score).
   */
  @Override
  public int getValue() {
    return this.value;
  }

  /**
   * Returns the cost of the Card (how many pawns it costs to place).
   */
  @Override
  public int getCost() {
    return this.cost;
  }

  /**
   * Returns the player that owns the card (RED or BLUE).
   */
  @Override
  public Board.Player getInfluence() {
    return this.influence;
  }

  /**
   * Returns the name of the Card.
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Returns the 5x5 Influence Grid that displays the Card's influence.
   */
  @Override
  public char[][] getInfluenceGrid() {
    return this.influenceGrid;
  }

  /**
   * Renders a card so that ony the owner of the card is known.
   * RED Player -> "R"
   * BLUE Player -> "B"
   *
   * @return the formatted card
   */
  @Override
  public String toString() {
    return (influence == Board.Player.RED ? "R" : "B");
  }

  /**
   * Determines whether two cards are the same.
   *
   * @param obj another Card object
   * @return true if ALL card characteristics are the same
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    QueensCard card = (QueensCard) obj;
    return cost == card.cost &&
            value == card.value &&
            name.equals(card.name) &&
            Arrays.deepEquals(influenceGrid, card.influenceGrid) &&
            influence == card.influence;
  }

  /**
   * Determines the hashcode (place in memory) of the card.
   *
   * @return the hashcode of the card
   */
  @Override
  public int hashCode() {
    return Objects.hash(cost, value, name, Arrays.deepHashCode(influenceGrid), influence);
  }
}