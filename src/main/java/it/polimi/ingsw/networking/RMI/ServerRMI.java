package it.polimi.ingsw.networking.RMI;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerRMI extends UnicastRemoteObject implements Controller {
    private GameController gamesController;
    private final Map<String, ClientCallBack> clients;
    private Map<String, String> nicknames;

    private final Object lock = new Object();

    public ServerRMI(GameController game) throws RemoteException {
        clients = new ConcurrentHashMap<>();
        nicknames = new ConcurrentHashMap<>();
        gamesController = game;
    }

    @Override
    public int addUser(String psw, String nickname, ClientCallBack client) throws RemoteException {
        boolean success;
        String message;

        synchronized (lock) {
            if (nicknames.containsKey(nickname)) {
                if (nicknames.get(nickname).equals(psw)) {
                    clients.put(nickname, client);
                    message = "Welcome back " + nickname;
                    success = true;
                } else {
                    message = "Nickname already exists or wrong password";
                    success = false;
                }
            } else {
                nicknames.put(nickname, psw);
                clients.put(nickname, client);
                message = "Welcome " + nickname;
                success = true;
            }
        }

        if(success){
            client.setNickname(nickname);
        }
        client.receiveMessage(message);

        return success? 0 : -1;
    }

    @Override
    public void createGame(String name, int numPlayers) throws RemoteException, IllegalActionException {
        ClientCallBack client;
        synchronized (lock) {
            client = clients.get(name);
        }
        gamesController.createGame(new Player(name), numPlayers, client);
    }

    @Override
    public void leaveGame() throws RemoteException {
    }

    @Override
    public boolean joinGame(String name) throws RemoteException, IllegalActionException {
        ClientCallBack client;
        synchronized (lock) {
            client = clients.get(name);
        }

        return gamesController.joinGame(new Player(name), client);
    }

    @Override
    public void executeAction(Action action, String nickname) throws RemoteException, IllegalActionException {
        gamesController.executeAction(action, nickname);
    }
}