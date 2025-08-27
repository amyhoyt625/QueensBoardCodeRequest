package cs3500.queensboard.view;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.BasicStroke;

import cs3500.queensboard.model.Card;

/**
 * The {@code CardPanelBlue} class is a custom {@link JPanel} that represents a graphical
 * way to display a {@link Card} in the game. The Blue Player card is visually represented
 * with a light blue background, card information(including name, cost, value, and influence grid).
 * <p>
 *   This class is responsible for rendering a SINGLE card's details:
 *   String name
 *   Int Cost
 *   Int value
 *   Char[][] influenceGrid
 *   And also to indicate if a card was selected (highlighted).
 *   The highlighting is done by outlines the selected card in yellow.
 * </p>
 */
public class CardPanelBlue extends JPanel {

  private final Card card;
  private boolean highlight = false; // Flag to indicate if the card is selected/highlighted

  /**
   * Creates a new instance of [@code CardPanelBlue} with the specified card.
   * The card is defined to be a set size of 150 x 300 pixels.
   *
   * @param card The card being converted to a graphical object.
   */
  public CardPanelBlue(Card card) {
    this.card = card;
    this.setPreferredSize(new Dimension(150, 300));
  }

  /**
   * Changes the boolean highlight flag if a card should be highlighted.
   * If card is highlighted, a yellow border is drawn around the card, and triggers
   * the repaint method to "redraw" the current state of the card.
   *
   * @param highlight {@code true} to highlight, {@code false} to remove highlight
   */
  public void setHighlight(boolean highlight) {
    this.highlight = highlight;
    repaint();  // Repaint the panel to show the highlight
  }

  /**
   * "Paints" the card as a graphical object. This method draws the card's visual representation,
   * including the blue background, black outline, and card information.
   * If the card is highlighted, a yellow border is drawn around the card, and
   * the repaint method is called.
   *
   * @param g the {@link Graphics} object used for painting the blue card
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);  // Ensures the background is properly cleared before painting
    Graphics2D g2d = (Graphics2D) g; // Cast Graphics to Graphics2D

    g2d.setColor(new Color(135, 206, 235)); // Light blue background
    g2d.fillRect(0, 0, getWidth(), getHeight());

    if (highlight) { // If highlighted, draw a border around the card
      g2d.setColor(Color.YELLOW);  // Highlight color
      g2d.setStroke(new BasicStroke(5));  // Thicker border for highlight
      g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1); // Draw highlight
      g2d.setStroke(new BasicStroke(1)); // Reset stroke to default
    }

    g2d.setColor(Color.BLACK); // Black Outline (5 pixels inset)
    g2d.drawRect(5, 5, getWidth() - 10, getHeight() - 10);

    g2d.setColor(Color.BLACK); // Card Components
    g2d.setFont(new Font("Arial", Font.BOLD, 14)); // Matching font style
    g2d.drawString(card.getName(), 10, 40); // Draw card name
    g2d.drawString("Cost: " + card.getCost(), 10, 60); // Draw card cost
    g2d.drawString("Value: " + card.getValue(), 10, 80); // Draw card value

    // Influence Grid (Adjusted for padding)
    int gridSize = 5;
    int cellSize = 20;
    int gridStartX = 5; // Small margin for aesthetics
    int gridStartY = 100;

    char[][] influenceGrid = card.getInfluenceGrid();
    for (int row = 0; row < gridSize; row++) {
      for (int col = 0; col < gridSize; col++) {
        switch (influenceGrid[row][col]) { // Set color based on influence grid value
          case 'I':
            g2d.setColor(Color.CYAN); // Influenced cells
            break;
          case 'C':
            g2d.setColor(Color.ORANGE); // Card cells
            break;
          default:
            g2d.setColor(Color.DARK_GRAY); // Empty cells
        }
        g2d.fillRect(gridStartX + col * cellSize,
                gridStartY + row * cellSize, cellSize, cellSize);

        g2d.setColor(Color.BLACK); // Draw cell outline
        g2d.drawRect(gridStartX + col * cellSize,
                gridStartY + row * cellSize, cellSize, cellSize);
      }
    }
  }

}