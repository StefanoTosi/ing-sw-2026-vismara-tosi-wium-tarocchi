package it.polimi.ingsw.UI;

import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.networking.Client;
import it.polimi.ingsw.networking.UIObserver;

public class UISession {
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
        UISession.client = client;
    }

    public static int getPortRMI() {
        return portRMI;
    }

    public static void setPortRMI(int portRMI) {
        UISession.portRMI = portRMI;
    }

    public static int getPortTCP() {
        return portTCP;
    }

    public static void setPortTCP(int portTCP) {
        UISession.portTCP = portTCP;
    }

    public static String getAddr() {
        return addr;
    }

    public static void setAddr(String addr) {
        UISession.addr = addr;
    }

    public static UIObserver getObserver() {
        return observer;
    }

    public static void setObserver(UIObserver observer) {
        UISession.observer = observer;
    }

    public static GameDTO getGame() {
        return game;
    }

    public static void setGame(GameDTO game) {
        UISession.game = game;
    }
}
