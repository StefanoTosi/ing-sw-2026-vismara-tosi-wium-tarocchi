package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.states.GameState;

import java.util.*;

public class Game {
    private List<Player> players;
    private Map<Character, Integer> playerPositions;
    private int numPlayers;
    private Board board;
    private GameState state;

    public Game (List<Player> players) {
        this.players = players;
        this.playerPositions = new HashMap<Character, Integer>();
        this.numPlayers = players.size();
        this.board = new Board(players.size());
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
