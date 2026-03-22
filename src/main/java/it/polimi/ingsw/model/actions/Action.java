package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

public interface Action {
    void execute(Player player) throws IllegalActionException;
}
