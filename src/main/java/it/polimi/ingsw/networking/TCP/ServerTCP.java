package it.polimi.ingsw.networking.TCP;

import it.polimi.ingsw.controller.GameController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCP {
    private int port = 1234;
    private ServerSocket serverSocket;
    private GameController gameController;

    public ServerTCP(GameController gameController) {
        this.gameController = gameController;
    }

    public void waitForConnection() throws IOException {
        try{
            serverSocket = new ServerSocket(port);
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("Accepted connection");

                Thread th = new Thread(new ServerThread(socket, gameController));
                th.start();
            }
        }catch(IOException e){
            throw e;
        }
    }
}
