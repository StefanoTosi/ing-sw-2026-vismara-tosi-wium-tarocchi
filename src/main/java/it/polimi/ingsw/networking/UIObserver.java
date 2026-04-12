package it.polimi.ingsw.networking;

import it.polimi.ingsw.controller.states.GameState;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.RemoteException;

public interface UIObserver {
    void update(GameDTO game) throws RemoteException, IllegalActionException;
}
