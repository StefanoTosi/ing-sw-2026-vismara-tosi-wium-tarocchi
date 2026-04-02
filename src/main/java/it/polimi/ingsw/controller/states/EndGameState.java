package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.Comparator;
import java.util.List;

public class EndGameState extends GameState {
    private final Game game;

    public EndGameState(Game game) {
        this.game = game;
    }

    /**
     * Calculates the final PP scores for each player and orders them to form the rankings.
     * @return the rankings of the players, as a list where the first element is the winning player.
     */
    public List<Player> calculateRankings() {
        List<Player> rankings = game.getPlayers();
        for (Player player : rankings) {
            player.addPp(player.countTribePp());
            //player.addPp(player.countEffectPp());
        }

        rankings.sort(Comparator.comparingInt(Player::getPp).reversed());
        return rankings;
    }
}
