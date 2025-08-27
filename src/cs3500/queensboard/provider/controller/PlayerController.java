package cs3500.queensboard.provider.controller;

/**
 * Represents a controller that manages a single player's interaction with the QueensBlood game.
 * The idea is each player will be given one controller, the same model and different views so
 * that It handles player input (via GUI or strategy), updates the model accordingly, and ensures
 * the view stays in sync with the actions took.
 */
public interface PlayerController {
  /**
   * Starts the controller for this player. For human players, it initializes
   * the view and waits for input. For machine players, it attempts a move immediately.
   * This method must be called after all players are registered and the model is started.
   */
  void playGame();
}