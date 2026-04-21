package it.polimi.ingsw.networking.TCP;

import it.polimi.ingsw.controller.GameController;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class ServerThread implements Runnable {
    private Socket client;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    private GameController gameController;
    private Map<String, String> nicknames;

    public ServerThread(Socket client, GameController game, Map<String, String> nicknames) {
        this.gameController = game;
        this.nicknames = nicknames;
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
        if (nicknames.containsKey(nickname)) {
            if (nicknames.get(nickname).equals(psw)) {
                message = "Welcome back " + nickname;
                success = 0;
            } else {
                message = "Nickname already exists or wrong password";
                success = -1;
            }
        } else {
            nicknames.put(nickname, psw);
            message = "Welcome " + nickname;
            success = 0;
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
