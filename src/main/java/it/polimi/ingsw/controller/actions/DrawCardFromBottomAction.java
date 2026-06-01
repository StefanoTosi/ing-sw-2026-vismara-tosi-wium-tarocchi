package it.polimi.ingsw.controller.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Action that allows a player to draw a card from the bottom row
 * of the game board.
 * <p>
 * The position identifies which card the player wants to draw.
 * The actual validation and execution are delegated to the current
 * game state.
 * </p>
 */
public class DrawCardFromBottomAction implements Action {
    private final int pos;


    @JsonCreator
    public DrawCardFromBottomAction(@JsonProperty("pos") int pos) {
        this.pos = pos;
    }

    @JsonProperty("pos")
    public int getPos() {
        return pos;
    }

    /**
     * Executes the card draw action for the specified player.
     *
     * @param player the player performing the action
     * @throws IllegalActionException if the selected card cannot be drawn
     *                                in the current game state
     * @throws IOException if an I/O error occurs during execution
     */
    @Override
    public void execute(Player player) throws IllegalActionException, IOException {
        player.getGame().getState().drawCardFromBottom(player, pos);
    }

}
