package it.polimi.ingsw.networking.RMI;

import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.Client;
import it.polimi.ingsw.networking.UIObserver;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;


public class ClientRMI extends UnicastRemoteObject implements ClientCallBack, Client {
    private static final int PORT = 1099;
    private String nickname;
    private final Controller controller;
    private UIObserver observer;

    public ClientRMI(UIObserver observer) throws RemoteException, NotBoundException {
        this.observer = observer;
        this.controller = connectToServer();
    }

    @Override
    public void receiveMessage(String message) throws RemoteException {
        System.out.println(message + "\n");
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    @Override
    public String getNickname(){
        return nickname;
    }

    public Controller connectToServer() throws RemoteException, NotBoundException {
        // Getting the registry
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", PORT);
        String remoteObjectName = "ServerRMI";

        return (Controller) registry.lookup(remoteObjectName);
    }

    @Override
    public void update(GameDTO game) throws RemoteException, IllegalActionException {
        if (game != null) {
            observer.update(game);
        }
    }

    @Override
    public int addUser(String password, String username) throws RemoteException {
        return controller.addUser(password, username, this);
    }

    @Override
    public boolean joinGame() throws IllegalActionException, RemoteException {
        return controller.joinGame(getNickname());
    }

    @Override
    public void createGame(int num) throws IllegalActionException, RemoteException {
        controller.createGame(getNickname(), num);
    }

    @Override
    public void executeAction(Action action) throws IllegalActionException, RemoteException {
        controller.executeAction(action, getNickname());
    }
}
