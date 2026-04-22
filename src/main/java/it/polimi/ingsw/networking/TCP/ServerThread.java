package it.polimi.ingsw.networking.TCP;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.User;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Map;

import static it.polimi.ingsw.networking.JsonUtil.toJson;

public class ServerThread implements Runnable, ObserverTCP {
    private Socket client;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    private GameController gameController;
    private Map<String, User> users;
    private final Object lock;


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
    }

    private void comunicate(){
        try{
            while(true){
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
                    default:
                        break;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
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
                message = "Welcome " + nickname;
                success = 0;
            }
        }

        Message msg = new Message(RequestType.ADDUSER, message, success);
        out.writeObject(msg);
    }

    private void joinGame(String name) throws IllegalActionException, IOException {
        boolean result = gameController.joinGameTCP(new Player(name), this);
        Message msg = new Message(RequestType.JOINGAME, result);
        out.writeObject(msg);
    }

    private void createGame(int num, String name) throws IllegalActionException, RemoteException {
        gameController.createGameTCP(new Player(name), num, this);
    }

    private void executeAction(Action action, String nickname) throws IllegalActionException, RemoteException {
        gameController.executeAction(action, nickname);
    }

    @Override
    public void run() {
        comunicate();
    }

    @Override
    public void update(GameDTO game) throws Exception {
        Message response = new Message(RequestType.UPDATE, toJson(game));
        out.writeObject(response);
    }
}
