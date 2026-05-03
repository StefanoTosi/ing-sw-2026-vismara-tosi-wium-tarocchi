package it.polimi.ingsw.networking.TCP;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.JsonUtil;
import it.polimi.ingsw.networking.User;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerThread implements Runnable, ObserverTCP {
    private Socket client;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    private GameController gameController;
    private Map<String, User> users;
    private Long lastSeen;
    private final Object lock;
    private String nickname;
    private boolean clientOn;


    public ServerThread(Socket client, GameController game, Map<String, User> users, Object lock) {
        this.gameController = game;
        this.users = users;
        this.lock = lock;

        try{
            this.client = client;
            out = new ObjectOutputStream(client.getOutputStream());
            out.flush();
            in = new ObjectInputStream(client.getInputStream());
        }catch(Exception e){
            e.printStackTrace();
        }
        this.lastSeen = 0L;
        this.clientOn = true;
        checkTimeouts();
    }

    public void checkTimeouts(){
        new Thread(() -> {
            while(clientOn){
                long now = System.currentTimeMillis();
                    if(now - lastSeen > 10000 && lastSeen != 0){
                        users.get(getNickname()).setActive(false);
                        users.get(getNickname()).setInGame(false);
                        System.out.println("connessione chiusa con successo "+getNickname());
                        try {
                            leaveMatch(getNickname());
                            closeGame();
                            this.clientOn = false;
                            client.close();
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
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public String getNickname(){
        return this.nickname;
    }

    private void comunicate(){
        try{
            while(clientOn){
                Message req = (Message)in.readObject();
                switch (req.getRequest()){
                    case ADDUSER:
                        req.getParams();
                        addUser((String)req.getParams()[0], (String)req.getParams()[1]);
                        break;
                    case JOINGAME:
                        joinGame((String)req.getParams()[0]);
                        break;
                    case CREATEGAME:
                        createGame((int)req.getParams()[0], (String)req.getParams()[1]);
                        break;
                    case EXECUTEACTION:
                        executeAction((Action)req.getParams()[0], (String)req.getParams()[1]);
                        break;
                    case LEAVEMATCH:
                        leaveMatch((String)req.getParams()[0]);
                        break;
                    case LEAVEGAME:
                        leaveGame((String)req.getParams()[0]);
                        break;
                    case PING:
                        lastSeen = System.currentTimeMillis();
                        break;
                    case CLOSEGAME:
                        closeGame();
                        break;
                    default:
                        break;
                }
            }
        }catch(Exception e){
            System.out.println("Non sto ricevendo messaggi da " + getNickname());
            e.printStackTrace();
        }
    }

    private void closeGame() throws IllegalActionException, IOException, ClassNotFoundException, InterruptedException {
        gameController.removeGame(getNickname());
    }

    private void addUser(String psw, String nickname) throws IOException {
        int success = -1;
        String message;
        User user;

        synchronized (lock) {
            if (users.containsKey(nickname)) {
                user = users.get(nickname);
                if (user.getPassword().equals(psw)) {
                    //check if the user is already logged in elsewhere
                    if(user.isActive()) {
                        message = "User " + nickname + " is already active elsewhere";
                        success = -1;
                    } else {
                        user.setActive(true);
                        message = "Welcome back " + nickname;
                        setNickname(nickname);
                        success = 0;
                    }
                } else {
                    message = "Nickname already exists or wrong password";
                    success = -1;
                }
            } else {
                user = new User(nickname, psw);
                user.setActive(true);
                users.put(nickname, user);
                setNickname(nickname);
                message = "Welcome " + nickname;
                success = 0;
            }
        }

        Message response = new Message(RequestType.ADDUSER, message, success);
        sendResponse(response);
    }

    private void leaveMatch(String name) throws IOException, IllegalActionException, ClassNotFoundException, InterruptedException {
        synchronized (lock){
            User user = users.get(name);
            user.setInGame(false);
        }
        gameController.leaveMatchTCP(name, this);
    }

    private void leaveGame(String name){
        synchronized (lock){
            User user = users.get(name);
            user.setActive(false);
        }
    }

    private void joinGame(String name) throws Exception {
        User user;
        synchronized (lock){
            user = users.get(name);
        }
        boolean result = true;
        if(!user.getInGame()){
            result = gameController.joinGameTCP(new Player(name), this);
            user.setInGame(true);
        }else{
            gameController.reconnectGameTCP(name, this);
        }
        Message response = new Message(RequestType.JOINGAME, result);
        sendResponse(response);
    }

    private void createGame(int num, String name) throws IllegalActionException, RemoteException {
        User user;
        synchronized (lock){
            user = users.get(name);
            user.setInGame(true);
        }
        gameController.createGameTCP(new Player(name), num, this);
    }

    private void executeAction(Action action, String nickname) throws IllegalActionException, IOException, InterruptedException {
        gameController.executeAction(action, nickname);
    }

    private synchronized void sendResponse(Message response) throws IOException {
        out.writeObject(response);
        out.flush();
    }

    @Override
    public void run() {
        comunicate();
    }

    @Override
    public void update(GameDTO game) throws Exception {
        Message response = new Message(RequestType.UPDATE, JsonUtil.toJson(game));
        sendResponse(response);
    }

    @Override
    public void closingGame(GameDTO game) throws Exception {
        Message response = new Message(RequestType.CLOSEGAME, JsonUtil.toJson(game));
        sendResponse(response);
    }
}
