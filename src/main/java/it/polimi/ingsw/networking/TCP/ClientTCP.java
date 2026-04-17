package it.polimi.ingsw.networking.TCP;

import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.Client;
import it.polimi.ingsw.networking.UIObserver;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;

public class ClientTCP implements Client {
    private String nickname;
    private UIObserver observer;
    private final String serverAdress = "127.0.0.1";
    private final int serverPort = 1234;
    private Socket mySocket;

    private ObjectInputStream in;
    private ObjectOutputStream out;


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
                //TODO
                //Ascolta ogni risposta dal client ed esegue di conseguenza
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
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
    public void update(GameDTO game) throws RemoteException, IllegalActionException {
        //Do nothing
    }

    @Override
    public int addUser(String password, String username) throws IOException, ClassNotFoundException {
        Message request = new Message(RequestType.ADDUSER, password, username);
        sendRequest(request);
        Message response = (Message) in.readObject();
        System.out.println(response.getParams()[0]);
        return (int)response.getParams()[1];
    }

    @Override
    public boolean joinGame() throws IllegalActionException, IOException, ClassNotFoundException {
        Message request = new Message(RequestType.JOINGAME);
        sendRequest(request);
        return false;
    }

    @Override
    public void createGame(int num) throws IllegalActionException, IOException {
        Message request = new Message(RequestType.CREATEGAME, num);
        sendRequest(request);
    }

    @Override
    public void executeAction(Action action) throws IllegalActionException, IOException {
        Message request = new Message(RequestType.EXECUTEACTION, action);
        sendRequest(request);
    }

    @Override
    public String getNickname() {
        return nickname;
    }
}
