package it.polimi.ingsw.networking;

import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.DB.LeaderboardDTO;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Represents the generic client interface used by the application
 * to interact with the server, independently of the underlying
 * communication protocol (TCP or RMI).<br>
 *<br>
 * This interface defines the core operations available to a player,
 * including user authentication, game lifecycle management, action execution,
 * server communication, and leaderboard retrieval.
 */
public interface Client {

    /**
     * Receives an update of the current game state from the server.
     *
     * @param game the updated game state
     * @throws IOException if a communication error occurs
     * @throws IllegalActionException if the received state is invalid
     * @throws InterruptedException if the update process is interrupted
     */
    void update(GameDTO game) throws IOException, IllegalActionException, InterruptedException;

    /**
     * Registers a new user or logs in an existing one.
     *
     * @param password the user's password
     * @param username the user's username
     * @return true if the operation succeeds, false otherwise
     * @throws IOException if a communication error occurs
     * @throws ClassNotFoundException if a deserialization error occurs
     */
    boolean addUser(String password, String username) throws IOException, ClassNotFoundException;

    /**
     * Joins an existing game session.
     *
     * @return true if the join operation succeeds, false otherwise
     * @throws IllegalActionException if the action is not allowed
     * @throws IOException if a communication error occurs
     * @throws ClassNotFoundException if a deserialization error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    boolean joinGame() throws IllegalActionException, IOException, ClassNotFoundException, InterruptedException;

    /**
     * Creates a new game session.
     *
     * @param num the number of players for the game
     * @throws IllegalActionException if the action is not allowed
     * @throws IOException if a communication error occurs
     */
    void createGame(int num) throws IllegalActionException, IOException;

    /**
     * Executes a game action on the server.
     *
     * @param action the action to execute
     * @throws Exception if an error occurs during execution or communication
     */
    void executeAction(Action action) throws Exception;
    /**
     * Returns the nickname associated with this client.
     *
     * @return the player's nickname
     */
    String getNickname();

    /**
     * Leaves the current match.
     *
     * @throws IOException if a communication error occurs
     * @throws InterruptedException if the operation is interrupted
     * @throws IllegalActionException if the action is not allowed
     * @throws ClassNotFoundException if a deserialization error occurs
     */
    void leaveMatch() throws IOException, InterruptedException, IllegalActionException, ClassNotFoundException;

    /**
     * Disconnects the client from the game session.
     *
     * @throws RemoteException if a remote communication error occurs
     */
    void leaveGame() throws RemoteException;

    /**
     * Starts sending periodic heartbeat signals to the server
     * in order to keep the connection alive.
     */
    void ping();

    /**
     * Stops the specified game session.
     *
     * @param name the name of player who want to stop the session
     * @throws IllegalActionException if the action is not allowed
     * @throws IOException if a communication error occurs
     * @throws ClassNotFoundException if a deserialization error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    void stopGame(String name) throws IllegalActionException, IOException, ClassNotFoundException, InterruptedException;

    /**
     * Sets the UI observer that will receive updates from the client.
     *
     * @param observer the UI observer instance
     */
    void setObserver(UIObserver observer);

    /**
     * Retrieves the current leaderboard from the server.
     *
     * @return a list of leaderboard entries
     * @throws RemoteException if a remote communication error occurs
     */
    List<LeaderboardDTO> getLeaderboard() throws RemoteException;
}
