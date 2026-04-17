package it.polimi.ingsw.networking;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.networking.RMI.ServerRMI;
import it.polimi.ingsw.networking.TCP.ServerTCP;

import java.io.IOException;
import java.rmi.*;
import java.rmi.registry.*;

public class ServerMain {
    //default number of RMI Registry port
    private static final int PORT = 1099;

    public static void main(String[] args) throws IOException, AlreadyBoundException{
        GameController gameController = new GameController();


        //RMI connection
        ServerRMI server = new ServerRMI(gameController);

        //Bind remote object's stub in the registry
        Registry registry = LocateRegistry.createRegistry(PORT);
        registry.bind("ServerRMI", server);
        System.out.println("ServerRMI ready");

        //Socket connection
        ServerTCP serverTCP = new ServerTCP(gameController);
        serverTCP.waitForConnection();
    }
}
