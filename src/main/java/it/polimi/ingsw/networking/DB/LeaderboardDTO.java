package it.polimi.ingsw.networking.DB;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class LeaderboardDTO implements Serializable {
    private String nickname;
    private int totalScore;
    private int numPlayers;

    public LeaderboardDTO() {}

    @JsonCreator
    public LeaderboardDTO(@JsonProperty("nickname")String nickname,@JsonProperty("totalScore") int totalScore,@JsonProperty("numPlayers") int numPlayers) {
        this.nickname = nickname;
        this.totalScore = totalScore;
        this.numPlayers = numPlayers;
    }

    public String getNickname() {
        return nickname;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getNumPlayers() {
        return numPlayers;
    }
}
