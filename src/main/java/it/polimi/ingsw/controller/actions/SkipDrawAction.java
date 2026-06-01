package it.polimi.ingsw.controller.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.IOException;

/**
 * Action that allows a player to skip their drawing turn if the only available cards are {@code Buildings}.
 */
public class SkipDrawAction implements Action{

    /**
     * Generates a new {@code SkipDrawAction} object.
     */
    @JsonCreator
    public SkipDrawAction(){}

    @Override
    public void execute(Player player) throws IllegalActionException, IOException {
        player.getGame().getState().skipDraw(player);
    }
}