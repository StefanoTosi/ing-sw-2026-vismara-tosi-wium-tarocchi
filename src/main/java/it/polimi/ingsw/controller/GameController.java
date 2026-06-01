package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.RMI.ClientCallBack;
import it.polimi.ingsw.networking.TCP.ObserverTCP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Controller responsible for managing active games and players.
 * <p>
 * Provides methods to create, remove and retrieve games, manage player
 * connections through RMI or TCP, execute game actions, and handle
 * player disconnections and reconnections.
 */
public class GameController {
    private List<Game> games;

    /**
     * Generates a new {@code GameController} with an empty game list.
     */
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
     * Searches for a player with the specified nickname among all active games.
     *
     * @param nickname the nickname of the player to search for
     * @return the matching player, or {@code null} if no player is found
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
     * Adds a game to the list of managed games.
     *
     * @param game the game to be added
     */
    public void addGame(Game game) {
        games.add(game);
    }

    /**
     * Removes the game associated with the specified player.
     * <p>
     * The game is removed from persistent storage, closed, and removed
     * from the list of active games.
     * @throws IOException if an error occurs when trying to delete the game from the save file
     */
    public void removeGame(String name) throws IOException {
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
     * Joins a player, using an RMI connection, to the first available game.
     *
     * @param player the player who wants to join a game
     * @param client the RMI callback associated with the client
     * @return {@code true} if the player successfully joins a game,
     *         {@code false} if no available game exists
     * @throws IllegalActionException if player registration is not allowed in the current game state
     */
    public boolean joinGameRMI(Player player, ClientCallBack client) throws IllegalActionException {
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
     * Creates a new game and registers the first player, using an RMI connection.
     *
     * @param player the player creating the game
     * @param num the number of players in the game, chosen by the user
     * @param client the RMI callback associated with the client
     * @throws IllegalActionException if player registration is not allowed in the current game state
     */
    public void createGameRMI(Player player, int num, ClientCallBack client) throws IllegalActionException {
        Game newGame = new Game(new ArrayList<Player>(), new Random(System.currentTimeMillis()));
        addGame(newGame);
        newGame.setNumPlayers(num);
        newGame.addObserverRMI(client);
        player.setGame(newGame);
        newGame.getState().registerPlayer(newGame, player);
    }

    /**
     * Joins a player, using a TCP connection, to the first available game.
     *
     * @param player the player who wants to join a game
     * @param client the RMI callback associated with the client
     * @return {@code true} if the player successfully joins a game,
     *         {@code false} if no available game exists
     * @throws IllegalActionException if player registration is not allowed in the current game state
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
     * Creates a new game and registers the first player, using a TCP connection.
     *
     * @param player the player creating the game
     * @param num the number of players in the game, chosen by the user
     * @param client the RMI callback associated with the client
     * @throws IllegalActionException if player registration is not allowed in the current game state
     */
    public void createGameTCP(Player player, int num, ObserverTCP client) throws IllegalActionException {
        Game newGame = new Game(new ArrayList<Player>(), new Random(System.currentTimeMillis()));
        addGame(newGame);
        newGame.setNumPlayers(num);
        newGame.addObserverTCP(client);
        player.setGame(newGame);
        newGame.getState().registerPlayer(newGame, player);
    }

    /**
     * Executes an action requested by a player, throwing an exception if the action is not allowed in the current game state.
     * <p>
     * If the action succeeds, all game observers are notified.
     * If an {@code IllegalActionException} is thrown, the error message
     * is stored in the game state and observers are notified before
     * rethrowing the exception.
     *
     * @param action the action to execute
     * @param player the nickname of the player performing the action
     * @throws IllegalArgumentException if the action arguments are invalid
     * @throws IllegalActionException if the action is not allowed in the current game state
     * @throws IOException if a communication error occurs
     * @throws InterruptedException if the current thread is interrupted
     * @see Action
     */
    public void executeAction(Action action, String player) throws IllegalArgumentException, IllegalActionException, IOException, InterruptedException {
        getPlayer(player).getGame().setErrorFlag("");
        try {
            action.execute(getPlayer(player));
            getPlayer(player).getGame().notifyObserver();
        } catch (IllegalActionException e) {
            getPlayer(player).getGame().setErrorFlag(e.getReason());
            getPlayer(player).getGame().notifyObserver();
            throw e;
        }
    }

    /**
     * Removes a TCP observer from the specified player's game.
     *
     * @param player the player's nickname
     * @param client the TCP observer to be removed
     */
    public void leaveMatchTCP(String player, ObserverTCP client) {
        if(getPlayer(player) != null){
            getPlayer(player).getGame().removeObserverTCP(client);
        }
    }

    /**
     * Removes an RMI observer from the specified player's game.
     *
     * @param player the player's nickname
     * @param client the RMI observer to be removed
     */
    public void leaveMatchRMI(String player, ClientCallBack client) {
        if(getPlayer(player) != null){
            getPlayer(player).getGame().removeObserverRMI(client);
        }
    }

    /**
     * Reconnects a TCP client to an existing game.
     * <p>
     * After adding the observer, the controller checks whether the game
     * can be resumed.
     *
     * @param player the player's nickname
     * @param client the TCP observer to reconnect
     */
    public void reconnectGameTCP(String player, ObserverTCP client) {
        getPlayer(player).getGame().addObserverTCP(client);
        canResume(getPlayer(player).getGame());
    }

    /**
     * Reconnects an RMI client to an existing game.
     * <p>
     * After adding the observer, the controller checks whether the game
     * can be resumed.
     *
     * @param player the player's nickname
     * @param client the RMI observer to reconnect
     */
    public void reconnectGameRMI(String player, ClientCallBack client) {
        getPlayer(player).getGame().addObserverRMI(client);
        canResume(getPlayer(player).getGame());
    }

    /**
     * Checks whether a suspended game can be resumed.
     *
     * @param game the game to check
     */
    private void canResume(Game game) {
        game.canResume();
    }
}