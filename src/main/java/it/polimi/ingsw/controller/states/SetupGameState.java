package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.RemoteException;

public class SetupGameState extends GameState {
    public void registerPlayer(Game game, Player player) throws IllegalActionException, RemoteException {
        // Check it's a valid action
        game.getPlayers().add(player);

        // Start the game
        // game.notifyObserver();
        if (game.getPlayers().size() == game.getNumPlayers()) {
            game.getState().startGame(game);
        }
    }

    public void startGame(Game game) throws IllegalActionException, RemoteException {
        // Initialize board
        game.getBoard().initialize(game.getPlayers().size());
        System.out.println("Game started");

        // Transition
        game.setState(new FillBoardState(game));
        game.getState().refillBoard();
    }
}
