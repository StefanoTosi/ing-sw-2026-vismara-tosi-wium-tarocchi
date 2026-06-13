package it.polimi.ingsw.networking;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.UI.UIMain;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.SaveGames;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.DB.DBConnection;
import it.polimi.ingsw.networking.DB.UserDAO;
import it.polimi.ingsw.networking.RMI.ServerRMI;
import it.polimi.ingsw.networking.TCP.ServerTCP;

import java.io.File;
import java.io.IOException;
import java.rmi.*;
import java.rmi.registry.*;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Entry point of the server application.<br>
 *<br>
 * This class is responsible for initializing the game controller,
 * loading configuration parameters, restoring persisted data,
 * and starting both RMI and TCP networking services.
 */
public class ServerMain {
    private static int portRMI;
    private static int portTCP;
    private static String host;

    /**
     * Main method that bootstraps the server.<br>
     *<br>
     * It performs the following steps:<br>
     *    - Loads configuration from JSON file<br>
     *    - Initializes database connection<br>
     *    - Loads persisted users and saved games<br>
     *    - Starts RMI registry and binds remote server<br>
     *    - Starts TCP server for socket-based clients
     *
     * @param args command-line arguments (not used)
     * @throws IOException if configuration or I/O operations fail
     * @throws AlreadyBoundException if the RMI name is already bound
     * @throws IllegalActionException if a game restoration action fails
     */
     static void main(String[] args) throws IOException, AlreadyBoundException, IllegalActionException {
        ObjectMapper mapper = new ObjectMapper();
         JsonNode root = mapper.readTree(ServerMain.class.getResource("/it/polimi/ingsw/config.json"));

        try{
            portRMI = root.get("port_rmi").asInt();
            portTCP = root.get("port_tcp").asInt();
            //host = root.get("host").asText();
        }catch(Exception e){
            portRMI = 1099;
            portTCP = 1234;
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Write the server Address");
        host = scanner.nextLine();

         System.out.println("Enter the host path of the DB (press enter for setting default localhost:3306)");
         DBConnection.setHOST(scanner.nextLine());
         System.out.println("Enter the user:");
         DBConnection.setUSER(scanner.nextLine());
         System.out.println("Enter the password:");
         DBConnection.setPASSWORD(scanner.nextLine());

        GameController gameController = new GameController();
        Map<String,User> users = new ConcurrentHashMap<>();
        Object lock = new Object();

        //create or connect to a DB if possible
        DBConnection.initializeDB();
        UserDAO.loadUser(users);

        //loading available saved games
        SaveGames.loadSaves(gameController, users);

        //RMI connection
        System.setProperty("java.rmi.server.hostname", host);
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
