package it.polimi.ingsw.networking.TCP;

import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.Client;
import it.polimi.ingsw.networking.UIObserver;

import java.rmi.RemoteException;

public class ClientTCP implements Client {
    private String nickname;
    private UIObserver observer;

    public ClientTCP(UIObserver observer) {
        this.observer = observer;
    }

    @Override
    public void update(GameDTO game) throws RemoteException, IllegalActionException {
        if (game != null) {
            observer.update(game);
        }
    }

    @Override
    public int addUser(String password, String username) throws RemoteException {
        return 0;
    }

    @Override
    public boolean joinGame() throws IllegalActionException, RemoteException {
        return false;
    }

    @Override
    public void createGame(int num) throws IllegalActionException, RemoteException {

    }

    @Override
    public void executeAction(Action action) throws IllegalActionException, RemoteException {

    }

    @Override
    public String getNickname() {
        return nickname;
    }
}
