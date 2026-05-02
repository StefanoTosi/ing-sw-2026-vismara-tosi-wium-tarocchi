package it.polimi.ingsw.networking.RMI;
import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.IOException;
import java.rmi.*;

public interface Controller extends Remote{
    public int addUser(String psw, String nickname, ClientCallBack client) throws RemoteException;
    public void ping(String name) throws RemoteException;
    public void createGame(String name, int numPlayers) throws RemoteException, IllegalActionException;
    public void leaveGame(String name) throws RemoteException;
    public void leaveMatch(String name) throws RemoteException;
    public boolean joinGame(String name) throws IOException, IllegalActionException, InterruptedException;
    public void executeAction(Action action, String nickname) throws IOException, IllegalActionException, InterruptedException;
}
