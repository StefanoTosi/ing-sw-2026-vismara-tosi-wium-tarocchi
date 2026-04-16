package it.polimi.ingsw.networking.RMI;

import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallBack extends Remote {
    void receiveMessage(String message) throws RemoteException; // TODO: serve?
    void setNickname(String nickname) throws RemoteException;
    String getNickname() throws RemoteException;
    void update(GameDTO game) throws RemoteException, IllegalActionException;
}
