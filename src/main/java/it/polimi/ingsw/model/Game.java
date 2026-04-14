package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.states.*;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.ClientCallBack;
import jdk.jfr.consumer.RecordingFile;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;

public class Game {
    private final List<Player> players;
    private int numPlayers;
    private final Board board;
    private GameState state;
    private List<Player> rankings;
    private List<ClientCallBack> observers;
    private Player playerTurn;
    private String errorFlag;

    public Game (List<Player> players) throws IllegalArgumentException {
        this.players = players;
        this.numPlayers = 0;
        this.board = new Board();
        for(int i = 0; i < numPlayers; i++){
            players.get(i).setGame(this);
        }

        this.state = new SetupGameState();
        this.observers = new ArrayList<>();
        this.playerTurn = null;
        this.errorFlag = "";
    }

    public void addObserver(ClientCallBack observer){
        this.observers.add(observer);
    }

    public void removeObserver(ClientCallBack observer){
        this.observers.remove(observer);
    }

    public void notifyObserver() throws RemoteException, IllegalActionException {
        for (ClientCallBack observer : observers) {
            new Thread(() -> {
                try {
                    observer.update(this.toDTO());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                } catch (IllegalActionException e) {
                    throw new RuntimeException(e);
                }
            }).start();
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
        String turn = "";
        if (playerTurn != null) {
            turn = playerTurn.getName();
        }

        return new GameDTO(getPlayers().stream().map(Player::toDTO).toList(),getNumPlayers(),
                getBoard().toDTO(), getState().getStateDTO(), turn/*, getRankings().stream().map(Player::toDTO).toList()*/);
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

}
