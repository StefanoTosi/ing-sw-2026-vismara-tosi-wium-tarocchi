package it.polimi.ingsw.networking;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallBack extends Remote {
    void receiveMessage(String message) throws RemoteException;
    void setNickname(String nickname) throws RemoteException;
    String getNickname() throws RemoteException;
    void update(String msg) throws RemoteException, IllegalActionException;
}
