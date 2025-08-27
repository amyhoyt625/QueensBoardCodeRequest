package cs3500.queensboard.provider.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for reading and loading deck configurations from a file.
 */
public class Reader {
  /**
   * This method will read a deck configuration file and then return a list of objects
   * that are of type CardGame. These represent the player's deck.
   *
   * @param file  configuration file's path.
   * @param owner player that owns the deck. Can be Red or Blue player.
   * @return list of CardGame objects.
   * @throws IOException if the current file is not able to be read properly.
   */
  public static List<Card> readDeck(String file, Player owner) throws IOException {
    List<Card> deck = new ArrayList<>();
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
      String line;

      while ((line = bufferedReader.readLine()) != null) {
        line.trim(); //will remove the extra spaces

        if (line.isEmpty()) {
          continue;
        }

        //Will get name, header and all three params of card
        //break the line into an array of strings divided by " " as after
        //Card: each card contains Name Cost Value
        String[] cardParameters = line.split(" ");

        //Card parameters need to be equal to 3, else, card not correctly formatted.
        if (cardParameters.length != 3) {
          throw new IOException("Invalid card format: Missing parameters");
        }

        String name = cardParameters[0].trim(); //Card name
        int cost = Integer.parseInt(cardParameters[1].trim()); //Cost
        int value = Integer.parseInt(cardParameters[2].trim()); //Value


        //Then, read influence grid
        char[][] influenceGrid = new char[5][5];
        for (int k = 0; k < 5; k++) {
          line = bufferedReader.readLine();
          //Card not correctly formatted
          // This is a class invariant
          // The influence grid should always be size 5x5
          // If a line is null is not 5 characters then the card throws an exception
          if (line == null || line.trim().length() != 5) {
            throw new IOException("Invalid influence grid format for " +
                    "card: " + name);
          }
          influenceGrid[k] = line.trim().toCharArray();
        }
        deck.add(new CardGame(name, cost, value, influenceGrid, owner));
      }
    }
    return deck;
  }
}
