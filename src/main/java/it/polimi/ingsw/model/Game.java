package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.states.*;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.RMI.ClientCallBack;
import it.polimi.ingsw.networking.TCP.ObserverTCP;

import java.io.IOException;
import java.rmi.RemoteException;
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

    public Game (List<Player> players, Random rng) throws IllegalArgumentException {
        this.players = players;
        this.numPlayers = 0;
        this.board = new Board(rng);
        for(int i = 0; i < numPlayers; i++){
            players.get(i).setGame(this);
        }

        this.state = new SetupGameState();
        this.observersRMI = new ArrayList<>();
        this.observersTCP = new ArrayList<>();
        this.playerTurn = null;
        this.errorFlag = "";
        this.rng = rng;
        this.turnNumber = 1;
        this.rankings = new ArrayList<>();
    }

    public void addObserverRMI(ClientCallBack observer){
        this.observersRMI.add(observer);
    }

    public void removeObserverRMI(ClientCallBack observer){
        this.observersRMI.remove(observer);
    }

    public void addObserverTCP(ObserverTCP observer){
        this.observersTCP.add(observer);
    }

    public void removeObserverTCP(ObserverTCP observer){
        this.observersTCP.remove(observer);
    }

    public void notifyObserver() throws IOException, IllegalActionException, InterruptedException {
        for (ClientCallBack observer : observersRMI) {
            observer.update(this.toDTO());
        }
        for (ObserverTCP observer : observersTCP) {
            try{
                observer.update(this.toDTO());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void closingGame() throws IllegalActionException, IOException, ClassNotFoundException, InterruptedException {
        for (ClientCallBack observer : observersRMI) {
            observer.closingGame(this.toDTO());
        }
        for (ObserverTCP observer : observersTCP) {
            try{
                observer.closingGame(this.toDTO());
            }catch(Exception e){
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

        return new GameDTO(getPlayers().stream().map(Player::toDTO).toList(),getNumPlayers(),
                getBoard().toDTO(), getState().getStateDTO(), turn.toDTO(), getRankings().stream().map(Player::toDTO).toList(), getTurnNumber());
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

    public Random gerRng() {
        return rng;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void newTurn() {
        this.turnNumber++;
    }
}
