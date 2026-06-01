package it.polimi.ingsw.controller.actions;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.RemoteException;

/**
 * Action used to register a player inside a game.
 * <p>
 * This is typically the first action executed when a player joins a match.
 * It delegates the registration process to the current game state.
 * </p>
 */
public class RegisterPlayerAction implements Action {
    private final String name;

    public RegisterPlayerAction(String name) {
        this.name = name;
    }

    /**
     * Executes the player registration inside the current game.
     *
     * <p>
     * Note: the registration is delegated to the game state and uses the
     * player's current game reference.
     * </p>
     *
     * @param player the player being registered
     * @throws IllegalActionException if the player cannot be registered
     *                                in the current state
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void execute(Player player) throws IllegalActionException, RemoteException {
        player.getGame().getState().registerPlayer(player.getGame(), player);
    }
}
