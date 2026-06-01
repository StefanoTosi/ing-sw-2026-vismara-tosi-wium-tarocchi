package it.polimi.ingsw.controller.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Totem;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Action that allows a player to choose a totem.
 * <p>
 * The selected totem is stored within the action and applied to the game
 * through the current game state when the action is executed.
 * </p>
 */
public class ChooseTotemAction implements Action {
    private final Totem totem;

    @JsonCreator
    public ChooseTotemAction(@JsonProperty("totem") Totem totem) {
        this.totem = totem;
    }

    @JsonProperty("totem")
    public Totem getTotem() {
        return totem;
    }

    /**
     * Executes the totem selection for the specified player.
     *
     * @param player the player performing the action
     * @throws IllegalActionException if the selected totem cannot be chosen
     *                                in the current game state
     * @throws IOException if an I/O error occurs during execution
     */
    @Override
    public void execute(Player player) throws IllegalActionException, IOException {
        player.getGame().getState().chooseTotem(player, totem);
    }
}
