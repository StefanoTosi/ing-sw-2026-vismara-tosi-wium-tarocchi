package it.polimi.ingsw.networking.TCP;

import it.polimi.ingsw.controller.GameController;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerThread implements Runnable {
    private Socket client;

    private BufferedReader in;
    private DataOutputStream out;

    private static GameController gameController;

    public ServerThread(Socket client, GameController game) {
        this.gameController = game;
        try{
            this.client = client;
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new DataOutputStream(client.getOutputStream());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

    }
}
