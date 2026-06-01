package it.polimi.ingsw.controller.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import java.io.IOException;

/**
 * Action that allows a player to select one of the available offer tiles.<br>
 * The selected offer tile is identified by a character.
 */
public class ChooseOfferAction implements Action {
    private final char order;

    /**
     * Generates a new {@code ChooseOfferAction} object, identified by the specified character.
     * @param order the character identifying the chosen offer tile
     */
    @JsonCreator
    public ChooseOfferAction(@JsonProperty("order") char order) {
        this.order = order;
    }

    @JsonProperty("order")
    public char getOrder() {
        return order;
    }

    @Override
    public void execute(Player player) throws IllegalActionException, IOException {
        player.getGame().getState().chooseOffer(player, order);
    }
}