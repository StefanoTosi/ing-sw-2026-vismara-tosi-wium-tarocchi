package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.Player;

public class DrawCardFromBottomAction implements Action {
    private final int pos;

    public DrawCardFromBottomAction(int pos) {
        this.pos = pos;
    }

    @Override
    public void execute(Player player) throws IllegalArgumentException {
        player.getGame().getState().drawCardFromTop(player, pos);
    }
}
