package it.polimi.ingsw.networking.RMI;

import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.Client;
import it.polimi.ingsw.networking.UIObserver;
import javafx.css.converter.LadderConverter;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;


public class ClientRMI extends UnicastRemoteObject implements ClientCallBack, Client {
    private static int port;
    private static String serverAddress;
    private String nickname;
    private final Controller controller;
    private UIObserver observer;

    public ClientRMI(UIObserver observer, int port, String address) throws RemoteException, NotBoundException {
        this.observer = observer;
        this.controller = connectToServer();
        this.port = port;
        this.serverAddress = address;
    }

    public void ping(){
        new Thread(() -> {
            while(true) {
                try {
                    controller.ping(getNickname());
                    Thread.sleep(3000);
                } catch (RemoteException e) {
                    System.out.println("\nSorry the server crashed\n");
                    System.exit(1);
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    System.out.println("\nSorry the server crashed\n");
                    System.exit(1);
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    @Override
    public void stopGame(String name) throws IllegalActionException, IOException, ClassNotFoundException, InterruptedException {
        controller.stopGame(name);
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

    @Override
    public void leaveMatch() throws IOException, IllegalActionException, ClassNotFoundException, InterruptedException {
        controller.leaveMatch(getNickname());
    }

    @Override
    public void leaveGame() throws RemoteException {
        controller.leaveGame(getNickname());
    }

    public Controller connectToServer() throws RemoteException, NotBoundException {
        // Getting the registry
        Registry registry = LocateRegistry.getRegistry(serverAddress, port);
        String remoteObjectName = "ServerRMI";

        return (Controller) registry.lookup(remoteObjectName);
    }

    @Override
    public void update(GameDTO game) throws IOException, IllegalActionException, InterruptedException {
        if (game != null) {
            observer.update(game);
        }
    }

    @Override
    public void closingGame(GameDTO game) throws IllegalActionException, IOException, ClassNotFoundException, InterruptedException {
        //System.out.println("\n");
        observer.closingGame(game);
    }

    @Override
    public int addUser(String password, String username) throws RemoteException {
        return controller.addUser(password, username, this);
    }

    @Override
    public boolean joinGame() throws IllegalActionException, IOException, InterruptedException {
        return controller.joinGame(getNickname());
    }

    @Override
    public void createGame(int num) throws IllegalActionException, RemoteException {
        controller.createGame(getNickname(), num);
    }

    @Override
    public void executeAction(Action action) throws IllegalActionException, IOException, InterruptedException {
        controller.executeAction(action, getNickname());
    }

    @Override
    public void setObserver(UIObserver observer) {
        this.observer = observer;
    }
}
