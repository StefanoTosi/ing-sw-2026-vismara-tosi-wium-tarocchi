package it.polimi.ingsw.controller.actions;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface Action extends Serializable {
    void execute(Player player) throws IllegalActionException, RemoteException;
}
