package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.states.DrawCardState;

public class DrawCardFromTopAction implements Action {
    private final int pos;

    public DrawCardFromTopAction(int pos) {
        this.pos = pos;
    }

    @Override
    public void execute(Player player) throws IllegalArgumentException {
        player.getGame().getState().drawCardFromTop(player, pos);
    }
}
