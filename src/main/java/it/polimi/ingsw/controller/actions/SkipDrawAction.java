package it.polimi.ingsw.controller.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.IOException;

/**
 * Action that allows a player to skip the draw phase.
 * <p>
 * When executed, the current game state is notified that the player
 * chooses not to draw any card, advancing the game flow accordingly.
 * </p>
 */
public class SkipDrawAction implements Action{

    @JsonCreator
    public SkipDrawAction(){}

    /**
     * Executes the skip-draw action for the given player.
     *
     * @param player the player performing the action
     * @throws IllegalActionException if skipping draw is not allowed
     *                                in the current game state
     * @throws IOException if an I/O error occurs during execution
     */
    @Override
    public void execute(Player player) throws IllegalActionException, IOException {
        player.getGame().getState().skipDraw(player);
    }
}
