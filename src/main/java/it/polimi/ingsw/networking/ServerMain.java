package it.polimi.ingsw.networking;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.networking.RMI.ServerRMI;
import it.polimi.ingsw.networking.TCP.ServerTCP;

import java.io.File;
import java.io.IOException;
import java.rmi.*;
import java.rmi.registry.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerMain {
    private static int portRMI;
    private static int portTCP;

    public static void main(String[] args) throws IOException, AlreadyBoundException{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File("src/main/resources/it/polimi/ingsw/config.json"));

        try{
            portRMI = root.get("port_rmi").asInt();
            portTCP = root.get("port_tcp").asInt();
        }catch(Exception e){
            portRMI = 1099;
            portTCP = 1234;
            e.printStackTrace();
        }

        GameController gameController = new GameController();
        Map<String,User> users = new ConcurrentHashMap<>();
        Object lock = new Object(); //lista di lock per diverse funzioni?

        //RMI connection
        ServerRMI server = new ServerRMI(gameController, users, lock);

        //Bind remote object's stub in the registry
        Registry registry = LocateRegistry.createRegistry(portRMI);
        registry.bind("ServerRMI", server);
        System.out.println("ServerRMI ready");

        //Socket connection
        ServerTCP serverTCP = new ServerTCP(gameController, lock, portTCP);
        System.out.println("ServerTCP ready");
        serverTCP.waitForConnection(users);
    }
}
