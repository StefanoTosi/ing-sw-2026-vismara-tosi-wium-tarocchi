package it.polimi.ingsw.networking.RMI;
import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.DB.LeaderboardDTO;

import java.io.IOException;
import java.rmi.*;
import java.util.List;

public interface Controller extends Remote{
    boolean addUser(String psw, String nickname, ClientCallBack client) throws RemoteException;
    void ping(String name) throws RemoteException;
    void createGame(String name, int numPlayers) throws RemoteException, IllegalActionException;
    void leaveGame(String name) throws RemoteException;
    void leaveMatch(String name) throws IOException, IllegalActionException, ClassNotFoundException, InterruptedException;
    boolean joinGame(String name) throws IOException, IllegalActionException, InterruptedException;
    void executeAction(Action action, String nickname) throws IOException, IllegalActionException, InterruptedException;
    void stopGame(String name) throws IOException, IllegalActionException, ClassNotFoundException, InterruptedException;
    List<LeaderboardDTO> getLeaderboard()throws RemoteException;
}
