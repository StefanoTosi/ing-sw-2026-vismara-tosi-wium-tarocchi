package it.polimi.ingsw.networking.RMI;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.User;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerRMI extends UnicastRemoteObject implements Controller {
    private GameController gamesController;
    private final Map<String, ClientCallBack> clients;
    private Map<String, User> users;
    private final Object lock;

    public ServerRMI(GameController game, Map<String, User> users, Object lock) throws RemoteException {
        clients = new ConcurrentHashMap<>();
        this.users = users;
        gamesController = game;
        this.lock = lock;
    }

    @Override
    public int addUser(String psw, String nickname, ClientCallBack client) throws RemoteException {
        boolean success;
        String message;
        User user;

        synchronized (lock) {
            if (users.containsKey(nickname)) {
                user = users.get(nickname);
                if (user.getPassword().equals(psw)) {
                    //check if the user is already logged in elsewhere
                    if(user.isActive()) {
                        message = "User " + nickname + " is already active elsewhere";
                        success = false;
                    } else {
                        user.setActive(true);
                        message = "Welcome back " + nickname;
                        success = true;
                    }
                } else {
                    message = "Nickname already exists or wrong password";
                    success = false;
                }
            } else {
                user = new User(nickname, psw);
                user.setActive(true);
                users.put(nickname, user);
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