package it.polimi.ingsw.networking;

import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.DB.LeaderboardDTO;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

public interface Client {
    void update(GameDTO game) throws IOException, IllegalActionException, InterruptedException;
    boolean addUser(String password, String username) throws IOException, ClassNotFoundException;
    boolean joinGame() throws IllegalActionException, IOException, ClassNotFoundException, InterruptedException;
    void createGame(int num) throws IllegalActionException, IOException;
    void executeAction(Action action) throws IllegalActionException, IOException, InterruptedException;
    String getNickname();
    void leaveMatch() throws IOException, InterruptedException, IllegalActionException, ClassNotFoundException;
    void leaveGame() throws RemoteException;
    void ping();
    void stopGame(String name) throws IllegalActionException, IOException, ClassNotFoundException, InterruptedException;
    void setObserver(UIObserver observer);
    List<LeaderboardDTO> getLeaderboard() throws RemoteException;
}
