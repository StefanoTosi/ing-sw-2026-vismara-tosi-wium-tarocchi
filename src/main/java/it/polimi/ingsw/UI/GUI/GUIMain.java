package it.polimi.ingsw.UI.GUI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.UI.UISession;
import javafx.application.Application;

import java.io.File;
import java.io.IOException;

public class GUIMain {
    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File("src/main/resources/it/polimi/ingsw/config.json"));

        try {
            UISession.setPortRMI(root.get("port_rmi").asInt());
            UISession.setPortTCP(root.get("port_tcp").asInt());
            UISession.setAddr(root.get("host").asText());
        } catch(Exception e) {
            // Fallback to local test values
            UISession.setPortRMI(1099);
            UISession.setPortTCP(1234);
            UISession.setAddr("127.0.0.1");
            e.printStackTrace();
        }

        Application.launch(GUIApplication.class, args);
    }
}
