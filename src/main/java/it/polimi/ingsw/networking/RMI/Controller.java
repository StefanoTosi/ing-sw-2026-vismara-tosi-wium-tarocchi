package it.polimi.ingsw.networking.RMI;
import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.DB.LeaderboardDTO;

import java.io.IOException;
import java.rmi.*;
import java.util.List;

/**
 * Remote RMI interface exposed by the server.<br>
 *
 * Defines all operations that a client can invoke remotely:<br>
 * - user authentication<br>
 * - game lifecycle management<br>
 * - gameplay actions<br>
 * - heartbeat mechanism<br>
 * - leaderboard retrieval<br>
 *
 * All methods may throw RemoteException due to network communication.
 */
public interface Controller extends Remote{
    /**
     * Registers a new user or performs login.
     *
     * @param psw user password
     * @param nickname unique username
     * @param client RMI callback reference used for server → client communication
     * @return true if login/registration succeeds, false otherwise
     */
    boolean addUser(String psw, String nickname, ClientCallBack client) throws RemoteException;

    /**
     * Heartbeat signal used to detect active clients.
     *
     * @param name nickname of the client sending the ping
     */
    void ping(String name) throws RemoteException;

    /**
     * Creates a new game session.
     *
     * @param name creator username
     * @param numPlayers number of players required to start the game
     */
    void createGame(String name, int numPlayers) throws RemoteException, IllegalActionException;

    /**
     * Removes a user from the lobby or pre-game state.
     *
     * @param name username
     */
    void leaveGame(String name) throws RemoteException;

    /**
     * Leaves an active match and performs cleanup.
     *
     * @param name username
     */
    void leaveMatch(String name) throws IOException, IllegalActionException, ClassNotFoundException, InterruptedException;

    /**
     * Joins an existing game or reconnects to an ongoing one.
     *
     * @param name username
     * @return true if join succeeds
     */
    boolean joinGame(String name) throws IOException, IllegalActionException, InterruptedException;

    /**
     * Executes a game action sent by a client.
     *
     * @param action game action
     * @param nickname player performing the action
     */
    void executeAction(Action action, String nickname) throws IOException, IllegalActionException, InterruptedException;

    /**
     * Stops and removes a running game session.
     *
     * @param name username requesting termination
     */
    void stopGame(String name) throws IOException, IllegalActionException, ClassNotFoundException, InterruptedException;

    /**
     * Retrieves global leaderboard data.
     *
     * @return list of player rankings
     */
    List<LeaderboardDTO> getLeaderboard()throws RemoteException;
}
