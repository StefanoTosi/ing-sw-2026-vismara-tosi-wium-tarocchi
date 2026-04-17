package it.polimi.ingsw.networking.TCP;

import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.Client;
import it.polimi.ingsw.networking.UIObserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

public class ClientTCP implements Client {
    private String nickname;
    private UIObserver observer;
    private final String serverAdress = "127.0.0.1";
    private final int serverPort = 1234;
    private Socket mySocket;

    private BufferedReader in;
    private DataOutputStream out;


    public ClientTCP(UIObserver observer) {
        this.observer = observer;
        connect();
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void connect(){
        try {
            mySocket=new Socket(serverAdress, serverPort);

            //link to sokcet the object for reading/wriding
            in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            out = new DataOutputStream(mySocket.getOutputStream());
        } catch (IOException ex) {
            System.err.println("Host sconosciuto.");
        }
    }

    @Override
    public void update(GameDTO game) throws RemoteException, IllegalActionException {
        if (game != null) {
            observer.update(game);
        }
    }

    @Override
    public int addUser(String password, String username) throws RemoteException {
        return 0;
    }

    @Override
    public boolean joinGame() throws IllegalActionException, RemoteException {
        return false;
    }

    @Override
    public void createGame(int num) throws IllegalActionException, RemoteException {

    }

    @Override
    public void executeAction(Action action) throws IllegalActionException, RemoteException {

    }

    @Override
    public String getNickname() {
        return nickname;
    }
}
