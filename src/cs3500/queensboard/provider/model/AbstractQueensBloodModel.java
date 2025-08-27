package cs3500.queensboard.provider.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This is the abstract representation of the QueenBloodModel. This class will base
 * extended models as a general model of game functionalities and logic.
 * This class manages player actions, row and total scoring for each player,
 * state of the board, and tracks the turn of the player. The origin is located at position
 * (0, 0) which is the top-left corner of the game board. The first index will be the
 * row, which increases downwards and the second index will be the column, which increases
 * rightward. For example (1,2), is more to the right then (1,1) and (2,2) is below (1,2).
 */
public abstract class AbstractQueensBloodModel implements QueensBloodModel<Card> {
  private final List<ModelListener> listeners = new ArrayList<>();
  protected BoardPiece[][] board;
  private int row;
  private int col;
  private boolean gameStarted;
  private Player turn = Player.RED;
  private int consecutivePasses;
  private PlayerState redPlayer;
  private PlayerState bluePlayer;
  private GameState gameState;

  //------------------------------------------------------------------------------------------
  //                                  MUTATOR METHODS
  //------------------------------------------------------------------------------------------

  /**
   * Constructs the abstract version of the QueensBloodModel which represents a general features
   * of a version of the Queens Blood game. Checks if both the column number or row number is less
   * than 1, because anything smaller would not allow to represent a proper width by height board.
   *
   * @param row the row number of the board.
   * @param col the column number of the board.
   * @throws IllegalArgumentException if col or row are less than 1.
   */
  protected AbstractQueensBloodModel(int row, int col) {
    if (col < 1 || row < 1) {
      throw new IllegalArgumentException("Board row and col must be greater than 0");
    }

    this.row = row;
    this.col = col;
    this.gameStarted = false;
    this.consecutivePasses = 0;
  }

  @Override
  public void placeCardInPosition(int cardIdx, int row, int col) {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    if (row >= this.row || col >= this.col) {
      throw new IllegalArgumentException("Invalid position, row and col cannot be greater than " +
              "the board size");
    }
    if (row < 0 || col < 0) {
      throw new IllegalArgumentException("row and col cannot be smaller than 0");
    }
    if (cardIdx < 0 || cardIdx >= redPlayer.getHand().size()) {
      throw new IllegalArgumentException("cardIdx cannot be smaller " +
              "than 0 and smaller than hand size");
    }
    if (turn == Player.RED) {
      if (!anyCardSatisfiesConditions(redPlayer)) {
        throw new IllegalStateException("Player has to pass since none of the cells with their " +
                "pawns are greater than the card's cost in hand or none of the cells are empty");
      }
      executeCardPlacement(redPlayer, cardIdx, row, col);
    } else if (turn == Player.BLUE) {
      if (!anyCardSatisfiesConditions(bluePlayer)) {
        throw new IllegalStateException("Player has to pass since none of the cells with their " +
                "pawns are greater than the card's cost in hand or none of the cells are empty");
      }
      executeCardPlacement(bluePlayer, cardIdx, row, col);

    }
    //after executing the card's placement, then need to switch the player's turn
    switchTurn();

    //notify model listener
    if (isGameOver() != GameState.GAME_ONGOING) {
      notifyGameOver(isGameOver());
    } else {
      notifyTurnChange(turn());
    }
  }

  /**
   * Executes the placement of a card on the board. If the target cell is empty,
   * the card is placed directly. Otherwise, the existing piece determines the interaction.
   * The player's hand is updated, and the card's influence is applied.
   *
   * @param currentPlayer the player placing the card.
   * @param cardIdx       the index of the card in the player's hand.
   * @param row           the 0-based row position for placement.
   * @param col           the 0-based column position for placement.
   */
  private void executeCardPlacement(PlayerState currentPlayer, int cardIdx, int row, int col) {
    consecutivePasses = 0; //Reset since the move was made

    Card card = currentPlayer.getHand().get(cardIdx);
    BoardPiece piece = board[row][col];

    //Hence, the BoardPiece interface enforces that all the implemented classes add a
    //cardInteraction method, which will determine how each class Pawn or CardGame or BoardPosition
    //will behave with attempt of placing card through dynamic dispatch.
    piece.cardInteraction(card, board, row, col, currentPlayer);
    //The influenceImpact method Will only be reached if it is a pawn, since, CardGame
    //implementation of cardInteraction method will throw an exception.

    currentPlayer.removeCardFromHand(cardIdx);
    currentPlayer.drawCard();
    influenceImpact(card, row, col);
  }


  /**
   * This method is abstract establishes the blueprint of a how card
   * will show the amount of influence there has been impacted once it is placed
   * on a certain valid position on the board.
   *
   * @param card the card that is influencing.
   * @param row  the row position the card will influence.
   * @param col  the column position the card will influence.
   */
  @Override
  public abstract void influenceImpact(Card card, int row, int col);

  @Override
  public void passCard() {
    consecutivePasses += 1;

    if (consecutivePasses >= 2) {
      isGameOver();
      return;
    }

    //Then, switch turn
    switchTurn();

    //notify model listener
    if (isGameOver() != GameState.GAME_ONGOING) {
      notifyGameOver(isGameOver());
    } else {
      notifyTurnChange(turn());
    }
  }

  /**
   * Helper method that will switch the turn of the current player to the next. Players will switch
   * their turn when they either have to pass or they place a card in the board.
   */
  private void switchTurn() {
    turn = (turn == Player.RED) ? Player.BLUE : Player.RED;
  }

  /**
   * Checks if any card in the hand can be placed on the board. Iterates through each card that is
   * in the current hand to see if the conditions allow it to be placed onto the board. If the
   * piece can be placed, meaning cell is not null and card can be placed the method will return
   * true.
   *
   * @return true if it can, false if conditions are not met.
   */
  private boolean anyCardSatisfiesConditions(PlayerState currentPlayer) {
    for (Card card : currentPlayer.getHand()) {
      for (int row = 0; row < this.row; row++) {
        for (int col = 0; col < this.col; col++) {
          BoardPiece piece = board[row][col];

          //if piece is null or piece can be placed, meaning that it is a
          //pawn that has a cost that is greater or equal to the number of pawns
          // which should then return true
          if (piece.canPlaceCard(card, currentPlayer.getPlayer())) {
            return true;
          }
        }
      }
    }
    //If nothing has being returned yet, then player needs to
    passCard();
    return false;
  }

  @Override
  public void startGame(int row, int col, List<Card> deckRedPlayer,
                        List<Card> deckBluePlayer, int handSize) {
    if (gameStarted) {
      throw new IllegalStateException("Game has already started");
    }
    if (row < 1) {
      throw new IllegalArgumentException("Row cannot be less than 1");
    }
    //if it is less than 1 or even than throw error
    if (col < 1 || col % 2 == 0) {
      throw new IllegalArgumentException("Col cannot be less than 1 and needs to be odd");
    }
    if (deckRedPlayer.size() != deckBluePlayer.size()) {
      throw new IllegalArgumentException("Red player's deck size is not equal to blue's");
    }
    if (handSize < 1) {
      throw new IllegalArgumentException("HandSize cannot be less than 1");
    }
    //need to cast 1 to double so that division evaluates to 0.3333 and not integer division, 0.
    if (handSize > (((double) 1 / 3) * deckRedPlayer.size())) {
      throw new IllegalArgumentException("HandSize can't be greater than 1/3 of deck size");
    }
    if ((row * col) > deckRedPlayer.size()) {
      throw new IllegalArgumentException("Both decks needs to contain enough cards to" +
              "fill the board and fill a starting hand");
    }
    this.row = row;
    this.col = col;
    //when initializing both PlayerStates, the class PlayerState constructor will deal with the
    //creation of their respective hands.
    this.redPlayer = new PlayerState(Player.RED, deckRedPlayer, handSize);
    this.bluePlayer = new PlayerState(Player.BLUE, deckBluePlayer, handSize);
    this.gameStarted = true;
    this.gameState = GameState.GAME_ONGOING;

    boardInitialization();
  }

  /**
   * Initializes the game board. Creates a 2D array of board pieces where each of the cells
   * are empty. The leftmost column, column 0 starts with one Red pawn in each row, while the
   * rightmost column (column index of given col - 1) starts with one Blue pawn in each row.
   */
  private void boardInitialization() {
    this.board = new BoardPiece[row][col];
    for (int row = 0; row < this.row; row++) {
      for (int col = 0; col < this.col; col++) {
        board[row][col] = new BoardPosition();
      }
    }

    for (int r = 0; r < this.row; r++) {
      board[r][0] = new Pawn(Player.RED); //left most includes one red pawn each
      board[r][this.col - 1] = new Pawn(Player.BLUE); //rightmost includes one blue pawn each
    }
  }

  @Override
  public QueensBloodModel copy() {
    AbstractQueensBloodModel modelCopy = new QueensBloodTraditional(this.row, this.col);

    modelCopy.gameStarted = this.gameStarted;
    modelCopy.turn = this.turn;
    modelCopy.consecutivePasses = this.consecutivePasses;
    modelCopy.gameState = this.gameState;

    // Deep copy of the board
    BoardPiece[][] newBoard = new BoardPiece[this.row][this.col];
    for (int r = 0; r < this.row; r++) {
      for (int c = 0; c < this.col; c++) {
        if (this.board[r][c] != null) {
          newBoard[r][c] = this.board[r][c].copy();
        }
      }
    }
    modelCopy.board = newBoard;

    modelCopy.redPlayer = this.redPlayer.copy();
    modelCopy.bluePlayer = this.bluePlayer.copy();

    return modelCopy;
  }

  // ------------------------------------------------------------------------------------------
  //                                  OBSERVER METHODS
  // ------------------------------------------------------------------------------------------

  @Override
  public int getWidth() {
    return col;
  }

  @Override
  public int getHeight() {
    return row;
  }

  @Override
  public BoardPiece getItemAt(int row, int col) {
    checkValidBordersAndGameStart(row, col);
    return board[row][col];
  }

  @Override
  public int getRemainingRedDeckSize() {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    return redPlayer.getDeck().size();
  }


  @Override
  public int getRemainingBlueDeckSize() {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    return bluePlayer.getDeck().size();
  }


  @Override
  public GameState isGameOver() {
    if (consecutivePasses >= 2) {
      int redScore = redTotalScore();
      int blueScore = blueTotalScore();

      if (redScore > blueScore) {
        gameState = GameState.RED_WINS;
      } else if (blueScore > redScore) {
        gameState = GameState.BLUE_WINS;
      } else {
        gameState = GameState.TIE;
      }
    }

    return gameState;
  }

  @Override
  public int redTotalScore() {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    return calculateTotalScore(Player.RED);
  }

  @Override
  public int blueTotalScore() {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    return calculateTotalScore(Player.BLUE);
  }

  /**
   * Helper method to calculate the total score for the given player.The method will
   * abstract the behavior of what should be repeated in both redTotalScore() and blueTotalScore()
   * for code reusability. The method iterates through each row, sums up the values of all cards
   * belonging to the given player and the opponent, then adds the row score only if the player's
   * row score is greater than the opponent's.
   *
   * @param player represents the player whose score is being calculated, can be RED or BLUE.
   * @return The total score for the specified player.
   * @throws IllegalStateException if the game has not started.
   */
  private int calculateTotalScore(Player player) {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    }

    int totalScore = 0;

    for (int r = 0; r < row; r++) {
      int playerRowScore = 0;
      int opponentRowScore = 0;

      for (int c = 0; c < col; c++) {
        BoardPiece piece = board[r][c];

        if (piece != null) {
          if (piece.getOwner() == player) {
            playerRowScore += piece.getValue();
          } else {
            opponentRowScore += piece.getValue();
          }
        }
      }
      //Only add the row score that is if the current player's row score is greater than the
      //opponent's row score.
      if (playerRowScore > opponentRowScore) {
        totalScore += playerRowScore;
      }
    }
    return totalScore;
  }

  @Override
  public PlayerState getRedPlayerState() {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    return redPlayer;
  }

  @Override
  public PlayerState getBluePlayerState() {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    return bluePlayer;
  }

  @Override
  public Player turn() {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    return turn;
  }

  @Override
  public List<Card> getPlayerHand(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    }

    if (player == Player.RED) {
      return redPlayer.getHand();
    } else {
      return bluePlayer.getHand();
    }
  }

  @Override
  public List<Card> getCurrentPlayerHand() {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    if (turn == Player.RED) {
      return redPlayer.getHand();
    } else {
      return bluePlayer.getHand();
    }
  }

  @Override
  public Optional<Player> getOwnerAt(int row, int col) {
    checkValidBordersAndGameStart(row, col);

    BoardPiece piece = getItemAt(row, col);

    if (piece == null) {
      return Optional.empty();
    } else {
      return Optional.ofNullable(piece.getOwner());
    }
  }

  @Override
  public boolean canPlayCardAt(Player player, int cardIdx, int row, int col) {
    checkValidBordersAndGameStart(row, col);

    PlayerState currentPlayer = (player == Player.RED) ? redPlayer : bluePlayer;
    List<Card> hand = currentPlayer.getHand();

    if (cardIdx < 0 || cardIdx >= hand.size()) {
      throw new IllegalArgumentException("Invalid card index");
    }

    Card card = hand.get(cardIdx);
    BoardPiece piece = board[row][col];

    //A placement can only be legal if the piece is a pawn owner by the same player as the one
    //given, and it has enough pawns to cover cost. Cannot place a card where there already is one
    //Double dispatch will deal with that through the canPlaceCard method in BoardPiece.
    return piece.canPlaceCard(card, player);
  }

  /**
   * Validates that the given row and column coordinates are within the bounds of the board
   * and that the game has already started. Throws exceptions if any of the checks fail.
   *
   * @param row the row index to validate
   * @param col the column index to validate
   * @throws IllegalArgumentException if row or col are negative, or exceed the board's size
   * @throws IllegalStateException    if the game has not started
   */
  private void checkValidBordersAndGameStart(int row, int col) {
    if (row >= this.row || col >= this.col) {
      throw new IllegalArgumentException("Invalid position, row and col cannot be greater than " +
              "the board size");
    }
    if (row < 0 || col < 0) {
      throw new IllegalArgumentException("row and col cannot be smaller than 0");
    }
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    }
  }

  @Override
  public int getRowScore(Player player, int row) {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    if (row < 0 || row >= this.row) {
      throw new IllegalArgumentException("Invalid row index");
    }

    int score = 0;
    for (int col = 0; col < this.col; col++) {
      BoardPiece piece = board[row][col];

      if (piece != null && piece.getOwner() == player) {
        score += piece.getValue();
      }
    }
    return score;
  }

  @Override
  public void addModelListener(ModelListener listener) {
    if (listener == null) {
      throw new IllegalArgumentException("Listener cannot be null.");
    }
    listeners.add(listener);
  }


  /**
   * Notifies all registered {@link ModelListener} instances that the current player's turn
   * has started, meaning that the turn has changed. This should be called whenever the active
   * player changes and the game is not yet over.
   *
   * @param currentPlayer the player whose turn has just begun
   */
  private void notifyTurnChange(Player currentPlayer) {
    for (ModelListener l : listeners) {
      l.turnStarted(currentPlayer);
    }
  }

  /**
   * Notifies all registered {@link ModelListener} instances that the game has ended.
   * This should be called once the game is determined to be over, after a card placement
   * or a pass action.
   *
   * @param result the final {@link GameState} of the game, indicating the winner or a tie
   */
  private void notifyGameOver(GameState result) {
    for (ModelListener l : listeners) {
      l.gameOver(result);
    }
  }


}
