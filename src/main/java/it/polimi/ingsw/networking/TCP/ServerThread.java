package it.polimi.ingsw.networking.TCP;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.networking.User;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class ServerThread implements Runnable {
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
        //TODO comunicazione con client
        try{
            while(true){
                Message req = (Message)in.readObject();
                switch (req.getRequest()){
                    case ADDUSER:
                        req.getParams();
                        addUser((String)req.getParams()[0], (String)req.getParams()[1]);
                        break;
                    case JOINGAME:
                        joinGame();
                        break;
                    case CREATEGAME:
                        createGame();
                        break;
                    case EXECUTEACTION:
                        executeAction();
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

    private void joinGame(){}

    private void createGame(){}

    private void executeAction(){}

    @Override
    public void run() {
        comunicate();
    }
}
