package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.states.ChooseOfferState;

public class ChooseOfferAction implements Action {
    private final char order;

    public ChooseOfferAction(char order) {
        this.order = order;
    }

    @Override
    public void execute(Player player) throws IllegalArgumentException {
        player.getGame().getState().chooseOffer(player, order);
    }
}
