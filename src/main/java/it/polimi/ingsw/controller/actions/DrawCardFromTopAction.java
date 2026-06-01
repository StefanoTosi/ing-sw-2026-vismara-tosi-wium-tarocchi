package it.polimi.ingsw.controller.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Action that allows a player to draw a card from the top row
 * of the game board.
 * <p>
 * The position identifies which card in the top row is selected.
 * Execution and validation are handled by the current game state.
 * </p>
 */
public class DrawCardFromTopAction implements Action {
    private final int pos;

    @JsonCreator
    public DrawCardFromTopAction(@JsonProperty("pos") int pos) {
        this.pos = pos;
    }

    @JsonProperty("pos")
    public int getPos() {
        return pos;
    }

    /**
     * Executes the action for the given player, drawing a card
     * from the top row at the specified position.
     *
     * @param player the player performing the action
     * @throws IllegalActionException if the selected position is invalid
     *                                in the current game state
     * @throws IOException if an I/O error occurs during execution
     */
    @Override
    public void execute(Player player) throws IllegalActionException, IOException {
        player.getGame().getState().drawCardFromTop(player, pos);
    }

}
