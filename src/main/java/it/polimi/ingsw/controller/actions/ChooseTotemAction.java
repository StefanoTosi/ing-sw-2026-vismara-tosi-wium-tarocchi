package it.polimi.ingsw.controller.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Totem;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.IOException;

/**
 * Action that allows a player to select one of the available totems.<br>
 * Each {@code Totem} is identified by its color.
 */
public class ChooseTotemAction implements Action {
    private final Totem totem;

    /**
     * Generates a new {@code ChooseTotemAction} object, identified by the totem's color.
     * @param totem the color identifying the chosen totem
     */
    @JsonCreator
    public ChooseTotemAction(@JsonProperty("totem") Totem totem) {
        this.totem = totem;
    }

    @JsonProperty("totem")
    public Totem getTotem() {
        return totem;
    }

    @Override
    public void execute(Player player) throws IllegalActionException, IOException {
        player.getGame().getState().chooseTotem(player, totem);
    }
}