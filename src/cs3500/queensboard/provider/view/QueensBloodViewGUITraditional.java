package cs3500.queensboard.provider.view;

import cs3500.queensboard.provider.controller.ViewListener;
import cs3500.queensboard.provider.model.BoardPiece;
import cs3500.queensboard.provider.model.Card;
import cs3500.queensboard.provider.model.Player;
import cs3500.queensboard.provider.model.ReadOnlyQueensBloodModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * GUI representation of the QueensBlood game that is utilizing the Java Swing toolkit.
 * Displays the game board, every player's hand of cards (includes card name, cost, value,
 * and influence grid), the both players' row scores on each row of the board. The class
 * additionally supports actions like keyboard inputs, cell selection, card selection, and
 * confirmations of moves/passing that is through a stub controller.
 */
public class QueensBloodViewGUITraditional extends JFrame implements QueensBloodViewGUI {
  private final ReadOnlyQueensBloodModel model;
  private final Player player;
  JLabel[] rowScoreLeft;
  JLabel[] rowScoreRight;
  private ViewListener viewListener;
  private HashMap<Player, ArrayList<Integer>> rowScores;
  private JButton[][] boardCells;
  private Card selectedCard = null;
  private JButton selectedBoardCell = null;
  private JPanel panelHandRed;
  private JPanel panelHandBlue;


  /**
   * Constructs the GUI and initializes the visuals of the game within the given model.
   *
   * @param model read-only model of the QueensBlood board game.
   * @throws IllegalArgumentException If model or player given are null.
   */
  public QueensBloodViewGUITraditional(ReadOnlyQueensBloodModel model, Player player) {
    if (model == null || player == null) {
      throw new IllegalArgumentException("Model and player cannot be null");
    }
    this.model = model;
    this.player = player;
    //initiliaze rowScores
    rowScores = new HashMap<>();
    buildBoard(model, player);

  }

  private void buildBoard(ReadOnlyQueensBloodModel model, Player player) {
    getContentPane().removeAll();
    setTitle("QueensBlood Game");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    setSize(1000, 700);

    JPanel panelBoardWrapper = new JPanel(new BorderLayout());

    JPanel panelLeftScores = new JPanel(new GridLayout(5, 1));

    rowScoreLeft = new JLabel[model.getHeight()];

    for (int i = 0; i < 5; i++) {
      rowScoreLeft[i] = new JLabel(" 0 ", SwingConstants.CENTER);
      rowScoreLeft[i].setOpaque(true);
      rowScoreLeft[i].setBackground(Color.GRAY);
      rowScoreLeft[i].setForeground(Color.WHITE);
      panelLeftScores.add(rowScoreLeft[i]);
    }

    JPanel panelRightScores = new JPanel(new GridLayout(5, 1));
    rowScoreRight = new JLabel[model.getHeight()];
    for (int i = 0; i < model.getHeight(); i++) {
      rowScoreRight[i] = new JLabel(" 0 ", SwingConstants.CENTER);
      rowScoreRight[i].setOpaque(true);
      rowScoreRight[i].setBackground(Color.GRAY);
      rowScoreRight[i].setForeground(Color.WHITE);
      panelRightScores.add(rowScoreRight[i]);
    }

    JPanel panelBoard = new JPanel(new GridLayout(model.getHeight(), model.getWidth(), 5, 5));
    panelBoard.setBackground(Color.LIGHT_GRAY);
    boardCells = new JButton[model.getHeight()][model.getWidth()];

    for (int row = 0; row < model.getHeight(); row++) {
      for (int col = 0; col < model.getWidth(); col++) {
        BoardPiece piece = model.getItemAt(row, col);

        String cellText = " ";
        Color textColor = Color.BLACK;

        if (piece != null) {
          int display = piece.getDisplayValue();
          cellText = (display > 0) ? String.valueOf(display) : " ";

          Player owner = piece.getOwner();
          if (owner == Player.RED) {
            textColor = Color.RED;
          } else if (owner == Player.BLUE) {
            textColor = Color.BLUE;
          }
        }

        JButton cell = new JButton(cellText);
        cell.setForeground(textColor);
        cell.setPreferredSize(new Dimension(80, 80));
        cell.setBackground(Color.WHITE);
        cell.addActionListener(e -> selectBoardCell(cell));
        boardCells[row][col] = cell;
        panelBoard.add(cell);
      }
    }

    panelBoardWrapper.add(panelLeftScores, BorderLayout.WEST);
    panelBoardWrapper.add(panelBoard, BorderLayout.CENTER);
    panelBoardWrapper.add(panelRightScores, BorderLayout.EAST);
    add(panelBoardWrapper, BorderLayout.CENTER);

    if (player == Player.RED) {
      panelHandRed = new JPanel(new FlowLayout());
      panelHandRed.setBackground(Color.RED);
      int index = 0;
      for (Card card : model.getRedPlayerState().getHand()) {
        panelHandRed.add(new CardPanel(card, Player.RED, index++, this));
      }
      add(panelHandRed, BorderLayout.NORTH);
    }

    if (player == Player.BLUE) {
      panelHandBlue = new JPanel(new FlowLayout());
      panelHandBlue.setBackground(Color.BLUE);
      int index = 0;
      for (Card card : model.getBluePlayerState().getHand()) {
        panelHandBlue.add(new CardPanel(card, Player.BLUE, index++, this));
      }
      add(panelHandBlue, BorderLayout.SOUTH);
    }

    setVisible(true);

    addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          if (selectedCard != null && selectedBoardCell != null) {
            for (int r = 0; r < boardCells.length; r++) {
              for (int c = 0; c < boardCells[0].length; c++) {
                if (boardCells[r][c] == selectedBoardCell) {
                  if (viewListener != null) {
                    viewListener.confirmMove();
                  }
                  return;
                }
              }
            }
          } else {
            if (selectedCard == null && selectedBoardCell != null) {
              JOptionPane.showMessageDialog(
                      QueensBloodViewGUITraditional.this,
                      "Please select a card before confirming.",
                      "Missing Selection",
                      JOptionPane.WARNING_MESSAGE);
            } else if (selectedCard != null && selectedBoardCell == null) {
              JOptionPane.showMessageDialog(
                      QueensBloodViewGUITraditional.this,
                      "Please select a cell before confirming.",
                      "Missing Selection",
                      JOptionPane.WARNING_MESSAGE);
            }
          }
        } else if (e.getKeyCode() == KeyEvent.VK_P) {
          if (viewListener != null) {
            viewListener.passTurn();
          }
        }
      }
    });

    this.addComponentListener(new ComponentAdapter() {

      @Override
      public void componentResized(ComponentEvent e) {
        Dimension newSize = getSize();
        int width = newSize.width;

        int cardWidth = width / 10;
        int cardHeight = cardWidth * 4 / 3;

        if (panelHandRed != null) {
          for (Component c : panelHandRed.getComponents()) {
            c.setPreferredSize(new Dimension(cardWidth, cardHeight));
          }
          panelHandRed.revalidate();
          panelHandRed.repaint();
        }

        if (panelHandBlue != null) {
          for (Component c : panelHandBlue.getComponents()) {
            c.setPreferredSize(new Dimension(cardWidth, cardHeight));
          }
          panelHandBlue.revalidate();
          panelHandBlue.repaint();
        }
        if (panelHandRed != null && panelHandBlue != null) {
          panelHandRed.revalidate();
          panelHandRed.repaint();
          panelHandBlue.revalidate();
          panelHandBlue.repaint();
        }
      }
    });

    setFocusable(true);
    requestFocusInWindow();
    revalidate();
    repaint();
  }


  private void selectBoardCell(JButton cell) {
    if (selectedBoardCell != null) {
      selectedBoardCell.setBackground(Color.WHITE);
    }
    selectedBoardCell = cell;
    selectedBoardCell.setBackground(Color.CYAN);

    for (int r = 0; r < boardCells.length; r++) {
      for (int c = 0; c < boardCells[0].length; c++) {
        if (boardCells[r][c] == cell) {
          if (viewListener != null) {
            viewListener.selectCell(r, c);
          }
          requestFocusInWindow();
          return;
        }
      }
    }
  }

  @Override
  public ViewListener getListener() {
    return this.viewListener;
  }

  @Override
  public void selectCard(Card card) {
    this.selectedCard = card;
  }

  @Override
  public void updateRowScores() {
    try {
      for (int r = 0; r < boardCells.length; r++) {
        int redRowScore = model.getRowScore(Player.RED, r);
        int blueRowScore = model.getRowScore(Player.BLUE, r);
        if (!rowScores.containsKey(Player.RED)) {
          rowScores.put(Player.RED, new ArrayList(List.of(redRowScore)));
        } else if (!rowScores.containsKey(Player.BLUE)) {
          rowScores.put(Player.BLUE, new ArrayList(List.of(blueRowScore)));
        } else {
          rowScores.get(Player.RED).add(redRowScore);
          rowScores.get(Player.BLUE).add(blueRowScore);
        }
      }

      ArrayList<Integer> redRowScores = rowScores.get(Player.RED);
      ArrayList<Integer> blueRowScores = rowScores.get(Player.BLUE);

      for (int i = 0; i < redRowScores.size(); i++) {
        String numberRedRowScore = " " + String.valueOf(redRowScores.get(i)) + " ";
        rowScoreLeft[i].setText(numberRedRowScore);

        //panelLeftScores.add(rowScoreLeft[i]);
      }

      for (int i = 0; i < blueRowScores.size(); i++) {
        String numberBlueRowScore = " " + String.valueOf(blueRowScores.get(i)) + " ";
        rowScoreRight[i].setText(numberBlueRowScore);

        //panelRightScores.add(rowScoreRight[i]);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  public void placeCard(Card card, int row, int col) {
    int display = card.getDisplayValue();
    String cellText = (display > 0) ? String.valueOf(display) : " ";

    Player owner = card.getOwner();
    Color textColor = Color.BLACK;

    if (owner == Player.RED) {
      boardCells[row][col].setBackground(Color.RED);
      boardCells[row][col].setFont(new Font("Arial", Font.BOLD, 30));
      boardCells[row][col].setForeground(textColor);
      boardCells[row][col].setText(cellText);
    } else if (owner == Player.BLUE) {
      boardCells[row][col].setBackground(Color.BLUE);
      boardCells[row][col].setFont(new Font("Arial", Font.BOLD, 30));
      boardCells[row][col].setForeground(textColor);
      boardCells[row][col].setText(cellText);
    }

  }


  @Override
  public Card getSelectedCard() {
    return this.selectedCard;
  }

  @Override
  public void addListener(ViewListener listener) {
    this.viewListener = listener;
  }


  @Override
  public void highlightCard(int cardIdx) {
    // Handled by UI
  }

  @Override
  public void highlightCell(int row, int col) {
    if (row >= 0 && row < boardCells.length && col >= 0 && col < boardCells[0].length) {
      boardCells[row][col].setBackground(Color.YELLOW);
    }
  }

  @Override
  public void clearHighlights() {
    for (int r = 0; r < boardCells.length; r++) {
      for (int c = 0; c < boardCells[0].length; c++) {
        boardCells[r][c].setBackground(Color.WHITE);
      }
    }
    selectedCard = null;
    selectedBoardCell = null;
  }

  @Override
  public void showError(String message) {
    javax.swing.JOptionPane.showMessageDialog(
            this,
            message,
            "Error Given",
            javax.swing.JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void showGameOver(String message) {
    javax.swing.JOptionPane.showMessageDialog(
            this,
            message,
            "Game Over",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public void showGame() {
    buildBoard(model, player);
  }

  @Override
  public void setTurnHighlight(boolean isActive) {
    this.setTitle(isActive ? "Your Turn" : "Waiting for turn to be done");
  }
}