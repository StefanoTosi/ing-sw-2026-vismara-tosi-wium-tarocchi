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
    private final Object lock;

    public ServerTCP(GameController gameController,  Object lock) {
        this.gameController = gameController;
        this.lock = lock;
    }

    public void waitForConnection(Map<String, String> nicknames) throws IOException {
        try{
            serverSocket = new ServerSocket(port);
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("Accepted connection");

                Thread th = new Thread(new ServerThread(socket, gameController, nicknames, lock));
                th.start();
            }
        }catch(IOException e){
            throw e;
        }
    }
}
