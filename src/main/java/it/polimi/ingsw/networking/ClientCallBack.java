package it.polimi.ingsw.networking;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallBack extends Remote {
    void receiveMessage(String message) throws RemoteException;
}
