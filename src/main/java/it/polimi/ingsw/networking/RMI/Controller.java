package it.polimi.ingsw.networking.RMI;
import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.*;

public interface Controller extends Remote{
    public int addUser(String psw, String nickname, ClientCallBack client) throws RemoteException;
    public void createGame(String name, int numPlayers) throws RemoteException, IllegalActionException;
    public void leaveGame() throws RemoteException;
    public boolean joinGame(String name) throws RemoteException, IllegalActionException;
    public void executeAction(Action action, String nickname) throws RemoteException, IllegalActionException;
}
