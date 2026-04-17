package it.polimi.ingsw.networking;

import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.IOException;
import java.rmi.RemoteException;

public interface Client {
    public void update(GameDTO game) throws IOException, IllegalActionException;
    public int addUser(String password, String username) throws IOException, ClassNotFoundException;
    public boolean joinGame() throws IllegalActionException, IOException, ClassNotFoundException;
    public void createGame(int num) throws IllegalActionException, IOException;
    public void executeAction(Action action) throws IllegalActionException, IOException;
    public String getNickname();
}
