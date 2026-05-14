package it.polimi.ingsw.networking.TCP;

import it.polimi.ingsw.model.GameDTO;
import java.rmi.Remote;

public interface ObserverTCP extends Remote {
    void update(GameDTO game) throws Exception;
    void closingGame(GameDTO game) throws Exception;
}