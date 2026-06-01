package it.polimi.ingsw.controller.actions;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.RemoteException;

/**
 * Action that allows a user to join an existing game.<br>
 */
public class RegisterPlayerAction implements Action {
    private final String name;

    /**
     * Generates a new {@code RegisterPlayerAction} object.
     * @param name the user's chosen name
     */
    public RegisterPlayerAction(String name) {
        this.name = name;
    }

    @Override
    public void execute(Player player) throws IllegalActionException, RemoteException {
        player.getGame().getState().registerPlayer(player.getGame(), player);
    }
}