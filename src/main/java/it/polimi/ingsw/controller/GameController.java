package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.RMI.ClientCallBack;
import it.polimi.ingsw.networking.TCP.ObserverTCP;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Controller responsible for managing all active games on the server.
 * <p>
 * It handles:<br>
 *     - Game creation and removal<br>
 *     - Player registration and lookup<br>
 *     - Joining and leaving games through TCP and RMI connections<br>
 *     - Player reconnection handling<br>
 *     - Execution of player actions
 */
public class GameController {
    private List<Game> games;

    public GameController() {
        games = new ArrayList<>();
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public List<Game> getGames() {
        return games;
    }

    /**
     * Searches for a player by nickname among all active games.
     *
     * @param nickname the nickname of the player
     * @return the corresponding player if found, {@code null} otherwise
     */
    public Player getPlayer(String nickname) {
        for(Game game : games){
            for(Player player : game.getPlayers()){
                if(player.getName().equals(nickname)){
                    return player;
                }
            }
        }
        return null;
    }

    /**
     * Adds a new game to the controller.
     *
     * @param game the game to add
     */
    public void addGame(Game game) {
        games.add(game);
    }

    /**
     * Removes the game associated with the specified player.<br>
     * The game is removed from persistent storage, closed,
     * and deleted from the list of active games.
     *
     * @param name the nickname of a player belonging to the game
     * @throws IllegalActionException if game removal is not allowed
     * @throws IOException if an I/O error occurs while removing saved data
     * @throws ClassNotFoundException if saved game data cannot be deserialized
     * @throws InterruptedException if the operation is interrupted
     */
    public void removeGame(String name) throws IllegalActionException, IOException, ClassNotFoundException, InterruptedException {
        if(getPlayer(name) != null){
            Game game = getPlayer(name).getGame();
            if(game != null){
                SaveGames.removeGame(game.toDTO());
                game.closingGame();
                games.remove(game);
            }
        }
    }

    /**
     * Attempts to add a player to an existing game through RMI.
     * <br>
     * The player is added to the first game that has available slots.
     *
     * @param player the player joining the game
     * @param client the player's RMI callback object
     * @return {@code true} if a game was found and joined, {@code false} otherwise
     * @throws IOException if a communication error occurs
     * @throws IllegalActionException if player registration fails
     */
    public boolean joinGameRMI(Player player, ClientCallBack client) throws IOException, IllegalActionException {
        for(Game game : games){
            if(game.getPlayers().size() < game.getNumPlayers()){
                game.addObserverRMI(client);
                player.setGame(game);
                game.getState().registerPlayer(game, player);
                game.notifyObserver();
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a new game and registers the first player through RMI.
     *
     * @param player the creator of the game
     * @param num the maximum number of players
     * @param client the creator's RMI callback object
     * @throws RemoteException if an RMI communication error occurs
     * @throws IllegalActionException if player registration fails
     */
    public void createGameRMI(Player player, int num, ClientCallBack client) throws RemoteException, IllegalActionException {
        Game newGame = new Game(new ArrayList<Player>(), new Random(System.currentTimeMillis()));
        addGame(newGame);
        newGame.setNumPlayers(num);
        newGame.addObserverRMI(client);
        player.setGame(newGame);
        newGame.getState().registerPlayer(newGame, player);
    }

    /**
     * Attempts to add a player to an existing game through TCP.
     * <br>
     * The player is added to the first game that has available slots.
     *
     * @param player the player joining the game
     * @param client the TCP observer associated with the player
     * @return {@code true} if a game was found and joined, {@code false} otherwise
     * @throws Exception if registration or communication fails
     */
    public boolean joinGameTCP(Player player, ObserverTCP client) throws Exception {
        for(Game game : games){
            if(game.getPlayers().size() < game.getNumPlayers()){
                game.addObserverTCP(client);
                player.setGame(game);
                game.getState().registerPlayer(game, player);
                game.notifyObserver();
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a new game and registers the first player through TCP.
     *
     * @param player the creator of the game
     * @param num the maximum number of players
     * @param client the TCP observer associated with the player
     * @throws RemoteException if a communication error occurs
     * @throws IllegalActionException if player registration fails
     */
    public void createGameTCP(Player player, int num, ObserverTCP client) throws RemoteException, IllegalActionException {
        Game newGame = new Game(new ArrayList<Player>(), new Random(System.currentTimeMillis()));
        addGame(newGame);
        newGame.setNumPlayers(num);
        newGame.addObserverTCP(client);
        player.setGame(newGame);
        newGame.getState().registerPlayer(newGame, player);
    }

    /**
     * Executes a player action and updates all observers.
     * <p>
     * If the action fails with an {@link IllegalActionException},
     * the error message is stored in the game state and propagated
     * to connected clients.
     *
     * @param action the action to execute
     * @param player the nickname of the acting player
     * @throws IllegalArgumentException if the action parameters are invalid
     * @throws IllegalActionException if the action is not allowed
     * @throws IOException if a communication error occurs
     * @throws InterruptedException if execution is interrupted
     */
    public void executeAction(Action action, String player) throws IllegalArgumentException, IllegalActionException, IOException, InterruptedException {
        // Clear any previously stored error before executing a new action.
        getPlayer(player).getGame().setErrorFlag("");
        try {
            action.execute(getPlayer(player));
            getPlayer(player).getGame().notifyObserver();
        } catch (IllegalActionException e) {
            // Store the reason of the failed action so that clients can display it.
            getPlayer(player).getGame().setErrorFlag(e.getReason());
            getPlayer(player).getGame().notifyObserver();
            throw e;
        }
    }

    /**
     * Disconnects a TCP observer from the player's game.
     *
     * @param player the player's nickname
     * @param client the TCP observer to remove
     */
    public void leaveMatchTCP(String player, ObserverTCP client) {
        if(getPlayer(player) != null){
            getPlayer(player).getGame().removeObserverTCP(client);
        }
    }

    /**
     * Disconnects an RMI observer from the player's game.
     *
     * @param player the player's nickname
     * @param client the RMI callback to remove
     */
    public void leaveMatchRMI(String player, ClientCallBack client) {
        if(getPlayer(player) != null){
            getPlayer(player).getGame().removeObserverRMI(client);
        }
    }

    /**
     * Reconnects a TCP client to its game and checks whether
     * the game can resume.
     *
     * @param player the player's nickname
     * @param client the TCP observer to reattach
     */
    public void reconnectGameTCP(String player, ObserverTCP client) {
        getPlayer(player).getGame().addObserverTCP(client);
        canResume(getPlayer(player).getGame());
    }

    /**
     * Reconnects an RMI client to its game and checks whether
     * the game can resume.
     *
     * @param player the player's nickname
     * @param client the RMI callback to reattach
     */
    public void reconnectGameRMI(String player, ClientCallBack client) {
        getPlayer(player).getGame().addObserverRMI(client);
        canResume(getPlayer(player).getGame());
    }

    /**
     * Delegates to the game logic to determine whether gameplay
     * can continue after one or more reconnections.
     *
     * @param game the game to evaluate
     */
    private void canResume(Game game) {
        game.canResume();
    }
}