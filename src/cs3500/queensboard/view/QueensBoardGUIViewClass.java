package cs3500.queensboard.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Dimension;
import java.awt.BasicStroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import cs3500.queensboard.controller.QueensBoardControllerInterface;
import cs3500.queensboard.model.Board;
import cs3500.queensboard.model.Card;
import cs3500.queensboard.model.ReadOnlyQueensBoard;
import cs3500.queensboard.player.PlayerActionsInterface;

/**
 * The {@code QueensBoardGUIViewClass} class is a custom {@link JPanel} that renders a graphical,
 * visual representation of the game board for the game. It displays the board grid, cards being
 * played with, and handles user interactions (selecting/de-selecting cards and cells).
 * <p>
 * This class is responsible for:
 *   <ul>
 *     <li> Rendering the game board with Cell objects </li>
 *     <li> Displaying the available cards in a player's hand </li>
 *     <li> Highlighting selected cards and cells by drawing a yellow border </li>
 *     <li> Managing user interactions like mouse clicks and keyboard inputs </li>
 *   </ul>
 * </p>
 */
public class QueensBoardGUIViewClass extends JPanel implements QueensBoardGUIView {
  private final ReadOnlyQueensBoard board;
  private List<QueensBoardControllerInterface> observers; // List of 1 observer
  private int boardHeight;
  private final Board.Player player;
  private int selectedCardIndex = -1;
  private int selectedRow = -1;
  private int selectedCol = -1;

  private QueensBoardControllerInterface controller; //this is the observer
  private PlayerActionsInterface playerType;

  /**
   * Creates an instance of the GUI view for the QueensBoard game.
   * A graphical, physical representation of the behaviors and objects described in the model.
   *
   * @param board  the read-only board model being displayed
   * @param player the current player (RED or BLUE)
   * @throws IllegalArgumentException if the given board is null
   */
  public QueensBoardGUIViewClass(ReadOnlyQueensBoard board, Board.Player player) {
    if (board == null) {
      throw new IllegalArgumentException("Board cannot be null");
    }
    setupListeners();
    this.board = board;
    this.player = player;

    //TODO FIXT RESIZE WINDOW
    int cardHeight = 300;
    int cellHeight = 130;
    boardHeight = (cellHeight * board.getHeight());

    //sets window size
    this.setPreferredSize(new Dimension(130 *
            board.getWidth() + 100, boardHeight + cardHeight + 20));
  }


  /**
   * Adds an observer (controller) to the list of observers.
   *
   * @param obs the observer (controller) being added
   */
  @Override
  public void addObserver(QueensBoardControllerInterface obs) {
    this.controller = obs;
  }

  private void setupListeners() {
    setFocusable(true);
    requestFocusInWindow(); // Ensure focus is on this component

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        handleMouseClick(e.getX(), e.getY());
        requestFocusInWindow(); // Regain focus on mouse click DIDNT HAVE BEFORE
      }
    });

    addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        // Do nothing
      }

      @Override
      public void keyPressed(KeyEvent e) {
        System.out.println("Key Pressed: " + e.getKeyCode());
        handleKeyPress(e);
      }

      @Override
      public void keyReleased(KeyEvent e) {
        // Do nothing
      }
    });

  }


  private void handleMouseClick(int x, int y) {
    if (controller == null) {
      System.err.println("Error: controller is null! Cannot process key press.");
      return;
    }
    if (!inputEnabled) {
      return; // Do nothing if input is disabled
    }

    // Check if the controller is initialized
    if (controller != null) {
      // Define dimensions
      int cardWidth = 150;
      int cellSize = 130;   // Each grid cell is 130x130 px
      int boardOffsetCellWidth = 40; // Adjust for potential padding if needed, 80 total
      int boardOffsetCard = 10; // Adjust for margins, 20 total

      if (isCardClick(y)) {
        int cardIndex = (x - boardOffsetCard) / cardWidth; // Adjusted for any offset

        if (cardIndex >= board.getHand().size()) {
          throw new IndexOutOfBoundsException("Card index out of bounds, no card found here");
        }

        Card selectedCard = null;
        // Validate the coordinates
        if (x >= 0 && x < (board.getWidth()) && y >= 0 && y < board.getWidth()) {
          selectedCard = board.getCardAt(x, y);
        } else {
          System.out.println("Invalid coordinates: (" + x + ", " + y + ")");
          // Optionally, show an error message to the user.
        }

        if (selectedCard == null) {
          System.out.println("Error: No card selected!");
        }

        //check if click in same spot aka twice to un-click
        if (selectedCardIndex == cardIndex) {
          selectedCardIndex = -1; // Deselect
          controller.cardSelected(); // Card was clicked
        } else {
          selectedCardIndex = cardIndex;
          controller.cardSelected(); // Card was clicked
        }
        controller.handleCardClick(cardIndex);
        // Notify the card panels to repaint and highlight the selected card
        repaint();

      } else if (isGridClick(y)) {
        int row = (y) / cellSize; // Compute row based on board dimensions
        int col = (x - boardOffsetCellWidth) / cellSize; // Compute column based on board dimensions

        //check if click in same spot aka twice to un-click
        if (selectedRow == row && selectedCol == col) {
          selectedRow = -1; // Deselect
          selectedCol = -1;
          controller.cellSelected(); // Cell was clicked
        } else {
          selectedRow = row;
          selectedCol = col;
          controller.cellSelected(); // Cell was clicked
        }
        controller.handleCellClick(row, col);
      }
    } else {
      System.err.println("Error: controller is null! Cannot process mouse click.");
    }
  }

  //
  private void handleKeyPress(KeyEvent e) {

    if (controller == null) {
      System.err.println("Error: controller is null! Cannot process key press.");
      return;
    }

    char c = e.getKeyChar();

    if (c == ' ') {  // Spacebar for placing a card
      System.out.println("Placing card at row: " + selectedRow + ", col: " + selectedCol);
      System.out.println("Selected card index: " + selectedCardIndex);

      if (selectedCardIndex >= 0 && selectedCardIndex < board.getHand().size()) {
        controller.handlePlaceKey(selectedCardIndex, selectedRow, selectedCol);
      } else {
        System.out.println("Invalid card index.");
      }

    } else if (c == 'p') {  // "P" key for passing turn
      System.out.println("Passing turn.");
      controller.handleTurnPass();
    }
  }


  private boolean isCardClick(int y) {
    return y > 130 * board.getHeight();
  }

  private boolean isGridClick(int y) {
    return y < 130 * board.getHeight();
  }


  /**
   * "Paints" the game board as a graphical object. This method draws the game board's visual
   * representation, including the board itself, pawns that exist and are placed onto the board,
   * a placed card, the row scores for each player, and each player's available hand.
   * This method also handles drawing the yellow outline around a highlighted card/cell.
   *
   * @param g the {@link Graphics} object used for painting the current state of the game
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (board.isGameOver()) { //if true aka game over
      return; // Don't continue with the painting logic if the game is over
    }
    Graphics2D g2d = (Graphics2D) g;

    drawBoard(g, 40);
    drawPawns(g, 40);
    drawRowScores(g, 40);
    drawPlayerHand(g, boardHeight + 20, player); // Draw cards first

    // Now draw highlights after cards, so they're on top
    g2d.setStroke(new BasicStroke(15)); // Set outline thickness

    drawPlacedCard(g, 40);

    // Highlight card
    if (selectedCardIndex != -1) {
      g.setColor(new Color(255, 223, 0)); // Highlight color
      int cardX = (selectedCardIndex * 150) + (12 * selectedCardIndex);
      int cardY = 130 * board.getHeight() + 15;
      g2d.drawRect(cardX, cardY, 160, 310);
    }

    // Highlight cell with thicker border
    g2d.setStroke(new BasicStroke(5));
    if (selectedRow != -1 && selectedCol != -1) {
      g.setColor(new Color(255, 223, 0));
      int cellX = (selectedCol * 130) + 40;
      int cellY = selectedRow * 130;
      g2d.drawRect(cellX, cellY, 130, 130);
    }


    g2d.setStroke(new BasicStroke(1)); // Reset stroke for other drawings
  }


  /**
   * Highlights the selected cell on the board by determining its position on the board,
   * and repainting the panel with the yellow outline.
   *
   * @param row The row index of the cell to highlight.
   * @param col The column index of the cell to highlight.
   */
  @Override
  public void highlightCell(int row, int col) {
    selectedRow = row;
    selectedCol = col;
    repaint();
  }

  /**
   * Highlights the selected card on the board by determining its index within the player hand
   * and repainting the panel with the yellow outline.
   *
   * @param cardIndex The index of the card in the player hand to highlight
   */
  @Override
  public void highlightCard(int cardIndex) {
    selectedCardIndex = cardIndex;
    repaint();
  }

  @Override
  public void placeCard(int row, int col, int cardIndex) {
    selectedCardIndex = cardIndex;
    selectedRow = row;
    selectedCol = col;
    repaint();
  }


  /**
   * Private helper to draw the Board in frame.
   *
   * @param g Graphic object
   */
  private void drawBoard(Graphics g, int extraSpace) {
    int cellSize = 130;

    for (int row = 0; row < board.getHeight(); row++) {
      for (int col = 0; col < board.getWidth(); col++) {
        int x = col * 130 + extraSpace;
        int y = row * cellSize;
        if ((row + col) % 2 == 0) {
          g.setColor(Color.LIGHT_GRAY); // Row Scores
        } else {
          g.setColor(Color.GRAY); // Cells
        }
        g.fillRect(x, y, cellSize, cellSize);
        g.setColor(Color.BLACK); // Outline
        g.drawRect(x, y, cellSize, cellSize);
      }
    }
  }

  /**
   * Private helper to draw each player's row scores in frame.
   *
   * @param g Graphic Object
   */
  private void drawRowScores(Graphics g, int extraSpace) {
    g.setColor(Color.BLACK); // Outline
    g.setFont(new Font("Arial", Font.BOLD, 20));

    for (int row = 0; row < board.getHeight(); row++) {
      g.drawString(String.valueOf(board.getRedRowScore(row)), 15, row * 130 + 75);
      g.drawString(String.valueOf(board.getBlueRowScore(row)),
              board.getWidth() * 130 + extraSpace + 15, row * 130 + 75);
    }
  }

  /**
   * Private helper to draw pawns of players, supporting up to 3 pawns per cell.
   *
   * @param g          Graphics object
   * @param extraSpace Extra spacing for board positioning
   */
  private void drawPawns(Graphics g, int extraSpace) {
    int cellSize = 130;
    int pawnSize = 40; // Reduce size for multiple pawns
    int spacing = 10; // Spacing between pawns

    for (int row = 0; row < board.getHeight(); row++) {
      for (int col = 0; col < board.getWidth(); col++) {
        Board.Player owner = board.getCell(row, col).getPawnOwner();
        int pawnCount = board.getCell(row, col).getPawnCount();

        if (owner == null || pawnCount == 0 || board.getCell(row, col).hasCard()) {
          continue; // Skip if no pawns or a card is present
        }

        // Set color based on owner
        if (owner == Board.Player.RED) {
          g.setColor(new Color(255, 102, 102));
        } else if (owner == Board.Player.BLUE) {
          g.setColor(new Color(135, 206, 235));
        }

        // Base position for drawing
        int baseX = col * cellSize + extraSpace + (cellSize - pawnSize) / 2;
        int baseY = row * cellSize + (cellSize - pawnSize) / 2;

        // Draw pawns based on count
        if (pawnCount == 1) {
          g.fillOval(baseX, baseY, pawnSize, pawnSize);
        } else if (pawnCount == 2) {
          g.fillOval(baseX - (pawnSize / 2 + spacing), baseY, pawnSize, pawnSize); // Left
          g.fillOval(baseX + (pawnSize / 2 + spacing), baseY, pawnSize, pawnSize); // Right
        } else if (pawnCount >= 3) {
          g.fillOval(baseX, baseY - (pawnSize / 2 + spacing), pawnSize, pawnSize); // Top
          g.fillOval(baseX - (pawnSize / 2 + spacing),
                  baseY + (pawnSize / 3), pawnSize, pawnSize); // Bottom-left
          g.fillOval(baseX + (pawnSize / 2 + spacing),
                  baseY + (pawnSize / 3), pawnSize, pawnSize); // Bottom-right
        }
      }
    }
  }


  /**
   * private helper draws placed card.
   */
  private void drawPlacedCard(Graphics g, int extraSpace) {
    int cellSize = 130;

    for (int row = 0; row < board.getHeight(); row++) {
      for (int col = 0; col < board.getWidth(); col++) {
        //if there is a card and red owns
        if (board.getCell(row, col).hasCard()
                && board.getCell(row, col).getCard().getInfluence() == Board.Player.RED) {
          g.setColor(new Color(255, 102, 102));
          int xVal = col * cellSize + extraSpace;
          int yVal = row * cellSize;
          g.fillRect(xVal, yVal, 130, 130);
          // Draw text inside the rectangle
          g.setColor(Color.BLACK); // Outline
          g.setFont(new Font("Arial", Font.BOLD, 40));
          String cardValue = String.valueOf(board.getCardAt(row, col).getValue());
          g.drawString(cardValue, xVal + 55, yVal + 80);  // Text and its position
        }
        //if there is a card and blue owns
        if (board.getCell(row, col).hasCard()
                && board.getCell(row, col).getCard().getInfluence() == Board.Player.BLUE) {
          int xVal = col * cellSize + extraSpace;
          int yVal = row * cellSize;
          g.setColor(new Color(135, 206, 235));
          g.fillRect(xVal, yVal, 130, 130);
          // Draw text inside the rectangle
          g.setColor(Color.BLACK); // Outline
          g.setFont(new Font("Arial", Font.BOLD, 40));
          String cardValue = String.valueOf(board.getCardAt(row, col).getValue());
          g.drawString(cardValue, xVal + 55, yVal + 80);  // Text and its position
        }
      }
    }
  }

  /**
   * Private helper to draw the player's current hand.
   *
   * @param g Graphic Object
   */
  private void drawPlayerHand(Graphics g, int startY, Board.Player player) {
    Board.Player currentPlayer = player;
    List<Card> hand = new ArrayList<>();
    if (player.equals(Board.Player.RED)) {
      hand = board.getRedHand();
    } else if (player.equals(Board.Player.BLUE)) {
      hand = board.getBlueHand();
    }

    this.removeAll(); // Clear previous card components

    int cardWidth = 150;
    int cardGap = 10;  // Gap between cards
    int totalWidth = hand.size() * (cardWidth + cardGap); // Adjust total width to include gap
    int startX = 10;

    for (int i = 0; i < hand.size(); i++) {
      Card card = hand.get(i);
      JPanel cardPanel = (currentPlayer == Board.Player.RED)
              ? new CardPanelRed(card)
              : new CardPanelBlue(card);

      // Add the gap after each card
      cardPanel.setBounds(startX + i * (cardWidth + cardGap), startY, cardWidth, 300);
      this.add(cardPanel);
    }

    this.revalidate();
    this.repaint();
  }

  /**
   * clear Highlights clears highlights after a move.
   */
  public void clearHighlights() {
    selectedCardIndex = -1;
    selectedRow = -1;
    selectedCol = -1;
    repaint();
  }

  private boolean inputEnabled = true;

  public void disableInput() {
    inputEnabled = false;
  }

  @Override
  public void showGameOver() {
    Board.Player winner = board.getWinner();
    int score = board.getScore(winner);
    JOptionPane.showMessageDialog(null,
            String.format("Game over! %s wins with a score of %d.", winner, score),
            "Game Over", JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public int getSelectedCardIndex() {
    return selectedCardIndex;
  }

  @Override
  public int getSelectedRow() {
    return selectedRow;
  }

  @Override
  public int getSelectedCol() {
    return selectedCol;
  }
}