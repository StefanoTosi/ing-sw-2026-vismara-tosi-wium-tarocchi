package it.polimi.ingsw.controller.actions;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.RemoteException;

public class RegisterPlayerAction implements Action {
    private final String name;

    public RegisterPlayerAction(String name) {
        this.name = name;
    }

    @Override
    public void execute(Player player) throws IllegalActionException, RemoteException {
        // TODO: dobbiamo passare game perché player non esiste
        player.getGame().getState().registerPlayer(player.getGame(), player);
    }
}
