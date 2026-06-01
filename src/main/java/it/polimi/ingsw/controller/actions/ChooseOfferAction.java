package it.polimi.ingsw.controller.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import java.io.IOException;

/**
 * Action that allows a player to select one of the available offers.
 * <p>
 * The selected offer is identified by its order character and the
 * actual validation and execution are delegated to the current game state.
 * </p>
 */
public class ChooseOfferAction implements Action {
    private final char order;

    @JsonCreator
    public ChooseOfferAction(@JsonProperty("order") char order) {
        this.order = order;
    }

    @JsonProperty("order")
    public char getOrder() {
        return order;
    }

    /**
     * Executes the offer selection for the specified player.
     *
     * @param player the player performing the action
     * @throws IllegalActionException if the selected offer cannot be chosen
     * in the current game state
     * @throws IOException if an I/O error occurs during execution
     */
    @Override
    public void execute(Player player) throws IllegalActionException, IOException {
        player.getGame().getState().chooseOffer(player, order);
    }
}
