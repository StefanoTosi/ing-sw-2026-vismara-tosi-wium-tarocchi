package it.polimi.ingsw.networking;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerRMI extends UnicastRemoteObject implements Controller {
    private GameController game;
    private final Map<String, ClientRMIMain> clients = new ConcurrentHashMap<>();

    protected ServerRMI() throws RemoteException {
    }

    /**
     *
     * @param uuid
     * @param nickname
     * @param client
     * @return 0 client add with success
     * @return -1 nickname already in use
     * @return 1 error, you should never reach that point
     * @throws RemoteException
     */
    @Override
    public int addUser(String uuid, String nickname, ClientRMIMain client) throws RemoteException {
        if(clients.containsKey(uuid)){
            if(clients.containsValue(client)){
                if(!clients.get(uuid).getNickname().equals(nickname)){
                    client.receiveMessage("Invalid nickname");
                    return -1;
                }
            } else {
                client.setNickname(nickname);
                clients.put(uuid, client);
                return 0;
            }
        } else {
            if(!clients.containsValue(client)){
                client.setNickname(nickname);
                clients.put(uuid, client);
                return 0;
            }else{
                client.receiveMessage("Invalid nickname");
                return -1;
            }
        }
        //you should never reach this point
        return 1;
    }

    @Override
    public void createGame(int numPlayers) throws RemoteException {

    }

    @Override
    public void leaveGame() throws RemoteException {

    }

    @Override
    public boolean joinGame() throws RemoteException {
        return false;
    }

    @Override
    public void executeAction(Action action) throws RemoteException {
        game.executeAction(action);
    }

    @Override
    public void test(String testo) throws RemoteException {
        System.out.println("Siamo connesssi boyyyssss" + testo);
    }
}
