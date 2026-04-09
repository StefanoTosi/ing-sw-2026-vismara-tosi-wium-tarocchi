package it.polimi.ingsw.networking;

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
    private final Map<String, ClientRMI> clients;
    private Map<String, String> nicknames;

    protected ServerRMI() throws RemoteException {
        clients = new ConcurrentHashMap<>();
        nicknames = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized int addUser(String psw, String nickname, ClientRMI client) throws RemoteException {
        client.receiveMessage("[ServerRMI] Adding user " + psw + " " + nickname);
        if(nicknames.containsKey(nickname)){
            if(nicknames.get(nickname).equals(psw)){
                client.setNickname(nickname);
                clients.put(nickname, client);
                client.receiveMessage("Welcome back " + nickname);
                return 0;
            }else{
                client.receiveMessage("Nickname already exists or wrong password");
                return -1;
            }
        }else{
            nicknames.put(nickname, psw);
            client.setNickname(nickname);
            clients.put(nickname, client);
            client.receiveMessage("Welcome "+nickname);
            return 0;
        }
    }

    @Override
    public synchronized void createGame(ClientRMI client, int numPlayers) throws RemoteException, IllegalActionException {
        gamesController.createGame(new Player(client.getNickname()), numPlayers, client);
    }

    @Override
    public void leaveGame() throws RemoteException {
    }

    @Override
    public synchronized boolean joinGame(ClientRMI client) throws RemoteException, IllegalActionException {
        gamesController.joinGame(new Player(client.getNickname()), client);
        return false;
    }

    @Override
    public void executeAction(Action action) throws RemoteException {
        gamesController.executeAction(action);
    }

    @Override
    public void test() throws RemoteException {
        System.out.println("[ServerRMI] test");
    }
}
