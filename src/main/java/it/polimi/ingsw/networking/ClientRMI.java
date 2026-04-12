package it.polimi.ingsw.networking;

import it.polimi.ingsw.controller.states.GameState;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;


public class ClientRMI extends UnicastRemoteObject implements ClientCallBack {
    private static final int PORT = 1099;
    private String nickname;
    private UIObserver  observer;

    protected ClientRMI(UIObserver observer) throws RemoteException {
        this.observer = observer;
    }

    @Override
    public void receiveMessage(String message) throws RemoteException {
        System.out.println(message + "\n");
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
    public void update(GameDTO game) throws RemoteException, IllegalActionException {
        if (game != null) {
            observer.update(game);
        }
    }
}
