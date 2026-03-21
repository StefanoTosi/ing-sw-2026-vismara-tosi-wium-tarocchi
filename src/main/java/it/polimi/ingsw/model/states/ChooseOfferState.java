package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

public class ChooseOfferState extends GameState {
    private final Game game;

    public ChooseOfferState(Game game) {
        this.game = game;
    }

    public void chooseOffer(Player player, char order) {
        if (order >= 'A' && order <= 'G') {
            game.putPlayerPosition(player, order);
        }
    }
}
