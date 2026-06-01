package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.states.*;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.events.EventResult;
import it.polimi.ingsw.networking.RMI.ClientCallBack;
import it.polimi.ingsw.networking.TCP.ObserverTCP;
import java.util.*;

/**
 * Represents a game session and manages its global state,
 * including participating {@code Players}, the {@code Board}, the current turn,
 * remote observers (RMI/TCP), and {@code Event} results.
 * The class implements the {@code Observer} pattern in order to notify
 * connected clients about game state updates.
 */
public class Game {
    private final List<Player> players;
    private int numPlayers;
    private final Board board;
    private GameState state;
    private List<Player> rankings;
    private List<ClientCallBack> observersRMI;
    private List<ObserverTCP> observersTCP;
    private Player playerTurn;
    private String errorFlag;
    private Random rng;
    private int turnNumber;
    private final Object lockRMI;
    private final Object lockTCP;
    private List<List<EventResult>> eventResults;

    /**
     * Creates a new game by initializing the {@code Board}, the initial game state, and all structures required for observer and turn management.
     * @param players list of {@code Players} participating in the game
     * @param rng random number generator seed, only used in testing
     * @throws IllegalArgumentException if the {@code Board} cannot be correctly initialized
     */
    public Game (List<Player> players, Random rng) throws IllegalArgumentException {
        this.players = players;
        this.numPlayers = 0;
        this.board = new Board(rng);
        this.state = new SetupGameState();
        this.observersRMI = new ArrayList<>();
        this.observersTCP = new ArrayList<>();
        this.playerTurn = null;
        this.errorFlag = "";
        this.rng = rng;
        this.turnNumber = 1;
        this.rankings = new ArrayList<>();
        this.lockRMI = new Object();
        this.lockTCP = new Object();
        this.eventResults = null;
    }

    /**
     * Creates a game starting from an already existing state, filled with the specified data.<br>
     * This constructor is used for loading and restoring a previously saved game.
     *
     * @param players list of {@code Players} participating in the game
     * @param numPlayers number of players participating in the game
     * @param board the current {@code Board}
     * @param rankings the rankings calculated at the end of the game
     * @param playerTurn the {@code Player} whose turn is currently active
     * @param errorFlag a {@code String} containing information about the latest error
     * @param turnNumber current turn number
     * @param eventResults results of events resolved during the latest round
     * @throws IllegalArgumentException if the {@code Board} cannot be correctly initialized
     */
    public Game (List<Player> players, int numPlayers, Board board, List<Player> rankings,
                 Player playerTurn, String errorFlag, int turnNumber, List<List<EventResult>> eventResults) throws IllegalArgumentException {
        this.players = players;
        this.numPlayers = numPlayers;
        this.board = board;
        for(int i = 0; i < numPlayers; i++){
            players.get(i).setGame(this);
        }

        this.playerTurn = playerTurn;
        this.errorFlag = errorFlag;
        this.turnNumber = turnNumber;
        this.rankings = rankings;
        this.observersRMI = new ArrayList<>();
        this.observersTCP = new ArrayList<>();
        this.lockRMI = new Object();
        this.lockTCP = new Object();
        this.eventResults = eventResults;
    }

    /**
     * Adds an RMI observer to the game's observer list.
     * The operation is synchronized to guarantee thread safety.
     *
     * @param observer RMI observer to be added
     */
    public void addObserverRMI(ClientCallBack observer){
        synchronized (lockRMI){
            this.observersRMI.add(observer);
        }
    }

    /**
     * Removes an RMI observer from the game's observer list.
     * The operation is synchronized to guarantee thread safety.
     *
     * @param observer RMI observer to be removed
     */
    public void removeObserverRMI(ClientCallBack observer){
        synchronized (lockRMI){
            this.observersRMI.remove(observer);
        }
    }

    /**
     * Adds a TCP observer to the game's observer list.
     * The operation is synchronized to guarantee thread safety.
     *
     * @param observer TCP observer to be added
     */
    public void addObserverTCP(ObserverTCP observer){
        synchronized (lockTCP){
            this.observersTCP.add(observer);
        }
    }

    /**
     * Removes a TCP observer from the game's observer list.
     * The operation is synchronized to guarantee thread safety.
     *
     * @param observer TCP observer to be removed
     */
    public void removeObserverTCP(ObserverTCP observer){
        synchronized (lockTCP){
            this.observersTCP.remove(observer);
        }
    }

    /**
     * Notifies all registered observers (RMI and TCP) by sending the updated game state.
     * <p>
     * Each observer receives a {@code GameDTO} object representing the current state of the game.
     * <p>
     * Any exception raised during notification is caught and printed without interrupting the notification process for the remaining observers.
     */
    public void notifyObserver() {
        synchronized (lockRMI){
            for (ClientCallBack observer : observersRMI) {
                try{
                    observer.update(this.toDTO());
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        synchronized (lockTCP){
            for (ObserverTCP observer : observersTCP) {
                try{
                    observer.update(this.toDTO());
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Notifies all observers that the game is closing.
     * <p>
     * Each client receives the final game state through a {@code GameDTO} object.
     * <p>
     * Any exception raised during notification is caught and printed without interrupting the notification process for the remaining observers.
     */
    public void closingGame() {
        synchronized (lockRMI){
            for (ClientCallBack observer : observersRMI) {
                try{
                    observer.closingGame(this.toDTO());
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        synchronized (lockTCP){
            for (ObserverTCP observer : observersTCP) {
                try{
                    observer.closingGame(this.toDTO());
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Notifies the {@code Observer} when all players have successfully reconnected to an interrupted game.
     */
    public void canResume(){
        // Check if all the players have reconnected
        if(observersRMI.size() + observersTCP.size() == numPlayers){
            try {
                notifyObserver();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public List<Player> getRankings() {
        return rankings;
    }

    public void setRankings(List<Player> rankings) {
        this.rankings = rankings;
    }

    public int getNumPlayers() { return numPlayers; }

    public List<Player> getPlayers() { return players; }

    public Board getBoard() {
        return board;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    /**
     * Converts the current {@code Game} object into the corresponding {@code GameDTO},
     * converting all of its components as well through calls to other {@code toDTO} methods.
     * @return the {@code GameDTO} obtained by converting all its components to DTO format
     */
    public GameDTO toDTO() {
        Player turn = new Player("");
        if (playerTurn != null) {
            turn = playerTurn;
        }

        return new GameDTO(getPlayers().stream().map(Player::toDTO).toList(),getNumPlayers(), getBoard().toDTO(), getState().getStateDTO(),
                turn.toDTO(), getRankings().stream().map(Player::toDTO).toList(), getTurnNumber(), getErrorFlag(), getEventResults());
    }

    public Player getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(Player playerTurn) {
        this.playerTurn = playerTurn;
    }

    public String getErrorFlag() {
        return errorFlag;
    }

    public void setErrorFlag(String errorFlag) {
        this.errorFlag = errorFlag;
    }

    public Random getRng() {
        return rng;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    /**
     * Increments the turn number.
     */
    public void newTurn() {
        this.turnNumber++;
    }

    public List<List<EventResult>> getEventResults() {
        return eventResults;
    }

    public void setEventResults(List<List<EventResult>> eventResults) {
        this.eventResults = eventResults;
    }
}