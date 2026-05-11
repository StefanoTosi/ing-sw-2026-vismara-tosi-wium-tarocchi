package it.polimi.ingsw.networking.RMI;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.DB.LeaderboardDTO;
import it.polimi.ingsw.networking.DB.UserDAO;
import it.polimi.ingsw.networking.User;

import java.io.IOException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerRMI extends UnicastRemoteObject implements Controller {
    private GameController gamesController;
    private final Map<String, ClientCallBack> clients;
    private Map<String, User> users;
    private ConcurrentHashMap<String, Long> lastSeen = new ConcurrentHashMap<>();
    private final Object lock;

    public ServerRMI(GameController game, Map<String, User> users, Object lock) throws RemoteException {
        clients = new ConcurrentHashMap<>();
        this.users = users;
        gamesController = game;
        this.lock = lock;
        checkTimeouts();
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
                        clients.put(nickname, client);
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
                UserDAO.addUsers(user);
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
    public synchronized void ping(String name) throws RemoteException {
        lastSeen.put(name, System.currentTimeMillis());
    }

    public void checkTimeouts(){
        new Thread(() -> {
            while(true){
                long now = System.currentTimeMillis();
                for(String name : lastSeen.keySet()){
                    if(now - lastSeen.get(name) > 10000){
                        users.get(name).setActive(false);
                        try {
                            System.out.println("Chiusa connessione con " + name);
                            lastSeen.remove(name);
                            leaveMatch(name);
                            clients.remove(name);
                            stopGame(name);
                        } catch (IllegalActionException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }).start();
    }

    @Override
    public void createGame(String name, int numPlayers) throws RemoteException, IllegalActionException {
        ClientCallBack client;
        synchronized (lock) {
            User user = users.get(name);
            user.setInGame(true);
            client = clients.get(name);
        }
        gamesController.createGameRMI(new Player(name), numPlayers, client);
    }

    @Override
    public void leaveGame(String name) throws RemoteException {
        synchronized (lock){
            User user = users.get(name);
            user.setActive(false);
            clients.remove(name);
        }
    }

    @Override
    public void leaveMatch(String name) throws IOException, IllegalActionException, ClassNotFoundException, InterruptedException {
        ClientCallBack client;
        synchronized (lock) {
            User user = users.get(name);
            user.setInGame(false);
            client = clients.get(name);
        }
        gamesController.leaveMatchRMI(name, client);
    }

    @Override
    public boolean joinGame(String name) throws IOException, IllegalActionException, InterruptedException {
        ClientCallBack client;
        boolean result = true;
        synchronized (lock){
            User user = users.get(name);
            client = clients.get(name);
            if(!user.getInGame()){
                user.setInGame(true);
                result = gamesController.joinGameRMI(new Player(name), client);
            }else{
                gamesController.reconnectGameRMI(name, client);
            }
        }
        return result;
    }

    @Override
    public void executeAction(Action action, String nickname) throws IOException, IllegalActionException, InterruptedException {
        gamesController.executeAction(action, nickname);
    }

    @Override
    public void stopGame(String name) throws IOException, IllegalActionException, ClassNotFoundException, InterruptedException {
        gamesController.removeGame(name);
    }

    @Override
    public List<LeaderboardDTO> getLeaderboard() {
        return UserDAO.printLeaderBoard();
    }
}