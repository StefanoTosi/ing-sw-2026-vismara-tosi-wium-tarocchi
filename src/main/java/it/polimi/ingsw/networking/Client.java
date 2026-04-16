package it.polimi.ingsw.networking;

import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.RemoteException;

public interface Client {
    public void update(GameDTO game) throws RemoteException, IllegalActionException;
    public int addUser(String password, String username) throws RemoteException;
    public boolean joinGame() throws IllegalActionException, RemoteException;
    public void createGame(int num) throws IllegalActionException, RemoteException;
    public void executeAction(Action action) throws IllegalActionException, RemoteException;
    public String getNickname();
}
