package it.polimi.ingsw.controller.actions;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

public class DrawCardFromTopAction implements Action {
    private final int pos;

    public DrawCardFromTopAction(int pos) {
        this.pos = pos;
    }

    @Override
    public void execute(Player player) throws IllegalActionException {
        player.getGame().getState().drawCardFromTop(player, pos);
    }
}
