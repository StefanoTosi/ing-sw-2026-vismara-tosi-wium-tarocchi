package it.polimi.ingsw.networking.TCP;

import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.Client;
import it.polimi.ingsw.networking.UIObserver;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static it.polimi.ingsw.networking.JsonUtil.fromJson;

public class ClientTCP implements Client {
    private String nickname;
    private UIObserver observer;
    private final String serverAdress = "127.0.0.1";
    private final int serverPort = 1234;
    private Socket mySocket;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    private final Map<RequestType, Message> responses = new ConcurrentHashMap<>();

    public ClientTCP(UIObserver observer) {
        this.observer = observer;
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
                        update((GameDTO) fromJson((String)response.getParams()[0], GameDTO.class));
                    }else{
                        responses.put(response.getRequest(), response);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    public Message waitResponses(RequestType requestType) throws InterruptedException {
        while(!responses.containsKey(requestType)){
            Thread.sleep(50); //Brutal polling
        }
        return responses.remove(requestType);
    }

    public void connect(){
        try {
            mySocket=new Socket(serverAdress, serverPort);

            //link to sokcet the object for reading/wriding
            out = new ObjectOutputStream(mySocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(mySocket.getInputStream());
        } catch (IOException ex) {
            System.err.println("Host sconosciuto.");
        }
    }

    private void sendRequest(Message request) throws IOException {
        out.writeObject(request);
        out.flush();
    }

    @Override
    public void update(GameDTO game) throws IOException, IllegalActionException {
        if(game != null){
            observer.update(game);
        }
    }

    @Override
    public int addUser(String password, String username) throws IOException, ClassNotFoundException {
        int result = -1;
        try{
            Message request = new Message(RequestType.ADDUSER, password, username);
            sendRequest(request);
            Message response = waitResponses(RequestType.ADDUSER);
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
            Message response = waitResponses(RequestType.JOINGAME);
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
        Message request = new Message(RequestType.EXECUTEACTION, action, getNickname());
        sendRequest(request);
    }

    @Override
    public String getNickname() {
        return nickname;
    }
}
