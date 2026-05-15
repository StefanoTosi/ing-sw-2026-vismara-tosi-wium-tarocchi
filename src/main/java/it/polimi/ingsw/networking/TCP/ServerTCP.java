package it.polimi.ingsw.networking.TCP;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.networking.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class ServerTCP {
    private int port;
    private ServerSocket serverSocket;
    private GameController gameController;
    private final Object lock;

    public ServerTCP(GameController gameController,  Object lock, int port) {
        this.gameController = gameController;
        this.lock = lock;
        this.port = port;
    }

    public void waitForConnection(Map<String, User> users) throws IOException {
        try {
            serverSocket = new ServerSocket(port);
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("Accepted connection");

                Thread th = new Thread(new ServerThread(socket, gameController, users, lock));
                th.start();
            }
        } catch(IOException e) {
            throw e;
        }
    }
}