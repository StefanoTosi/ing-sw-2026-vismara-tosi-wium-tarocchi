package it.polimi.ingsw.controller.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.IOException;

public class SkipDrawAction implements Action{

    @JsonCreator
    public SkipDrawAction(){}

    @Override
    public void execute(Player player) throws IllegalActionException, IOException {
        player.getGame().getState().skipDraw(player);
    }
}
