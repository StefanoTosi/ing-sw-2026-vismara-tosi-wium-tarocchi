package it.polimi.ingsw.UI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.UI.GUI.GUIApplication;
import it.polimi.ingsw.UI.TUI.TUI;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import javafx.application.Application;

import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Scanner;

public class UIMain {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(UIMain.class.getResource("/it/polimi/ingsw/config.json"));

        try {
            UISession.setPortRMI(root.get("port_rmi").asInt());
            UISession.setPortTCP(root.get("port_tcp").asInt());
            //UISession.setAddr(root.get("host").asText());
        } catch(Exception e) {
            // Fallback to local test values
            UISession.setPortRMI(1099);
            UISession.setPortTCP(1234);
            UISession.setAddr("127.0.0.1");
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Write the server Address");
        UISession.setAddr(scanner.nextLine());
        System.setProperty("java.rmi.server.hostname", UISession.getAddr());
        while (true) {
            System.out.print("Would you like to use the TUI [1] or GUI [2]: ");
            String in = scanner.nextLine();
            if (in.equals("1")) {
                TUI tui = new TUI(UISession.getPortRMI(), UISession.getPortTCP(), UISession.getAddr());
                tui.chooseTCPorRMI();
                tui.start();
                return;
            } else if (in.equals("2")) {
                Application.launch(GUIApplication.class, args);
                return;
            }
        }
    }
}
