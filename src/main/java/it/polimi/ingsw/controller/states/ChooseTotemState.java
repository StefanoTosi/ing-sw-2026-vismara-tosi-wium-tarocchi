package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Totem;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ChooseTotemState extends GameState {
    private final Game game;
    private List<Player> chooseOrder;

    public ChooseTotemState(Game game) throws IllegalActionException, RemoteException {
        this.game = game;
        chooseOrder = game.getPlayers().stream().toList();

        game.setPlayerTurn(chooseOrder.removeFirst());
    }

    @Override
    public void chooseTotem(Player player, Totem totem) throws IllegalActionException, RemoteException {
    }

    @Override
    public StateDTO getStateDTO() throws IllegalActionException {
        return StateDTO.CHOOSETOTEM;
    }
}
