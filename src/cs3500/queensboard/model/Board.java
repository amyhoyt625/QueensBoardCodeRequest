package cs3500.queensboard.model;

import java.util.ArrayList;
import java.util.List;
import cs3500.queensboard.controller.DeckConfig;
import cs3500.queensboard.controller.ModelStatus;

/**
 * Represents a playable game board consisting of Cell objects.
 * This class manages the state of the game, including the board layout,
 * players' hands, decks, scores, and turn tracking.
 * Responsibilities of this class include:
 * - Maintaining the dimensions of the board.
 * - Storing the game state, including whether it is active or has ended.
 * - Managing decks and hands for both players (Red and Blue).
 * - Tracking players' scores based on row completion.
 * - Keeping track of whose turn it is and whether the last move was a pass.
 * - Handling deck configurations and potential shuffle mechanics.
 * The board is represented as a 2D array of {@code Cell} objects,
 * where each cell contains information about its occupancy and the game pieces present.
 */
public class Board implements QueensBoard {
  private int widthLength; //cols
  private int heightLength; //rows
  private boolean gameState;
  private DeckConfig deckConfig;
  private List<Card> redDeck;
  private List<Card> blueDeck;
  private List<Card> redHand;
  private List<Card> blueHand;
  private int redRowScore;
  private int blueRowScore;
  private Cell[][] board; // board of cells, 0 indexed
  private Player turn; // Keep track of whose turn it is
  private boolean lastPassRed; // Determines if last move Red made was a pass
  private boolean lastPassBlue;
  private boolean shuffle;
  private String redDeckPath;
  private String blueDeckPath;

  private List<ModelStatus> listeners = new ArrayList<>();

  // Add a listener to the model
  public void addListener(ModelStatus listener) {
    listeners.add(listener);
  }

  // Notify all listeners when the board state changes
  private void notifyListeners() {
    for (ModelStatus listener : listeners) {
      listener.update();
    }
  }


  /**
   * Returns the player (RED or BLUE) who's current turn it is.
   */
  @Override
  public Player getTurn() {
    return this.turn;
  }

  public void setCell(int row, int col, Cell cell) {
    this.board[row][col] = cell;
  }

  /**
   * Represents the possibilities of a Player( RED, BLUE, or NONE).
   */
  public enum Player {
    RED,
    BLUE,
    NONE;

    /**
     * Checks if the player is RED.
     *
     * @return true if the player is RED, false otherwise
     */
    public boolean isRed() {
      return this == RED;
    }

    /**
     * Checks if the player is BLUE.
     *
     * @return true if the player is BLUE, false otherwise
     */
    public boolean isBlue() {
      return this == BLUE;
    }
  }

  /**
   * Constructor to create a board object.
   *
   * @param heightLength number of rows
   * @param widthLength  number of columns
   * @param shuffle      whether deck is shuffled (set to false)
   * @param deckConfig   instance of deck configuration
   * @throws IllegalArgumentException if number of rows is less than 0
   * @throws IllegalArgumentException if number of columns is less than 1 or is even
   * @throws IllegalArgumentException if deck configuration is null
   */
  public Board(int heightLength, int widthLength, boolean shuffle, DeckConfig deckConfig,
               String redDeckPath, String blueDeckPath) {
    if (heightLength <= 0) {
      throw new IllegalArgumentException("Board must have at least one row");
    }
    if (widthLength <= 1 || widthLength % 2 == 0) {
      throw new IllegalArgumentException("Board must have more than one column and be odd");
    }

    if (deckConfig == null) {
      throw new IllegalArgumentException("Board must have a deck config");
    }
    this.heightLength = heightLength;
    this.widthLength = widthLength;
    this.deckConfig = deckConfig;
    this.redDeck = deckConfig.getRedDeck();
    this.blueDeck = deckConfig.getBlueDeck();
    this.shuffle = false;
    initializeGame(redDeckPath, blueDeckPath);
  }

  /**
   * Initializes game state, deck, hand, and board.
   */
  private void initializeGame(String redDeckPath, String blueDeckPath) {
    this.gameState = false;  // The game has not started yet.
    //nobody passed yet
    lastPassRed = false;
    lastPassBlue = false;

    deckConfig = new DeckConfig();  // Initialize the deck configuration.
    deckConfig.loadDeck(redDeckPath, blueDeckPath);  // Load the deck with cards.

    List<Card> redDeck = deckConfig.getRedDeck();  // Get the red deck from deck config.
    List<Card> blueDeck = deckConfig.getBlueDeck();  // Get the blue deck from deck config.

    this.redHand = new ArrayList<>();  // Initialize red player's hand.
    this.blueHand = new ArrayList<>();  // Initialize blue player's hand.

    this.board = new Cell[heightLength][widthLength];
    this.turn = Player.RED;  // Red starts the game.

    // Set up the board:
    // - Fill the first column with red pawns.
    // - Fill the last column with blue pawns.
    // - Fill the rest of the board with 'X' (empty cells).

    // Initialize the first and last columns, and the rest of the board
    for (int row = 0; row < heightLength; row++) {
      for (int col = 0; col < widthLength; col++) {
        if (col == 0) {
          // First column: Red pawns
          this.board[row][col] = new Cell(1, Board.Player.RED);  // Pawn value of 1 for red
        } else if (col == widthLength - 1) {
          // Last column: Blue pawns
          this.board[row][col] = new Cell(1, Board.Player.BLUE);  // Pawn value of 1 for blue
        } else {
          // Fill the rest of the board with empty cells
          this.board[row][col] = new Cell();  // Empty cell
        }
      }
    }
  }

  /**
   * Creates a deep copy of the current Board.
   *
   * @return a new Board object that is an identical copy of this board.
   */
  public Board copy() {
    Board newBoard = new Board(this.heightLength, this.widthLength,
            this.shuffle, this.deckConfig, this.redDeckPath, this.blueDeckPath);

    // Copy game state
    newBoard.gameState = this.gameState;
    newBoard.turn = this.turn;
    newBoard.lastPassRed = this.lastPassRed;
    newBoard.redRowScore = this.redRowScore;
    newBoard.blueRowScore = this.blueRowScore;

    // Deep copy decks and hands
    newBoard.redDeck = new ArrayList<>(this.redDeck);
    newBoard.blueDeck = new ArrayList<>(this.blueDeck);
    newBoard.redHand = new ArrayList<>(this.redHand);
    newBoard.blueHand = new ArrayList<>(this.blueHand);

    // Deep copy board cells
    newBoard.board = new Cell[this.heightLength][this.widthLength];
    for (int i = 0; i < this.heightLength; i++) {
      for (int j = 0; j < this.widthLength; j++) {
        newBoard.board[i][j] = new Cell(this.board[i][j]); // Use the copy constructor
      }
    }

    return newBoard;
  }


  /**
   * Places a card from the hand to a given position on the polygonal board and then
   * draws a card from the deck if able.
   *
   * @param cardIdx index of the card in hand to place (0-index based)
   * @param row     row to place the card in (0-index based)
   * @param col     column to place the card in (0-index based)
   * @throws IllegalStateException    if the game has not started or there is a card at the given
   *                                  position
   * @throws IllegalArgumentException if cardIdx is out of bounds of the hand or
   *                                  row and col do not indicate a position on the polygon
   * @throws IllegalStateException    if the target cell is invalid
   * @throws IllegalStateException    if not enough pawns to place card
   */
  public void placeCardInPosition(int cardIdx, int row, int col) {
    // Debug: Check if game has started and is not finished
    System.out.println("placeCardInPosition called: cardIdx="
            + cardIdx + ", row=" + row + ", col=" + col);

    if (!gameState) {
      throw new IllegalStateException("Game has not started or is already finished.");
    }

    // Debug: Check for invalid board position
    if (row < 0 || row >= heightLength || col < 0 || col >= widthLength) {
      throw new IllegalArgumentException("Invalid board position.");
    }

    // Get the current player's hand and deck
    List<Card> currentHand = (turn == Player.RED) ? redHand : blueHand;
    List<Card> currentDeck = (turn == Player.RED) ? redDeck : blueDeck;

    // Validate card index
    if (cardIdx < 0 || cardIdx >= currentHand.size()) {
      throw new IllegalArgumentException("Invalid card index.");
    }

    Card card = currentHand.get(cardIdx);

    if (card == null) {
      throw new IllegalStateException("Cannot place a null card.");
    }

    Cell targetCell = board[row][col];
    // Debug: Log target cell
    System.out.println("Target cell at (" + row + ", " + col + "): " + targetCell);

    if (targetCell == null) {
      throw new IllegalArgumentException("Target cell is not valid.");
    }

    if (targetCell.hasCard()) {
      throw new IllegalStateException("Cannot place a card on cell that already has a card.");
    }

    if (targetCell.getPawnCount() < card.getCost()) {
      throw new IllegalStateException("Not enough pawns to place this card.");
    }

    if (targetCell.isEmpty()) {
      throw new IllegalStateException("Target cell is empty.");
    }

    if (targetCell.getOwner() != turn) {
      throw new IllegalStateException("Don't own this pawn or it's not your turn.");
    }

    // If the cell already has pawns, retain them and add the card to the cell
    targetCell = new Cell(card, 0, turn); // Create new Cell with card and existing pawns
    board[row][col] = targetCell;  // Update the board with the new cell
    currentHand.remove(cardIdx);

    applyInfluence(card, row, col);

    if (!currentDeck.isEmpty() && currentHand.size() < deckConfig.getMaxHandSize()) {
      currentHand.add(currentDeck.remove(0));
    }

    //TODO need this?
    //if red just placed a card set pass to false
    if (turn == Player.RED) {
      lastPassRed = false;
    }
    if (turn == Player.BLUE) {
      lastPassBlue = false;
    }

    // Switch turn
    System.out.println("Before switching turn: " +  turn);
    turn = (turn == Player.RED) ? Player.BLUE : Player.RED;
    System.out.println("After switching turn: " + turn);



    notifyListeners();
  }


  /**
   * Apply the influence grid to the cells influenced by the placed card.
   *
   * @param card    that is being placed on the game board.
   * @param cardRow of cell that is influenced
   * @param cardCol of cell that is influenced
   */
  public void applyInfluence(Card card, int cardRow, int cardCol) {
    char[][] influenceGrid = card.getInfluenceGrid(); // Get the influence grid of the card
    int gridSize = influenceGrid.length;
    int halfSize = gridSize / 2; // Determines the center of the grid
    Board.Player infl = card.getInfluence();

    for (int i = 0; i < gridSize; i++) {
      for (int j = 0; j < gridSize; j++) {
        //cant be either X or C, has to be I
        if (influenceGrid[i][j] != 'X' && influenceGrid[i][j] != 'C') {
          int targetRow = cardRow + (i - halfSize);
          int targetCol = cardCol + (j - halfSize);

          if (isValidCell(targetRow, targetCol)) { // Ensure within bounds
            Cell targetCell = getCell(targetRow, targetCol);

            if (targetCell.hasCard()) {
              continue; // Influence does nothing if there is a card
            } else if (targetCell.isEmpty()) {
              targetCell.addPawn(card, 1); // Add a pawn if the cell is empty
            } else {
              if (targetCell.getPawnOwner().equals(infl)) {
                // Increase pawn count, capped at 3
                targetCell.addPawn(card, Math.min(3 - targetCell.getPawnCount(), 1));
              } else {
                // Change ownership
                targetCell.changeOwnership();
              }
            }
          }
        }
      }
    }
  }

  /**
   * Return the cell at a specific coordinate.
   *
   * @param row position of the cell
   * @param col position of the cell
   * @return a cell object at that position
   */
  public Cell getCell(int row, int col) {
    if (isValidCell(row, col)) {
      return board[row][col];
    }
    return null; // Return null if the cell is out of bounds
  }

  /**
   * Determine if the cell at a specific coordinate is a valid instance of a cell.
   *
   * @param row position of the cell
   * @param col position of the cell
   * @return true if the cell at that position is valid
   */
  public Boolean isValidCell(int row, int col) {
    return row >= 0 && row < board.length && col >= 0 && col < board[0].length;
  }


  /**
   * handle passing instead of placing card.
   */
  private void nextTurn() {
    turn = (turn == Player.RED) ? Player.BLUE : Player.RED;
    resetPassStates();
    notifyListeners();
  }

  private void resetPassStates() {
    lastPassRed = false;
    lastPassBlue = false;
  }

  /**
   * handle passing instead of placing card.
   */
  @Override
  public void pass() {
    // Check if the game has started and is still in progress
    if (!gameState) {
      throw new IllegalStateException("Game has not started or is already finished.");
    }

    // Check if the current player passed their turn
    if (turn == Player.RED) {
      lastPassRed = true; // Red passes
    }
    else if (turn == Player.BLUE) {
      lastPassBlue = true; // Blue passes
    }

    // Check if both players have passed consecutively
    if (lastPassRed && lastPassBlue) {
      gameState = false; // End the game if both players pass consecutively
      System.out.println("Both players passed consecutively. Game over!");
    }

    // Switch turn if only one player passes
    turn = (turn == Player.RED) ? Player.BLUE : Player.RED;

    // Notify listeners that the game state has changed
    notifyListeners();
  }


  /**
   * Return the amount of emptySpace are available on the gameboard for gameplay.
   */
  public int getEmptySpaces() {
    int emptySpaces = 0;
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[0].length; j++) {
        if (board[i][j].isEmpty()) {
          emptySpaces++;
        }
      }
    }
    return emptySpaces;
  }

  /**
   * Starts the game with the given deck and hand size. If shuffle is set to true,
   * then the deck is shuffled prior to dealing the hand.
   *
   * <p>Note that modifying the deck given here outside this method should have no effect
   * on the game itself.
   *
   * @param redDeck  list of red cards to play the game with
   * @param blueDeck list of blue cards to play the game with
   * @param handSize maximum hand size for the game
   * @throws IllegalStateException    if the game has already been started
   * @throws IllegalArgumentException if the deck is null or contains a null object,
   *                                  if handSize is not positive (i.e. 0 or less),
   *                                  or if the deck does not contain enough cards to fill the board
   *                                  AND fill a starting hand
   */
  @Override
  public void startGame(List<Card> redDeck, List<Card> blueDeck, int handSize) {
    ///should start as player red
    this.turn = Player.RED;

    if (gameState) {
      throw new IllegalStateException("Game already started");
    }

    this.gameState = true;

    if (redDeck == null || blueDeck == null || handSize <= 0
            || handSize > deckConfig.getMaxHandSize()) {
      throw new IllegalArgumentException("Invalid deck or hand size");
    }

    for (Card card : redDeck) {
      if (card == null) {
        throw new IllegalArgumentException("Deck contains a null card.");
      }
    }

    // Ensure no nulls in blueDeck
    for (Card card : blueDeck) {
      if (card == null) {
        throw new IllegalArgumentException("Deck contains a null card.");
      }
    }

    // Ensure there are enough cards
    int requiredCards = (widthLength * heightLength);
    if (redDeck.size() < requiredCards || blueDeck.size() < requiredCards) {
      throw new IllegalArgumentException("Decks do not contain enough " +
              "cards to fill the board");
    }

    if (redDeck.size() < handSize || blueDeck.size() < handSize) {
      throw new IllegalArgumentException("Not enough cards in the deck to deal hands");
    }

    // Deal the red hand, no shuffling
    this.redHand = new ArrayList<>(); //this is empty
    for (int i = 0; i < handSize; i++) {
      this.redHand.add(this.redDeck.remove(0));
    }

    // Deal the blue hand, no shuffling
    this.blueHand = new ArrayList<>();
    for (int i = 0; i < handSize; i++) {
      this.blueHand.add(this.blueDeck.remove(0));
    }

    this.lastPassRed = false;
  }

  /**
   * Retrieve the number of cards that make up the width of the rectangle
   * that contains the polygon. (e.g. the number of columns in the widest row)
   *
   * @return the width of the board
   */
  @Override
  public int getWidth() {
    return widthLength;
  }

  /**
   * Retrieve the number of cards that make up the height of the rectangle
   * that contains the polygon. (e.g. the number of rows in the highest column)
   *
   * @return the height of the board
   */
  @Override
  public int getHeight() {
    return heightLength;
  }

  /**
   * Returns the card in the indicated position on the board. If there is no card on the board
   * and the position is valid, the method will return null.
   *
   * @param row the row to access
   * @param col the column to access
   * @return the card in the valid position or null if the position has no card
   * @throws IllegalArgumentException if the row and column are not a valid location
   *                                  for a card in the polygonal board
   */
  @Override
  public Card getCardAt(int row, int col) {
    if (row < 0 || row > heightLength || col < 0 || col > widthLength) {
      throw new IllegalArgumentException("Given row and column is invalid.");
    }
    if (board[row][col].card != null) {
      return board[row][col].card; // Return card at that position
    } else {
      return null; // no card is found at that position
    }
  }

  /**
   * Determines the total score for the given player.
   *
   * @param player the player that wants their score
   * @return the total score of the given player
   */
  @Override
  public int getScore(Player player) {
    int score = 0;  // Initialize a local score for the specified player
    int redCurrScore = 0;
    int blueCurrScore = 0;

    //check if red>blue
    // go throught each row, see whose score is greater and then add that
    // to said color's temp score
    for (int row = 0; row < heightLength; row++) {
      if (getRowWinner(row) == Player.RED) {
        redCurrScore += getRedRowScore(row);
      } else if (getRowWinner(row) == Player.BLUE) {
        blueCurrScore += getBlueRowScore(row);
      }
    }

    if (player.equals(Player.RED)) {
      return redCurrScore;
    } else if (player.equals(Player.BLUE)) {
      return blueCurrScore;
    } else {
      return score;
    }

  }

  //gets winner of a row
  private Board.Player getRowWinner(int row) {
    if (getRedRowScore(row) > getBlueRowScore(row)) {
      return Player.RED;
    } else if (getBlueRowScore(row) > getRedRowScore(row)) {
      return Player.BLUE;
    } else {
      return Player.NONE;
    }
  }


  /**
   * Return the score of the row based on who has the higher overall row score.
   *
   * @param row row number that score is needed for
   * @return row score
   */
  public int getRowScore(int row) {
    redRowScore = 0;
    blueRowScore = 0; // Reset at method start

    // Go through the row and adding up for each color to get each color's score for said row
    for (int col = 0; col < widthLength; col++) {
      if (board[row][col].hasCard()) {
        // Accumulate cell scores based on players
        if (board[row][col].getOwner() == Player.RED) {
          redRowScore += board[row][col].getCellScore();
        } else {
          blueRowScore += board[row][col].getCellScore();
        }
      }
    }
    // Determine which one is larger, reset the other to 0 -- no points
    if (redRowScore == blueRowScore) {
      return 0;
    } else if (redRowScore > blueRowScore) {
      return redRowScore;
    } else {
      return blueRowScore; // Return only the larger of the 2
    }
  }

  /**
   * Return the score of red for the given row.
   *
   * @param row row number that score is needed for
   * @return Red's row score
   */
  @Override
  public int getRedRowScore(int row) {
    getRowScore(row);
    return redRowScore;
  }

  /**
   * Return the score of blue for the given row.
   *
   * @param row row number that score is needed for
   * @return Blue's row score
   */
  @Override
  public int getBlueRowScore(int row) {
    getRowScore(row);
    return blueRowScore;
  }

  /**
   * Return the deckConfiguration for a file of cards.
   *
   * @return a deckConfiguration instance
   */
  @Override
  public DeckConfig getDeckConfig() {
    return this.deckConfig;
  }

  /**
   * Returns a copy of the player's current hand. If their hand is empty, then an empty
   * list is returned.
   *
   * @return a copy of the player's current hand
   * @throws IllegalStateException if the game has not started
   */
  @Override
  public List<Card> getHand() {
    if (!gameState) {
      throw new IllegalStateException("Game has not started");
    }

    if (this.turn == Player.RED) {
      return redHand;
    } else {
      return blueHand;
    }
  }

  /**
   * Returns a copy of the red player's current hand. If their hand is empty, then an empty
   * list is returned.
   *
   * @return a copy of the player's current hand
   * @throws IllegalStateException if the game has not started
   */
  @Override
  public List<Card> getRedHand() {
    if (!gameState) {
      throw new IllegalStateException("Game has not started");
    }
    return redHand;
  }

  /**
   * Returns a copy of the blue player's current hand. If their hand is empty, then an empty
   * list is returned.
   *
   * @return a copy of the player's current hand
   * @throws IllegalStateException if the game has not started
   */
  @Override
  public List<Card> getBlueHand() {
    if (!gameState) {
      throw new IllegalStateException("Game has not started");
    }
    return blueHand;
  }

  /**
   * Determine if the deck should be shuffled.
   * For now, this is always FALSE
   *
   * @return if deck is shuffled
   */
  @Override
  public boolean getShuffle() {
    return this.shuffle;
  }

  /**
   * Returns the number of cards left in the deck being used during the game.
   *
   * @param player the player that wants their remaining deck size
   * @return the number of cards left in the deck used in game
   * @throws IllegalStateException if the game has not started
   */
  @Override
  public int getRemainingDeckSize(Player player) {
    if (player == Player.RED) {
      return redDeck.size();
    } else {
      return blueDeck.size();
    }
  }

  /**
   * Returns the winner of the ENTIRE game, based on the winner of each row.
   */
  @Override
  public Board.Player getWinner() {
    if (getScore(Player.RED) > getScore(Player.BLUE)) {
      return Player.RED;
    } else if (getScore(Player.BLUE) > getScore(Player.RED)) {
      return Player.BLUE;
    } else {
      return Player.NONE;
    }
  }

  /**
   * Returns true if the game is over. The implementation must
   * describe what it means for the game to be over.
   *
   * @return true if the game is over, false otherwise
   * @throws IllegalStateException if the game has not started
   */
  @Override
  public boolean isGameOver() {
    boolean gameOver = false;
    if (!gameState || getEmptySpaces() == 0) {
      gameOver = true;
    }
    return gameOver;
  }



}

