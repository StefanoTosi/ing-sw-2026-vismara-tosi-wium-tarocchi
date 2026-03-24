package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

public class DrawCardState extends GameState {
    private final Game game;

    public DrawCardState(Game game) {
        this.game = game;
    }

    public void drawCardFromTop(Player player, int pos) throws IllegalActionException {
        // TODO: gestire pescate da builing
        player.addCard(game.getBoard().drawFromTopRowTribe(pos));
    }

    public void drawCardFromBottom(Player player, int pos) throws IllegalActionException {
        // TODO: gestire pescate da builing
        player.addCard(game.getBoard().drawFromBottomRowTribe(pos));
    }
}
