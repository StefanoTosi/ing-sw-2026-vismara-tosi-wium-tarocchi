package it.polimi.ingsw.networking;

import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.DB.LeaderboardDTO;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

public interface Client {
    public void update(GameDTO game) throws IOException, IllegalActionException, InterruptedException;
    public int addUser(String password, String username) throws IOException, ClassNotFoundException;
    public boolean joinGame() throws IllegalActionException, IOException, ClassNotFoundException, InterruptedException;
    public void createGame(int num) throws IllegalActionException, IOException;
    public void executeAction(Action action) throws IllegalActionException, IOException, InterruptedException;
    public String getNickname();
    public void leaveMatch() throws IOException, InterruptedException, IllegalActionException, ClassNotFoundException;
    public void leaveGame() throws RemoteException;
    public void ping();
    public void stopGame(String name) throws IllegalActionException, IOException, ClassNotFoundException, InterruptedException;
    public void setObserver(UIObserver observer);
    public List<LeaderboardDTO> getLeaderboard() throws RemoteException;
}
