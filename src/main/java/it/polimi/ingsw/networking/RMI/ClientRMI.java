package it.polimi.ingsw.networking.RMI;

import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.Client;
import it.polimi.ingsw.networking.DB.LeaderboardDTO;
import it.polimi.ingsw.networking.UIObserver;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * RMI client implementation.
 * This class acts as:
 *     Client-side proxy to communicate with the RMI server
 *     Remote callback endpoint (implements {@link ClientCallBack})
 * It receives updates from the server and forwards them to the UI layer
 * via {@link UIObserver}.
 */
public class ClientRMI extends UnicastRemoteObject implements ClientCallBack, Client {
    private static int port;
    private static String serverAddress;
    private String nickname;
    private final Controller controller;
    private UIObserver observer;

    /**
     * Creates an RMI client and connects to the server registry.
     *
     * @param observer UI observer for updates
     * @param port RMI registry port
     * @param address server IP address
     * @throws RemoteException if RMI export fails
     * @throws NotBoundException if server object is not found in registry
     */
    public ClientRMI(UIObserver observer, int port, String address) throws RemoteException, NotBoundException {
        this.observer = observer;
        this.port = port;
        this.serverAddress = address;
        this.controller = connectToServer();
    }

    /**
     * Periodically sends heartbeat messages to the server.
     * If the server becomes unreachable, the UI is notified.
     */
    public void ping(){
        new Thread(() -> {
            while(true) {
                try {
                    controller.ping(getNickname());
                    Thread.sleep(3000);
                } catch (Exception e) {
                    try {
                        observer.serverCrashed();
                    } catch (IOException | IllegalActionException | InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
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

    /**
     * Sets the nickname assigned by the server.
     */
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

    /**
     * Connects to the RMI registry and retrieves the server stub.
     *
     * @return remote Controller stub
     */
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
        observer.closingGame(game);
    }

    @Override
    public boolean addUser(String password, String username) throws RemoteException {
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

    @Override
    public List<LeaderboardDTO> getLeaderboard() throws RemoteException {
        return controller.getLeaderboard();
    }
}
