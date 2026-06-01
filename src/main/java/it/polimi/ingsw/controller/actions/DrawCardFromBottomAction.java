package it.polimi.ingsw.controller.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.IOException;

/**
 * Action that allows a player to draw one of the available cards from the bottom row.<br>
 * The selected {@code Card} is identified by its position in the row on the board,
 * regardless of the list ({@code Character} or {@code Building}) it belongs to.
 */
public class DrawCardFromBottomAction implements Action {
    private final int pos;

    /**
     * Generates a new {@code DrawCardFromBottomAction} object, indicating the {@code Card}'s position on the board.
     * @param pos the card's absolute position in the row, regardless of {@code Character}/{@code Building} list division
     */
    @JsonCreator
    public DrawCardFromBottomAction(@JsonProperty("pos") int pos) {
        this.pos = pos;
    }

    @JsonProperty("pos")
    public int getPos() {
        return pos;
    }

    @Override
    public void execute(Player player) throws IllegalActionException, IOException {
        player.getGame().getState().drawCardFromBottom(player, pos);
    }
}