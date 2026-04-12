package it.polimi.ingsw.networking;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.controller.actions.ChooseOfferAction;
import it.polimi.ingsw.controller.actions.DrawCardFromBottomAction;
import it.polimi.ingsw.controller.states.ChooseOfferState;
import it.polimi.ingsw.model.Game;
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

    protected ServerRMI() throws RemoteException {
        clients = new ConcurrentHashMap<>();
        nicknames = new ConcurrentHashMap<>();
        gamesController = new GameController();
    }

    @Override
    public synchronized int addUser(String psw, String nickname, ClientCallBack client) throws RemoteException {
        if (nicknames.containsKey(nickname)) {
            if (nicknames.get(nickname).equals(psw)) {
                client.setNickname(nickname);
                clients.put(nickname, client);
                client.receiveMessage("Welcome back " + nickname);
                return 0;
            } else {
                client.receiveMessage("Nickname already exists or wrong password");
                return -1;
            }
        } else {
            nicknames.put(nickname, psw);
            client.setNickname(nickname);
            clients.put(nickname, client);
            client.receiveMessage("Welcome "+nickname);
            return 0;
        }
    }

    @Override
    public synchronized void createGame(String name, int numPlayers) throws RemoteException, IllegalActionException {
        ClientCallBack client = clients.get(name);
        gamesController.createGame(new Player(name), numPlayers, client);
    }

    @Override
    public void leaveGame() throws RemoteException {
    }

    @Override
    public synchronized boolean joinGame(String name) throws RemoteException, IllegalActionException {
        ClientCallBack client = clients.get(name);
        return gamesController.joinGame(new Player(name), client);
    }

    @Override
    public void executeAction(Action action, String nickname) throws RemoteException, IllegalActionException {
        gamesController.executeAction(action, nickname);
    }
}
