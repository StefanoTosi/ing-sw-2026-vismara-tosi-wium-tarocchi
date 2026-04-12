package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.states.ChooseOfferState;
import it.polimi.ingsw.controller.states.SetupGameState;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.controller.states.GameState;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.ClientCallBack;

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
    private int playerTurn;

    public Game (List<Player> players) throws IllegalArgumentException {
        this.players = players;
        this.numPlayers = 0;
        this.board = new Board();
        for(int i = 0; i < numPlayers; i++){
            players.get(i).setGame(this);
        }

        this.state = new SetupGameState();
        this.observers = new ArrayList<>();
        this.playerTurn = numPlayers;
    }

    public void addObserver(ClientCallBack observer){
        this.observers.add(observer);
    }

    public void removeObserver(ClientCallBack observer){
        this.observers.remove(observer);
    }

    public void notifyObserver(String msg) throws RemoteException, IllegalActionException {
        for(ClientCallBack observer : observers){
           observer.update(msg, this.toDTO());
        }
    }

    public void notifyPlayer(String nickname)  throws RemoteException, IllegalActionException {
        for(ClientCallBack observer : observers){
            if(observer.getNickname().equals(nickname)){
                int stateNum;
                if(getState() instanceof ChooseOfferState){
                    stateNum = 1;
                } else {
                    stateNum = 2;
                }
                observer.myTurn(stateNum);
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

    public GameDTO toDTO(){
        if(getState() instanceof SetupGameState){
            return null;
        }
        return new GameDTO(getPlayers().stream().map(Player::toDTO).toList(),getNumPlayers(),
                getBoard().toDTO()/*, getRankings().stream().map(Player::toDTO).toList()*/);
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    public void incPlayerTurn() {
        this.playerTurn += 1;
    }

}
