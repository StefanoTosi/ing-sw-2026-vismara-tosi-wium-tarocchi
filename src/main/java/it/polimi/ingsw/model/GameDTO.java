package it.polimi.ingsw.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.controller.states.GameState;
import it.polimi.ingsw.controller.states.StateDTO;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.BoardDTO;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class GameDTO implements Serializable {
    private  List<PlayerDTO> players;
    private int numPlayers;
    private BoardDTO board;
    private StateDTO state; // TODO: temporaneo, possiamo serializzare i GameState?
    private PlayerDTO playerTurn;
    private int turnNumber;
    private List<PlayerDTO> rankings;

    @JsonCreator
    private GameDTO() {}

    public GameDTO(List<PlayerDTO> players, int numPlayers, BoardDTO board, StateDTO state, PlayerDTO playerTurn, List<PlayerDTO> rankings, int turnNumber) {
        this.players = List.copyOf(players);
        this.numPlayers = numPlayers;
        this.board = board;
        this.state = state;
        this.playerTurn = playerTurn;
        this.rankings = List.copyOf(rankings);
        this.turnNumber = turnNumber;
    }

    public List<PlayerDTO> getPlayers() {
        return players;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public BoardDTO getBoard() {
        return board;
    }

    public StateDTO getState() {
        return state;
    }

    public PlayerDTO getPlayerTurn() {
        return playerTurn;
    }


    public List<PlayerDTO> getRankings() {
        return rankings;
    }


    public int getTurnNumber() {
        return turnNumber;
    }
}
