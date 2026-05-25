package it.polimi.ingsw.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.controller.states.StateDTO;
import it.polimi.ingsw.model.board.BoardDTO;
import it.polimi.ingsw.model.events.EventResult;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class GameDTO implements Serializable {
    private  List<PlayerDTO> players;
    private int numPlayers;
    private BoardDTO board;
    private StateDTO state; // TODO: temporaneo, possiamo serializzare i GameState?
    private PlayerDTO playerTurn;
    private int turnNumber;
    private List<PlayerDTO> rankings;
    private String errorFlag;
    private Map<String, List<EventResult>> eventResults;

    @JsonCreator
    private GameDTO() {}

    public GameDTO(List<PlayerDTO> players, int numPlayers, BoardDTO board, StateDTO state, PlayerDTO playerTurn, List<PlayerDTO> rankings, int turnNumber, String errorFlag, Map<String, List<EventResult>> eventResults) {
        this.players = List.copyOf(players);
        this.numPlayers = numPlayers;
        this.board = board;
        this.state = state;
        this.playerTurn = playerTurn;
        this.rankings = List.copyOf(rankings);
        this.turnNumber = turnNumber;
        this.errorFlag = errorFlag;
        this.eventResults = eventResults;
    }

    public Game fromDTO() {
        return new Game(
                this.players.stream()
                        .map(PlayerDTO::fromDTO)
                        .collect(Collectors.toCollection(ArrayList::new)),
                this.numPlayers, this.board.fromDTO(),
                this.rankings.stream()
                        .map(PlayerDTO::fromDTO)
                        .collect(Collectors.toCollection(ArrayList::new)),
                this.playerTurn.fromDTO(), this.errorFlag, this.turnNumber, this.eventResults);
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

    public String getErrorFlag() {
        return errorFlag;
    }

    public Map<String, List<EventResult>> getEventResults() {
        return eventResults;
    }
}
