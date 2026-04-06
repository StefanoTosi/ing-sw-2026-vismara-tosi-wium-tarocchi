package it.polimi.ingsw.controller.actions;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

public class StartGameAction implements Action {
    public StartGameAction() {
    }

    @Override
    public void execute(Player player) throws IllegalActionException {
        player.getGame().getState().startGame(player.getGame());
    }
}
