package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

public class DrawCardState extends GameState {
    private final Game game;

    public DrawCardState(Game game) {
        this.game = game;
    }

    public void drawCardFromTop(Player player, int pos) throws IllegalActionException {
        player.addCard(game.getBoard().drawFromTopRow(pos));
    }

    public void drawCardFromBottom(Player player, int pos) throws IllegalActionException {
        player.addCard(game.getBoard().drawFromBottomRow(pos));
    }
}
