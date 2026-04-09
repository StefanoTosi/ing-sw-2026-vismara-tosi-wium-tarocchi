package it.polimi.ingsw.networking;
import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.*;

public interface Controller extends Remote{
    public int addUser(String psw, String nickname, ClientRMI client) throws RemoteException;
    public void createGame(ClientRMI client, int numPlayers) throws RemoteException, IllegalActionException;
    public void leaveGame() throws RemoteException;
    public boolean joinGame(ClientRMI client) throws RemoteException, IllegalActionException;
    public void executeAction(Action action) throws RemoteException;
    public void test() throws RemoteException;
}
