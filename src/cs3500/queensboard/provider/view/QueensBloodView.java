package cs3500.queensboard.provider.view;


import cs3500.queensboard.provider.model.BoardPiece;
import cs3500.queensboard.provider.model.Player;
import cs3500.queensboard.provider.model.ReadOnlyQueensBloodModel;

/**
 * Represents the view for a queens blood board game. It assumes that
 * the constructed board will follow a rectangular pattern. All cards
 * will be displayed wth a name, cost and their influence 5x5 grid on board.
 * Empty cells will.
 */
public class QueensBloodView {
  private final ReadOnlyQueensBloodModel model;

  /**
   * Constructs the view of the QueensBlood game. Checks if the output or model
   * will be null before starting in order to prevent errors from occurring.
   * Our second param output appendable destination that is outputted, will be added later
   * when controller introduced.
   *
   * @param model that is going to be displayed.
   * @throws IllegalArgumentException if output or model are null.
   */
  public QueensBloodView(ReadOnlyQueensBloodModel model) {

    if (model == null) {
      throw new IllegalArgumentException("Can't have a null model/output");
    }
    this.model = model;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();

    result.append("Textual view\n");

    int rows = model.getHeight();
    int columns = model.getWidth();

    //Consider rows 0 through rows
    for (int row = 0; row < rows; row++) {
      int redRowScore = 0;
      int blueRowScore = 0;
      StringBuilder rowContent = new StringBuilder();

      //Also, only consider columns 0 through columns
      for (int col = 0; col < columns; col++) {
        BoardPiece piece = model.getItemAt(row, col);

        if (piece == null) {
          rowContent.append("_");
        } else {
          Player owner = piece.getOwner();
          if (owner == Player.RED) {
            //Append the respective toString() of that piece. Remember, due to
            //double dispatch, program will be able to decide which toString() to
            //append, either Pawn's or CardGame's.
            rowContent.append(piece);
            redRowScore += piece.getValue();
          } else if (owner == Player.BLUE) {
            //Same for blue
            rowContent.append(piece);
            blueRowScore += piece.getValue();
          } else {
            rowContent.append(piece);
          }
        }
      }

      result.append(String.format("%2d %s %2d%n", redRowScore, rowContent, blueRowScore));
    }

    return result.toString();
  }


}
