package it.polimi.ingsw.networking.TCP;

import it.polimi.ingsw.model.GameDTO;
import java.rmi.Remote;

/**
 * TCP observer interface used for server → client updates.
 *
 * This interface defines callback methods that the server uses
 * to notify TCP clients about game state changes.
 */
public interface ObserverTCP {

    /**
     * Notifies the client about a game state update.
     *
     * @param game updated game state
     * @throws Exception if serialization or network communication fails
     */
    void update(GameDTO game) throws Exception;

    /**
     * Notifies the client that the game is closing or has ended.
     *
     * @param game final game state before shutdown
     * @throws Exception if communication fails
     */
    void closingGame(GameDTO game) throws Exception;
}