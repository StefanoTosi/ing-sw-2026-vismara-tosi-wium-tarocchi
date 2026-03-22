package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

public class ChooseOfferState extends GameState {
    private final Game game;

    public ChooseOfferState(Game game) {
        this.game = game;
    }

    public void chooseOffer(Player player, char order)  throws IllegalActionException {
        if (order >= 'A' && order <= 'G') {
            game.putPlayerPosition(player, order);
        }
    }
}
