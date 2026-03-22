package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

public abstract class GameState {
    // ChooseOfferState
    public void chooseOffer(Player player, char order) throws IllegalActionException {
        throw new IllegalActionException("Illegal action");
    }

    // DrawCardState
    public void drawCardFromTop(Player player, int pos) throws IllegalActionException {
        throw new IllegalActionException("Illegal action");
    }

    public void drawCardFromBottom(Player player, int pos) throws IllegalActionException {
        throw new IllegalActionException("Illegal action");
    }
}
