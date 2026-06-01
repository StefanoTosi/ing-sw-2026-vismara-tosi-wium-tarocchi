package it.polimi.ingsw.controller.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import java.io.IOException;

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

    @Override
    public void execute(Player player) throws IllegalActionException, IOException {
        player.getGame().getState().chooseOffer(player, order);
    }
}
