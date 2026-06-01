package it.polimi.ingsw.controller.actions;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.RemoteException;

/**
 * Action that triggers the start of a game.
 * <p>
 * Typically executed when all required players have joined and the
 * match is ready to begin. The transition is delegated to the
 * current game state.
 * </p>
 */
public class StartGameAction implements Action {
    public StartGameAction() {
    }

    /**
     * Executes the game start operation for the specified player's game.
     *
     * @param player the player triggering the start of the game
     * @throws IllegalActionException if the game cannot be started in the
     *                                current state (e.g., not enough players)
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void execute(Player player) throws IllegalActionException, RemoteException {
        player.getGame().getState().startGame(player.getGame());
    }
}
