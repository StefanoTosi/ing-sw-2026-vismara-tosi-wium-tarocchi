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

/**
 * Holds information about the current state of the {@code Game} in DTO format.
 */
public class GameDTO implements Serializable {
    private  List<PlayerDTO> players;
    private int numPlayers;
    private BoardDTO board;
    private StateDTO state;
    private PlayerDTO playerTurn;
    private int turnNumber;
    private List<PlayerDTO> rankings;
    private String errorFlag;
    private Map<String, List<EventResult>> eventResults;

    @JsonCreator
    private GameDTO() {}

    /**
     * Generates and initializes a {@code GameDTO} object, filled with the specified parameters.
     * @param players list of {@code Players} participating in the game
     * @param numPlayers number of players participating in the game
     * @param board the current {@code Board} in DTO format
     * @param state the current game {@code State} in DTO format
     * @param playerTurn the {@code Player} whose turn is currently active
     * @param rankings the rankings calculated at the end of the game
     * @param turnNumber current turn number
     * @param errorFlag a {@code String} containing information about the latest error
     * @param eventResults results of events resolved during the latest round
     */
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

    /**
     * Converts the current {@code GameDTO} object into the corresponding {@code Game},
     * converting all of its components as well through calls to other {@code fromDTO} methods.
     * @return the {@code Game} obtained by converting all its components from DTO to standard objects
     */
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
