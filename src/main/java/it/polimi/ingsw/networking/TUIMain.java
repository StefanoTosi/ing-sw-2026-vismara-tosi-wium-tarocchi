package it.polimi.ingsw.networking;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;

public class TUIMain {
    private static int portRMI;
    private static int portTCP;
    private static String serverAddress;

    public static void main(String[] args) throws IOException, NotBoundException, IllegalActionException, ClassNotFoundException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File("src/main/resources/it/polimi/ingsw/config.json"));

        try{
            portRMI = root.get("port_rmi").asInt();
            portTCP = root.get("port_tcp").asInt();
            serverAddress = root.get("host").asText();
        }catch(Exception e){
            //fallback to localTest value
            portRMI = 1099;
            portTCP = 1234;
            serverAddress = "127.0.0.1";
            e.printStackTrace();
        }

        TUI tui = new TUI(portRMI, portTCP, serverAddress);
        tui.chooseTCPorRMI();
        tui.start();
    }
}
