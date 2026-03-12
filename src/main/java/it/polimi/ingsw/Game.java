package it.polimi.ingsw;

import java.util.List;

public class Game {
    private List<Player> players;
    private int numPlayers;
    private Board board;

    public Game (List<Player> players, Board board) {
        this.players = players;
        this.numPlayers = players.size();
        this.board = board;
    }

    public int getNumPlayers() { return numPlayers; }

    public List<Player> getPlayers() { return players; }

    public Board getBoard() { return board; }
}
