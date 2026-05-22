package it.polimi.ingsw.networking;

import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import java.io.IOException;

/**
 * Observer interface used by the networking layer to notify the UI
 * about changes in the game state or connection status.
 *
 * Implementations of this interface are responsible for updating
 * the user interface when the server sends new game data,
 * when a game session ends, or when the server becomes unreachable.
 */
public interface UIObserver {
    /**
     * Notifies the UI with an updated game state.
     *
     * @param game the updated game state
     * @throws IOException if an I/O error occurs while processing the update
     * @throws IllegalActionException if the received game state is invalid
     * @throws InterruptedException if the update process is interrupted
     */
    void update(GameDTO game) throws IOException, IllegalActionException, InterruptedException;

    /**
     * Notifies the UI that the game session has been closed by the server.
     *
     * @param game the final game state
     * @throws IOException if an I/O error occurs while processing the update
     * @throws IllegalActionException if the game state is invalid
     * @throws ClassNotFoundException if deserialization fails
     * @throws InterruptedException if the operation is interrupted
     */
    void closingGame(GameDTO game) throws IOException, IllegalActionException, ClassNotFoundException, InterruptedException;

    /**
     * Notifies the UI that the server has become unreachable or has crashed.
     *
     * @throws IOException if an I/O error occurs while handling the event
     * @throws IllegalActionException if the application is in an invalid state
     * @throws InterruptedException if the handling is interrupted
     */
    void serverCrashed() throws IOException, IllegalActionException, InterruptedException;
}
