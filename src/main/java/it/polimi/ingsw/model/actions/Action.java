package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.Player;

public interface Action {
    void execute(Player player) throws IllegalArgumentException;
}
