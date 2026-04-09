package it.polimi.ingsw.networking;
import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.Player;

import java.rmi.*;

public interface Controller extends Remote{
    public int addUser(String uuid, String nickname, ClientRMIMain client) throws RemoteException;
    public void createGame(int numPlayers) throws RemoteException;
    public void leaveGame() throws RemoteException;
    public boolean joinGame(ClientRMIMain client)  throws RemoteException;
    public void executeAction(Action action) throws RemoteException;
}
