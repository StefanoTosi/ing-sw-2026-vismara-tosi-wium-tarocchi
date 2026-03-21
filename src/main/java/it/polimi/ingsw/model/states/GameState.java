package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.Player;

public abstract class GameState {
    // ChooseOfferState
    public void chooseOffer(Player player, char order) {
        return;
    }

    // DrawCardState
    public void drawCardFromTop(Player player, int pos) {
        return;
    }

    public void drawCardFromBottom(Player player, int pos) {
        return;
    }
}
