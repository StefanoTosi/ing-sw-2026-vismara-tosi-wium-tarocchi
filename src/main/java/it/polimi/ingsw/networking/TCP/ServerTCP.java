package it.polimi.ingsw.networking.TCP;

import it.polimi.ingsw.controller.GameController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class ServerTCP {
    private int port = 1234;
    private ServerSocket serverSocket;
    private GameController gameController;

    public ServerTCP(GameController gameController) {
        this.gameController = gameController;
    }

    public void waitForConnection(Map<String, String> nicknames) throws IOException {
        try{
            serverSocket = new ServerSocket(port);
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("Accepted connection");

                Thread th = new Thread(new ServerThread(socket, gameController, nicknames));
                th.start();
            }
        }catch(IOException e){
            throw e;
        }
    }
}
