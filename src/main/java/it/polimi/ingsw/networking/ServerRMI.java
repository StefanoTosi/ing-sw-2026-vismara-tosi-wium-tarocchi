package it.polimi.ingsw.networking;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.actions.Action;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerRMI extends UnicastRemoteObject implements Controller {
    private GameController gamesController;
    private final Map<String, ClientRMIMain> clients;
    private Map<String, String> nicknames;

    protected ServerRMI() throws RemoteException {
        clients = new ConcurrentHashMap<>();
        nicknames = new ConcurrentHashMap<>();
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
    public synchronized int addUser(String uuid, String nickname, ClientRMIMain client) throws RemoteException {
        ClientRMIMain exixstingClient = clients.get(uuid);

        //Already existing client
        if (exixstingClient != null) {
            String oldNickname = exixstingClient.getNickname();
            //same nickname
            if (oldNickname.equals(nickname)) {
                clients.put(uuid, client);
                return 0;
            }
            //already used nickname
            if (nicknames.containsKey(oldNickname)) {
                client.receiveMessage("Nickname already exists");
                return -1;
            }
            //valid nickname
            //oldNickname free again
            nicknames.remove(oldNickname);
            nicknames.put(nickname, uuid);
            client.setNickname(nickname);
            clients.put(uuid, client);
        }else{ //new client
            //already used nickname
            if (nicknames.containsKey(nickname)) {
                client.receiveMessage("Nickname already exists");
                return -1;
            }
            //valid nickname
            nicknames.put(nickname, uuid);
            client.setNickname(nickname);
            clients.put(uuid, client);
            return 0;
        }
        return 1;
    }

    @Override
    public void createGame(int numPlayers) throws RemoteException {

    }

    @Override
    public void leaveGame() throws RemoteException {

    }

    @Override
    public boolean joinGame(ClientRMIMain client) throws RemoteException {
        //TODO something in the gameController class
        return false;
    }

    @Override
    public void executeAction(Action action) throws RemoteException {
        gamesController.executeAction(action);
    }
}
