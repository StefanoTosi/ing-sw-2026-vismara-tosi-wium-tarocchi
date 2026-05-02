package it.polimi.ingsw.networking.TCP;

import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.Client;
import it.polimi.ingsw.networking.JsonUtil;
import it.polimi.ingsw.networking.UIObserver;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientTCP implements Client {
    private String nickname;
    private UIObserver observer;
    private String serverAdress;
    private int serverPort;
    private Socket mySocket;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    private final LinkedBlockingQueue<Message> responses = new LinkedBlockingQueue<>();

    public ClientTCP(UIObserver observer, int port, String address) {
        this.observer = observer;
        this.serverAdress = address;
        this.serverPort = port;
        connect();
        startListener();
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private void startListener(){
        new Thread(() -> {
            try{
                while(true) {
                    Message response = (Message) in.readObject();
                    if(response.getRequest().equals(RequestType.UPDATE)){
                        update((GameDTO) JsonUtil.fromJson((String)response.getParams()[0], GameDTO.class));
                    }else{
                        if(response.getRequest().equals(RequestType.CLOSEGAME)){
                            closeGame((GameDTO) JsonUtil.fromJson((String)response.getParams()[0], GameDTO.class));
                        }else{
                            responses.put(response);
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    public void connect(){
        try {
            mySocket=new Socket(serverAdress, serverPort);

            //link to socket the object for reading/wriding
            out = new ObjectOutputStream(mySocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(mySocket.getInputStream());
        } catch (IOException ex) {
            System.err.println("Host sconosciuto.");
        }
    }

    private synchronized void sendRequest(Message request) throws IOException {
        out.writeObject(request);
        out.flush();
    }

    @Override
    public void update(GameDTO game) throws IOException, IllegalActionException, InterruptedException {
        if(game != null){
            observer.update(game);
        }
    }

    private void closeGame(GameDTO game) throws IllegalActionException, IOException, ClassNotFoundException, InterruptedException {
        observer.closingGame(game);
    }

    @Override
    public int addUser(String password, String username) throws IOException, ClassNotFoundException {
        int result = -1;
        try{
            Message request = new Message(RequestType.ADDUSER, password, username);
            sendRequest(request);
            Message response = responses.take();
            if(response.getRequest().equals(RequestType.ADDUSER)){
                System.out.println(response.getParams()[0]);
                result = (int)response.getParams()[1];
                if(result == 0){
                    setNickname(username);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean joinGame() throws IllegalActionException, IOException, ClassNotFoundException {
        boolean result = false;
        try{
            Message request = new Message(RequestType.JOINGAME, getNickname());
            sendRequest(request);
            Message response = responses.take();
            if(response.getRequest().equals(RequestType.JOINGAME)){
                result = (boolean)response.getParams()[0];
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void createGame(int num) throws IllegalActionException, IOException {
        try{
            Message request = new Message(RequestType.CREATEGAME, num, getNickname());
            sendRequest(request);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void executeAction(Action action) throws IllegalActionException, IOException {
        try{
            Message request = new Message(RequestType.EXECUTEACTION, action, getNickname());
            sendRequest(request);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public void leaveMatch() throws InterruptedException {
        try{
            Message request = new Message(RequestType.LEAVEMATCH, getNickname());
            sendRequest(request);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void leaveGame() {
        try{
            Message request = new Message(RequestType.LEAVEGAME, getNickname());
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
                    Message request = new Message(RequestType.PING, getNickname());
                    sendRequest(request);
                    Thread.sleep(3000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
