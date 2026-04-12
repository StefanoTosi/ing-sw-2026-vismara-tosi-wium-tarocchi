package it.polimi.ingsw.controller.actions;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.RemoteException;

public class ChooseOfferAction implements Action {
    private final char order;

    public ChooseOfferAction(char order) {
        this.order = order;
    }

    @Override
    public void execute(Player player) throws IllegalActionException, RemoteException {
        player.getGame().getState().chooseOffer(player, order);
    }
}
