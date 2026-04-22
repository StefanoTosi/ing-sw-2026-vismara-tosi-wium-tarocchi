package it.polimi.ingsw.networking.TCP;

import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.IOException;
import java.rmi.Remote;

public interface ObserverTCP extends Remote {
    void update(GameDTO game) throws Exception;
}
