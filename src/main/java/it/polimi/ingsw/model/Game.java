package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.states.*;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.events.EventResult;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.RMI.ClientCallBack;
import it.polimi.ingsw.networking.TCP.ObserverTCP;

import java.io.IOException;
import java.util.*;

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
    private Map<String, List<EventResult>> eventResults;

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

    public Game (List<Player> players, int numPlayers, Board board, GameState state, List<Player> rankings,
                 Player playerTurn, String errorFlag, int turnNumber, Map<String, List<EventResult>> eventResults) throws IllegalArgumentException {
        this.players = players;
        this.numPlayers = numPlayers;
        this.board = board;
        for(int i = 0; i < numPlayers; i++){
            players.get(i).setGame(this);
        }

        this.state = state;
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

    public void addObserverRMI(ClientCallBack observer){
        synchronized (lockRMI){
            this.observersRMI.add(observer);
        }
    }

    public void removeObserverRMI(ClientCallBack observer){
        synchronized (lockRMI){
            this.observersRMI.remove(observer);
        }
    }

    public void addObserverTCP(ObserverTCP observer){
        synchronized (lockTCP){
            this.observersTCP.add(observer);
        }
    }

    public void removeObserverTCP(ObserverTCP observer){
        synchronized (lockTCP){
            this.observersTCP.remove(observer);
        }
    }

    public void notifyObserver() throws IOException, IllegalActionException, InterruptedException {
        synchronized (lockRMI){
            for (ClientCallBack observer : observersRMI) {
                try{
                    observer.update(this.toDTO());
                }catch(Exception e){
                    //e.printStackTrace();
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

    public void closingGame() throws IllegalActionException, IOException, ClassNotFoundException, InterruptedException {
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

    public void canResume(){
        //check if all the players have reconnected
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

    public GameDTO toDTO() throws IllegalActionException {
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

    public void newTurn() {
        this.turnNumber++;
    }

    public Map<String, List<EventResult>> getEventResults() {
        return eventResults;
    }

    public void setEventResults(Map<String, List<EventResult>> eventResults) {
        this.eventResults = eventResults;
    }
}
