package it.polimi.ingsw.networking;

import it.polimi.ingsw.model.Game;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;


public class ClientRMI extends UnicastRemoteObject implements ClientCallBack, GameObserver{
    private static final int PORT = 1099;
    private String nickname;
    private Game game;

    protected ClientRMI() throws RemoteException {
        this.game = null;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void receiveMessage(String message) throws RemoteException {
        System.out.println("Received message: " + message + "\n");
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

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
    public void update(Game game) {
        this.game = game;
    }
}
