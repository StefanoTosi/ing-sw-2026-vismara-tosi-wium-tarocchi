package it.polimi.ingsw.controller.actions;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

/**
 *
 */
public class DrawCardFromBottomAction implements Action {
    private final int pos;

    /**
     * Define the action of drawing from the bottom row
     * @param pos it's the absolute position of the card on the board,
     *            doesn't matter in which List it belongs
     */
    public DrawCardFromBottomAction(int pos) {
        this.pos = pos;
    }

    /**
     *  Execute the action on the specified player
     * @param player
     * @throws IllegalActionException
     */
    @Override
    public void execute(Player player) throws IllegalActionException {
        player.getGame().getState().drawCardFromTop(player, pos);
    }
}
