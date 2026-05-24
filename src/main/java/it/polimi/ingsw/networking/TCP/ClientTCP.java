package it.polimi.ingsw.networking.TCP;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.Client;
import it.polimi.ingsw.networking.DB.LeaderboardDTO;
import it.polimi.ingsw.networking.JsonUtil;
import it.polimi.ingsw.networking.RMI.ClientCallBack;
import it.polimi.ingsw.networking.UIObserver;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * TCP client implementation for the game.
 *This class handles all communication with the TCP server using:
 *     Synchronous request/response (via blocking queue)
 *     Asynchronous server updates (via listener thread)
 *     JSON serialization/deserialization using Jackson
 *The client supports both:
 *     Game actions (create game, join, execute action, etc.)
 *     Server notifications (game updates, game closure)
 */
public class ClientTCP implements Client {
    private String nickname;
    private UIObserver observer;
    private String serverAddress;
    private int serverPort;
    private Socket mySocket;

    private BufferedReader in;
    private PrintWriter out;
    private final ObjectMapper mapper = new ObjectMapper();

    private final LinkedBlockingQueue<Message> responses = new LinkedBlockingQueue<>();

    /**
     * Creates a TCP client and immediately connects to the server.
     *
     * @param observer UI observer for game updates
     * @param port TCP server port
     * @param address server IP address
     */
    public ClientTCP(UIObserver observer, int port, String address) {
        this.observer = observer;
        this.serverAddress = address;
        this.serverPort = port;
        connect();
        startListener();
    }


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Starts a background thread that listens for server messages.
     *
     * Messages are classified into:
     *     Asynchronous updates (UPDATE, CLOSEGAME)
     *     Synchronous responses (added to blocking queue)
     */
    private void startListener(){
        new Thread(() -> {
            try{
                while(true) {
                    String json = in.readLine();
                    Message response = JsonUtil.fromJson(json, Message.class);
                    switch (response.getRequest()){
                        case RequestType.UPDATE:
                            update(mapper.treeToValue(response.getPayload(), GameDTO.class));
                            break;
                        case RequestType.CLOSEGAME:
                            closeGame(mapper.treeToValue(response.getPayload(), GameDTO.class));
                            break;
                        default:
                            responses.put(response);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                shutDownClient();
            }
        }).start();
    }

    /**
     * Opens a socket connection to the server and initializes I/O streams.
     */
    public void connect(){
        try {
            mySocket=new Socket(serverAddress, serverPort);

            //link to socket the object for reading/writing
            out = new PrintWriter(mySocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
        } catch (IOException ex) {
            System.err.println("Unknown host");
        }
    }

    /**
     * Sends a serialized request to the server.
     *
     * @param request message to send
     * @throws Exception serialization or network error
     */
    private synchronized void sendRequest(Message request) throws Exception {
        String json = JsonUtil.toJson(request);
        out.println(json);
    }

    @Override
    public void update(GameDTO game) throws IOException, IllegalActionException, InterruptedException {
        if(game != null){
            observer.update(game);
        }
    }

    /**
     * Notifies UI that the game has been closed by the server.
     */
    private void closeGame(GameDTO game) throws IllegalActionException, IOException, ClassNotFoundException, InterruptedException {
        observer.closingGame(game);
    }

    @Override
    public boolean addUser(String password, String username) {
        boolean result = false;
        try{
            ObjectNode payload = mapper.createObjectNode();
            payload.put("username", username);
            payload.put("password", password);
            Message request = new Message(RequestType.ADDUSER, payload);
            sendRequest(request);
            Message response = responses.take();
            if(response.getRequest().equals(RequestType.ADDUSER)){
                System.out.println(response.getPayload().get("msg"));
                result = response.getPayload().get("result").asBoolean();
                if(result){
                    setNickname(username);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean joinGame() {
        boolean result = false;
        try{
            ObjectNode payload = mapper.createObjectNode();
            payload.put("nickname", getNickname());
            Message request = new Message(RequestType.JOINGAME, payload);
            sendRequest(request);
            Message response = responses.take();
            if(response.getRequest().equals(RequestType.JOINGAME)){
                result = response.getPayload().get("result").asBoolean();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void createGame(int num) {
        try{
            ObjectNode payload = mapper.createObjectNode();
            payload.put("num", num);
            payload.put("nickname", getNickname());
            Message request = new Message(RequestType.CREATEGAME, payload);
            sendRequest(request);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void executeAction(Action action) throws Exception {
        try{
            JsonNode payload = mapper.valueToTree(action);
            ObjectNode root = mapper.createObjectNode();
            root.set("action", payload);
            root.put("nickname", getNickname());
            Message request = new Message(RequestType.EXECUTEACTION, root);
            sendRequest(request);
            Message response = responses.take();
            if(response.getRequest().equals(RequestType.EXECUTEACTION)){
                if(!response.getPayload().get("error").asText().equals("")){
                    throw new IllegalActionException(response.getPayload().get("error").asText());
                }
            }
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public void leaveMatch() {
        try{
            ObjectNode payload = mapper.createObjectNode();
            payload.put("nickname", getNickname());
            Message request = new Message(RequestType.LEAVEMATCH, payload);
            sendRequest(request);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void leaveGame() {
        try{
            ObjectNode payload = mapper.createObjectNode();
            payload.put("nickname", getNickname());
            Message request = new Message(RequestType.LEAVEGAME, payload);
            sendRequest(request);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void ping() {
        new Thread(() -> {
            while(true) {
                try{
                    ObjectNode payload = mapper.createObjectNode();
                    payload.put("nickname", getNickname());
                    Message request = new Message(RequestType.PING, payload);
                    sendRequest(request);
                    Thread.sleep(3000);
                }catch (Exception e){
                    shutDownClient();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void stopGame(String name) {
        try{
            ObjectNode payload = mapper.createObjectNode();
            Message request = new Message(RequestType.CLOSEGAME, payload);
            sendRequest(request);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void shutDownClient(){
        System.out.println("\nSorry the server crashed\n");
        try {
            mySocket.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        System.exit(1);
    }

    @Override
    public void setObserver(UIObserver observer) {
        this.observer = observer;
    }

    @Override
    public List<LeaderboardDTO> getLeaderboard() {
        List<LeaderboardDTO> result = new ArrayList<>();
        try{
            Message request = new Message(RequestType.PRINTLEADERBOARD, null);
            sendRequest(request);
            Message response = responses.take();
            if(response.getRequest().equals(RequestType.PRINTLEADERBOARD)){
                System.out.println(response.getPayload().toString());
                result = mapper.convertValue(
                        response.getPayload(),
                        mapper.getTypeFactory()
                                .constructCollectionType(List.class, LeaderboardDTO.class));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
