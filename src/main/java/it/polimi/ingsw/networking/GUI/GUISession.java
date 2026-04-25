package it.polimi.ingsw.networking.GUI;

import it.polimi.ingsw.controller.states.GameState;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.networking.Client;
import it.polimi.ingsw.networking.UIObserver;

public class GUISession {
    private static int portRMI;
    private static int portTCP;
    private static String addr;
    private static Client client;
    private static UIObserver observer;
    private static GameDTO game;

    public static Client getClient() {
        return client;
    }

    public static void setClient(Client client) {
        GUISession.client = client;
    }

    public static int getPortRMI() {
        return portRMI;
    }

    public static void setPortRMI(int portRMI) {
        GUISession.portRMI = portRMI;
    }

    public static int getPortTCP() {
        return portTCP;
    }

    public static void setPortTCP(int portTCP) {
        GUISession.portTCP = portTCP;
    }

    public static String getAddr() {
        return addr;
    }

    public static void setAddr(String addr) {
        GUISession.addr = addr;
    }

    public static UIObserver getObserver() {
        return observer;
    }

    public static void setObserver(UIObserver observer) {
        GUISession.observer = observer;
    }

    public static GameDTO getGame() {
        return game;
    }

    public static void setGame(GameDTO game) {
        GUISession.game = game;
    }
}
