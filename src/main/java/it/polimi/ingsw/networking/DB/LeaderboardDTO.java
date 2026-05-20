package it.polimi.ingsw.networking.DB;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Data Transfer Object (DTO) used to represent
 * a leaderboard entry.
 *
 * This class stores information about:
 *  - The player's nickname;
 *  - The total score achieved;
 *  - The number of players involved in the match;
 *
 * It also supports JSON serialization/deserialization
 * through Jackson annotations.
 */
public class LeaderboardDTO implements Serializable {
    private String nickname;
    private int totalScore;
    private int numPlayers;

    /**
     * Default constructor required for serialization frameworks.
     */
    public LeaderboardDTO() {}

    /**
     * Creates a new leaderboard entry.
     *
     * @param nickname the player's nickname
     * @param totalScore the total score achieved
     * @param numPlayers the number of players in the match
     */
    @JsonCreator
    public LeaderboardDTO(@JsonProperty("nickname")String nickname,@JsonProperty("totalScore") int totalScore,@JsonProperty("numPlayers") int numPlayers) {
        this.nickname = nickname;
        this.totalScore = totalScore;
        this.numPlayers = numPlayers;
    }

    /**
     * Returns the player's nickname.
     *
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Returns the player's total score
     *
     * @return the total score
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Returns the number of players involved in the match.
     *
     * @return the number of players
     */
    public int getNumPlayers() {
        return numPlayers;
    }
}
