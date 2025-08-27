package cs3500.queensboard.provider.view;

import cs3500.queensboard.provider.model.Card;
import cs3500.queensboard.provider.model.Player;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * This class is a Panel representing a card in the player's hand.
 */
class CardPanel extends JPanel {
  private final Card card;
  private final Player owner;

  /**
   * Constructs the CardPanel. Visualizes card's information and also manages selection.
   *
   * @param card  at hand.
   * @param owner of the card.
   * @param gui   view representation.
   */
  public CardPanel(Card card, Player owner, int cardIndex, QueensBloodViewGUI gui) {
    this.card = card;
    this.owner = owner;

    // Sizing and coloring from ownership
    setPreferredSize(new Dimension(120, 160));
    setBackground(owner == Player.RED ? Color.PINK : Color.CYAN);
    setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

    // Manages selection and deselection of the cards
    addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        if (gui.getSelectedCard() == card) {
          gui.selectCard(null); // Deselect
          setBackground(owner == Player.RED ? Color.PINK : Color.CYAN);
        } else {
          gui.selectCard(card); // Select this card
          for (Component comp : getParent().getComponents()) {
            if (comp instanceof CardPanel) {
              CardPanel other = (CardPanel) comp;
              other.setBackground(other.owner == Player.RED ? Color.PINK : Color.CYAN);
            }
          }
          setBackground(Color.YELLOW); // Highlight selected
        }

        if (gui.getListener() != null) {
          gui.getListener().selectCard(cardIndex, owner);
        }
        // Prints card index selected at given clicked position and the player
        System.out.println("Selected card index: " + cardIndex + ", Player: " + owner);

      }
    });
  }


  /**
   * This override method is utilized to draw the card's information.
   * Information includes a name, cost, value, and 5x5 influence grid.
   *
   * @param g the <code>Graphics</code> object to protect.
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    // Name, Cost, and value drawings
    g2.setFont(new Font("Arial", Font.BOLD, 12));
    g2.setColor(Color.BLACK);
    g2.drawString(card.getName(), 10, 15);
    g2.drawString("Cost: " + card.getCost(), 10, 30);
    g2.drawString("Value: " + card.getValue(), 10, 45);

    // Grid drawings
    int gridSize = 15;
    int startX = 10;
    int startY = 60;

    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        char cell = card.getInfluenceGrid()[i][j];
        Color fillColor;

        switch (cell) {
          case 'X':
            fillColor = Color.CYAN;
            break;
          case 'C':
            fillColor = Color.ORANGE;
            break;
          default:
            fillColor = Color.DARK_GRAY;
        }

        // Draws the cells and bordering
        g2.setColor(fillColor);
        g2.setColor(fillColor);
        g2.fillRect(startX + j * gridSize, startY + i * gridSize, gridSize, gridSize);
        g2.setColor(Color.BLACK);
        g2.drawRect(startX + j * gridSize, startY + i * gridSize, gridSize, gridSize);
      }
    }
  }
}