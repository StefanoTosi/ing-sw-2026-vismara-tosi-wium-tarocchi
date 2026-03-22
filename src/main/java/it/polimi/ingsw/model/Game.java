package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.controller.GameState;

import java.util.*;

public class Game {
    private final List<Player> players;
    private Map<Character, Integer> playerPositions;
    private final int numPlayers;
    private final Board board;
    private GameState state;

    public Game (List<Player> players) {
        this.players = players;
        this.playerPositions = new HashMap<Character, Integer>();
        this.numPlayers = players.size();
        this.board = new Board(players.size());
        for(int i = 0; i < numPlayers; i++){
            players.get(i).setGame(this);
        }
    }

    public int getNumPlayers() { return numPlayers; }

    public List<Player> getPlayers() { return players; }

    public Map<Character, Integer> getPlayerPositions() {
        return playerPositions;
    }

    public void putPlayerPosition(Player player, char order) {
        playerPositions.put(order, players.indexOf(player));
    }

    public Board getBoard() {
        return board;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }
}
