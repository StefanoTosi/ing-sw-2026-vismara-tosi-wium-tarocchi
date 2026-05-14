package it.polimi.ingsw.controller.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Totem;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.RemoteException;

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

    @Override
    public void execute(Player player) throws IllegalActionException, RemoteException {
        player.getGame().getState().chooseTotem(player, totem);
    }
}
