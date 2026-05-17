package it.polimi.ingsw.controller.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.IOException;
import java.rmi.RemoteException;

public class DrawCardFromTopAction implements Action {
    private final int pos;

    /**
     * Define the action of drawing from the top row
     * @param pos it's the absolute position of the card on the board,
     *            doesn't matter in which List it belongs
     */
    @JsonCreator
    public DrawCardFromTopAction(@JsonProperty("pos") int pos) {
        this.pos = pos;
    }

    @JsonProperty("pos")
    public int getPos() {
        return pos;
    }

    /**
     * Execute the action on the specified player
     * @param player
     * @throws IllegalActionException
     */
    @Override
    public void execute(Player player) throws IllegalActionException, IOException {
        player.getGame().getState().drawCardFromTop(player, pos);
    }

}
