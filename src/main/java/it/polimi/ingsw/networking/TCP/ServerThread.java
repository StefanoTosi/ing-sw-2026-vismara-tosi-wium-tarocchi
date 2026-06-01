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

/**
 * Handles a single TCP client connection.<br>
 *<br>
 * This class is responsible for:<br>
 *    - Receiving and parsing client requests<br>
 *    - Executing game operations through GameController<br>
 *    - Sending responses back to the client<br>
 *    - Managing user session state (login, join, disconnect)<br>
 *    - Monitoring client timeout (heartbeat via ping)<br>
 *<br>
 * Each client runs on its own thread.
 */
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


    /**
     * Creates a new server-side handler for a TCP client.
     *
     * @param client client socket
     * @param game game controller
     * @param users shared user map
     * @param lock synchronization lock
     */
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

    /**
     * Monitors client inactivity.<br>
     *<br>
     * If no ping is received for more than 10 seconds:<br>
     *    - User is marked inactive<br>
     *    - Game session is closed<br>
     *    - Socket is closed
     */
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

    /**
     * Main loop that receives and processes client messages.<br>
     *<br>
     * It deserializes JSON messages and dispatches them to
     * the correct handler based on RequestType.
     */
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

    /** Closes current game session for this user */
    private void closeGame() throws IllegalActionException, IOException, ClassNotFoundException, InterruptedException {
        gameController.removeGame(getNickname());
    }

    /**
     * Handles user login/registration.<br>
     *<br>
     * Sends back:<br>
     * - success flag<br>
     * - message explaining result
     */
    private void addUser(String psw, String nickname) throws Exception {
        boolean success = false;
        String message;
        User user;

        synchronized (lock) {
            if (users.containsKey(nickname)) {
                user = users.get(nickname);
                if(user.getPassword() == null) {
                    user.setPassword(psw);
                }
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

    /**
     * Adds player to a game or reconnects if already in match.<br>
     *
     * Sends back join result to client.
     */
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
        ObjectNode payload = mapper.createObjectNode();
        payload.put("result", result);

        Message response = new Message(RequestType.JOINGAME, payload);
        sendResponse(response);
    }

    /**
     * Create a game with the numbers of players specified
     * @param num numbers of players
     * @param name nickname of the match's creator
     * @throws IllegalActionException
     * @throws RemoteException
     */
    private void createGame(int num, String name) throws IllegalActionException, RemoteException {
        User user;
        synchronized (lock){
            user = users.get(name);
            user.setInGame(true);
        }
        gameController.createGameTCP(new Player(name), num, this);
    }

    /**
     * Executes a player action in the game.<br>
     *
     * Any exception is captured and sent back as error message.
     */
    private void executeAction(Action action, String nickname) throws Exception {

        ObjectNode payload = mapper.createObjectNode();
        payload.put("error", "");
        try{
            gameController.executeAction(action, nickname);
        }catch(Exception e){
            payload.put("error", e.getMessage());
        }
        Message response = new Message(RequestType.EXECUTEACTION, payload);
        sendResponse(response);
    }

    private synchronized void sendResponse(Message response) throws Exception {
        String json = JsonUtil.toJson(response);
        out.println(json);
    }

    /**
     * Entry point of thread execution.<br>
     * Starts communication loop.
     */
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

    /**
     * Sends leaderboard data to client.
     */
    public void getLeaderboard() throws Exception {
        JsonNode payload = mapper.valueToTree(UserDAO.getLeaderBoard());
        Message response = new Message(RequestType.PRINTLEADERBOARD, payload);
        sendResponse(response);
    }
}
