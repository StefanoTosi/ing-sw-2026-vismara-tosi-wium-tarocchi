package it.polimi.ingsw.controller.actions;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.RemoteException;

public class StartGameAction implements Action {
    public StartGameAction() {
    }

    @Override
    public void execute(Player player) throws IllegalActionException, RemoteException {
        player.getGame().getState().startGame(player.getGame());
    }
}
