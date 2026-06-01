package it.polimi.ingsw.networking.RMI;

import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface used for server → client communication in RMI.<br>
 * This interface defines callback methods that the server can invoke on the client
 * to send updates, messages, and game state changes.<br>
 * Every method must throw {@link RemoteException} implicitly or explicitly,
 * since it is executed over the network.
 */
public interface ClientCallBack extends Remote {
    /**
     * Sends a simple text message from the server to the client.
     *
     * @param message message to display on the client side
     * @throws RemoteException if RMI communication fails
     */
    void receiveMessage(String message) throws RemoteException;

    /**
     * Assigns a nickname to the remote client session.
     *
     * @param nickname the nickname to set
     * @throws RemoteException if RMI communication fails
     */
    void setNickname(String nickname) throws RemoteException;

    /**
     * Retrieves the nickname associated with this client session.
     *
     * @return the client's nickname
     * @throws RemoteException if RMI communication fails
     */
    String getNickname() throws RemoteException;

    /**
     * Sends an updated game state to the client.<br>
     * This is called whenever the game model changes and
     * clients need to refresh their view.
     *
     * @param game the updated game DTO
     * @throws IOException if serialization/deserialization fails
     * @throws IllegalActionException if the game state is invalid
     * @throws InterruptedException if the update is interrupted
     * @throws RemoteException if RMI communication fails
     */
    void update(GameDTO game) throws IOException, IllegalActionException, InterruptedException;

    /**
     * Notifies the client that the game session is closing.<br>
     * This can be used for cleanup, final updates, or disconnection handling.
     *
     * @param game final game state before closing
     * @throws IllegalActionException if the game state is invalid
     * @throws IOException if serialization fails
     * @throws ClassNotFoundException if deserialization fails
     * @throws InterruptedException if interrupted during processing
     * @throws RemoteException if RMI communication fails
     */
    void closingGame(GameDTO game) throws IllegalActionException, IOException, ClassNotFoundException, InterruptedException;
}
