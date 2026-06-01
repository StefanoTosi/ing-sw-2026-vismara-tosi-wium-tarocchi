package it.polimi.ingsw.controller.actions;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.RemoteException;

/**
 * Action that triggers a game to start when all players have successfully joined.
 */
public class StartGameAction implements Action {
    /**
     * Generates a new {@code StartGameAction} object.
     */
    public StartGameAction() {
    }

    @Override
    public void execute(Player player) throws IllegalActionException, RemoteException {
        player.getGame().getState().startGame(player.getGame());
    }
}
