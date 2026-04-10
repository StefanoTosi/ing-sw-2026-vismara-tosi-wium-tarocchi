package it.polimi.ingsw.networking;

import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.RemoteException;

public interface UIObserver {
    void update() throws RemoteException, IllegalActionException;
}
