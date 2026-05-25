package it.polimi.ingsw.networking.TCP;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.networking.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * TCP server entry point.
 *
 * Responsible for:
 * - opening a ServerSocket
 * - accepting incoming client connections
 * - spawning a dedicated thread per client (ServerThread)
 *
 * This is a multi-threaded blocking TCP server model.
 */
public class ServerTCP {
    private int port;
    private ServerSocket serverSocket;
    private GameController gameController;
    private final Object lock;

    /**
     * Constructor.
     *
     * @param gameController shared game logic controller
     * @param lock global synchronization object
     * @param port TCP listening port
     */
    public ServerTCP(GameController gameController,  Object lock, int port) {
        this.gameController = gameController;
        this.lock = lock;
        this.port = port;
    }

    /**
     * Starts the TCP server and blocks waiting for connections.
     *
     * For each incoming connection:
     * - accepts socket
     * - prints debug message
     * - starts a new ServerThread to handle communication
     *
     * @param users shared map of registered users
     */
    public void waitForConnection(Map<String, User> users) throws IOException {
        try {
            serverSocket = new ServerSocket(port);
            while(true){
                // Blocking call: waits for a client connection
                Socket socket = serverSocket.accept();
                System.out.println("Accepted connection");

                // Each client gets its own thread (one-thread-per-client model)
                Thread th = new Thread(new ServerThread(socket, gameController, users, lock));
                th.start();
            }
        } catch(IOException e) {
            throw e;
        }
    }
}