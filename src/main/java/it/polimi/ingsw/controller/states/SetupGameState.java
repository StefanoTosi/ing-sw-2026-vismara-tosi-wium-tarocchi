package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

public class SetupGameState extends GameState {
    public void registerPlayer(Game game, Player player) throws IllegalActionException {
        // Check it's a valid action
        game.getPlayers().add(player);

        // Start the game
        if (game.getPlayers().size() == game.getNumPlayers()) {
            game.getState().startGame(game);
        }
    }

    public void startGame(Game game) throws IllegalActionException {
        // Initialize board
        game.getBoard().initialize(game.getPlayers().size());

        // Transition
        game.setState(new FillBoardState());
    }
}
