package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

public class SetupGameState extends GameState {
    public void registerPlayer(Game game, String name) throws IllegalActionException {
        // Check it's a valid action
        boolean found = false;
        for (Player p : game.getPlayers()) {
            if (p.getName().equals(name)) {
                found = true;
                break;
            }
        }

        if (!found) {
            // Add the player
            game.getPlayers().add(new Player(name));
        } else {
            // TODO: Return an error
        }

        // Start the game
        if (game.getPlayers().size() == 5) {
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
