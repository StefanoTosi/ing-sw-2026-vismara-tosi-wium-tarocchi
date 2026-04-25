package it.polimi.ingsw.networking.GUI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;

import java.io.File;
import java.io.IOException;

public class GUIMain {
    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File("src/main/resources/it/polimi/ingsw/config.json"));

        try{
            GUISession.setPortRMI(root.get("port_rmi").asInt());
            GUISession.setPortTCP(root.get("port_tcp").asInt());
            GUISession.setAddr(root.get("host").asText());
        }catch(Exception e){
            // Fallback to local test values
            GUISession.setPortRMI(1099);
            GUISession.setPortTCP(1234);
            GUISession.setAddr("127.0.0.1");
            e.printStackTrace();
        }

        Application.launch(GUIApplication.class, args);
    }
}
