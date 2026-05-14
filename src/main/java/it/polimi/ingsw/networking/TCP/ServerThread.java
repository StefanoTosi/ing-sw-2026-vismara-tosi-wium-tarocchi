package it.polimi.ingsw.networking.TCP;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.DB.UserDAO;
import it.polimi.ingsw.networking.JsonUtil;
import it.polimi.ingsw.networking.User;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Map;

public class ServerThread implements Runnable, ObserverTCP {
    private Socket client;

    private BufferedReader in;
    private PrintWriter out;
    private final ObjectMapper mapper = new ObjectMapper();

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
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
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
                        System.out.println("Connection successfully closed " + getNickname());
                        try {
                            leaveMatch(getNickname());
                            closeGame();
                            this.clientOn = false;
                            client.close();
                        } catch (IllegalActionException | IOException | ClassNotFoundException | InterruptedException e) {
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
                String json =  in.readLine();
                Message req = JsonUtil.fromJson(json, Message.class);
                switch (req.getRequest()){
                    case ADDUSER:
                        addUser(req.getPayload().get("password").asText(), req.getPayload().get("username").asText());
                        break;
                    case JOINGAME:
                        joinGame(req.getPayload().get("nickname").asText());
                        break;
                    case CREATEGAME:
                        createGame(req.getPayload().get("num").asInt(), req.getPayload().get("nickname").asText());
                        break;
                    case EXECUTEACTION:
                        JsonNode payload = req.getPayload();
                        executeAction(mapper.treeToValue(payload.get("action"), Action.class), payload.get("nickname").asText());
                        break;
                    case LEAVEMATCH:
                        leaveMatch(req.getPayload().get("nickname").asText());
                        break;
                    case LEAVEGAME:
                        leaveGame(req.getPayload().get("nickname").asText());
                        break;
                    case PING:
                        lastSeen = System.currentTimeMillis();
                        break;
                    case CLOSEGAME:
                        closeGame();
                        break;
                    case PRINTLEADERBOARD:
                        getLeaderboard();
                        break;
                    default:
                        break;
                }
            }
        }catch(Exception e){
            System.out.println("Not receiving messages from " + getNickname());
            e.printStackTrace();
        }
    }

    private void closeGame() throws IllegalActionException, IOException, ClassNotFoundException, InterruptedException {
        gameController.removeGame(getNickname());
    }

    private void addUser(String psw, String nickname) throws IOException {
        boolean success = false;
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
                        message = "Welcome back " + nickname;
                        setNickname(nickname);
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
                UserDAO.addUsers(user);
                setNickname(nickname);
                message = "Welcome " + nickname;
                success = true;
            }
        }

        ObjectNode payload = mapper.createObjectNode();
        payload.put("result", success);
        payload.put("msg", message);

        Message response = new Message(RequestType.ADDUSER, payload);
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
        result = gameController.joinGameTCP(new Player(name), this);
        user.setInGame(true);

        ObjectNode payload = mapper.createObjectNode();
        payload.put("result", result);

        Message response = new Message(RequestType.JOINGAME, payload);
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
        String json = mapper.writeValueAsString(response);
        out.println(json);
    }

    @Override
    public void run() {
        comunicate();
    }

    @Override
    public void update(GameDTO game) throws Exception {
        JsonNode payload = mapper.valueToTree(game);
        Message response = new Message(RequestType.UPDATE, payload);
        sendResponse(response);
    }

    @Override
    public void closingGame(GameDTO game) throws Exception {
        JsonNode payload = mapper.valueToTree(game);
        Message response = new Message(RequestType.CLOSEGAME, payload);
        sendResponse(response);
    }

    public void getLeaderboard() throws IOException {
        JsonNode payload = mapper.valueToTree(UserDAO.getLeaderBoard());
        Message response = new Message(RequestType.PRINTLEADERBOARD, payload);
        sendResponse(response);
    }
}
