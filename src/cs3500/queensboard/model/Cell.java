package cs3500.queensboard.model;

/**
 * Class defining the behavior of a cell object used in game play.
 */
public class Cell implements CellInterface {
  protected Card card;
  private int pawnValue;
  private Board.Player owner;
  private boolean isEmpty;

  /**
   * Constructor to create a usable Cell that is used to create a playable gameboard.
   * Creates a Cell with a pawn.
   *
   * @param pawnValue number of pawns in the cell
   * @param owner     owner of the pawns in the cell
   */
  public Cell(int pawnValue, Board.Player owner) {
    if (pawnValue < 0 || pawnValue > 3) {
      throw new IllegalArgumentException("invalid pawn amount");
    }
    this.card = null;
    this.pawnValue = pawnValue;
    this.owner = owner;
    this.isEmpty = false;
  }

  /**
   * Constructor to create a usable Cell that is used to create a playable gameboard.
   * Creates a Cell with a Card.
   *
   * @param card      card placed in the cell
   * @param pawnValue always 0 because a cell with a card cannot have pawns
   * @param owner     owner of the card in the cell
   */
  public Cell(Card card, int pawnValue, Board.Player owner) {
    if (pawnValue < 0 || pawnValue > 3) {  //!=0?
      throw new IllegalArgumentException("invalid pawn amount");
    }
    this.card = card;
    this.pawnValue = 0;
    this.owner = owner;
    this.isEmpty = false;
  }

  /**
   * Constructor to create a usable Cell that is used to create a playable gameboard.
   * Creates a Cell that is empty.
   */
  public Cell() {
    this.card = null;
    this.pawnValue = 0;
    this.owner = Board.Player.NONE;
    this.isEmpty = true;
  }

  /**
   * Copy constructor for creating a deep copy of a Cell.
   *
   * @param other The Cell to copy.
   */
  public Cell(Cell other) {
    if (other == null) {
      throw new IllegalArgumentException("Cannot copy a null Cell");
    }
    if (other.card != null) {
      this.card = new QueensCard((QueensCard) other.card); // Create a deep copy of the card
    } else {
      this.card = null; // Ensure null is preserved if the original card is null
    }
    this.pawnValue = other.pawnValue;
    this.owner = other.owner;
    this.isEmpty = other.isEmpty;
  }


  /**
   * Returns the score of the cell (value of card placed).
   */
  @Override
  public int getCellScore() {
    if (hasCard()) {
      return this.card.getValue(); // Value of card
    } else {
      return 0; // does not have a card (no value)
    }
  }

  /**
   * Returns the player (RED or BLUE) that owns the cell (w/ Pawns or cards).
   */
  @Override
  public Board.Player getOwner() {
    return this.owner;
  }

  /**
   * Returns the number of pawns in a cell.
   */
  @Override
  public int getPawnCount() {
    return this.pawnValue;
  }

  /**
   * Returns whether a cell contains a card.
   */
  @Override
  public boolean hasCard() {
    return this.card != null;
  }

  /**
   * Returns whether a cell is empty (no card or pawns).
   */
  @Override
  public boolean isEmpty() {
    return (this.card == null && this.pawnValue == 0 && this.owner == Board.Player.NONE);
  }

  /**
   * Adds the given amount of pawns to a cell.
   *
   * @param card owner must match owner of pawns
   * @param i    number of pawns being added
   * @throws IllegalArgumentException if the cell has a card
   * @throws IllegalArgumentException if the cell already has 3 pawns
   */
  @Override
  public void addPawn(Card card, int i) {
    this.owner = card.getInfluence();
    if (this.hasCard()) {
      throw new IllegalArgumentException("Cannot add pawn to a cell with a card.");
    }
    if (this.getPawnCount() >= 3) {
      throw new IllegalArgumentException("Cannot add more pawns to current cell");
    } else {
      this.pawnValue += i;
    }
  }

  /**
   * Returns the owner (RED or BLUE) of the pawns in a cell.
   */
  @Override
  public Board.Player getPawnOwner() {
    return this.owner;
  }

  /**
   * Changes the owner of a cell to the other player.
   */
  @Override
  public void changeOwnership() {
    if (this.owner == Board.Player.RED) {
      this.owner = Board.Player.BLUE;
    } else if (this.owner == Board.Player.BLUE) {
      this.owner = Board.Player.RED;
    }
  }

  /**
   * Return the card at the cell.
   *
   * @throws IllegalArgumentException if the cell does not contain a card
   */
  @Override
  public Card getCard() {
    if (this.hasCard()) {
      return this.card;
    } else {
      throw new IllegalArgumentException("Cannot get card from a cell without a card.");
    }
  }

  /**
   * Format the string based on the element in the cell.
   */
  @Override
  public String toString() {
    if (isEmpty()) {
      return "_"; // Empty cell
    } else if (hasCard()) {
      return this.card.toString(); // Call toString method on Card to convert
    } else {
      return String.valueOf(getPawnCount()); // Pawn Count of Cell
    }
  }
}
