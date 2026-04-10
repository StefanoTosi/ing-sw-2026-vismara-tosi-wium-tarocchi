package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.effects.Building;

import java.util.Comparator;
import java.util.List;

public class EndGameState extends GameState {
    private final Game game;

    public EndGameState(Game game) {
        this.game = game;
    }

    /**
     * Calculates the final PP scores for each player and orders them to form the rankings,
     * in a list where the first element is the winning player.
     */
    public void calculateRankings() {
        List<Player> rankings = game.getPlayers();
        for (Player player : rankings) {
            player.addPp(player.countTribePp());

            for(Building building : player.getBuildings()) {
                building.getEffect().applyEffectEndGame(player, building);
            }
        }

        rankings.sort(Comparator.comparingInt(Player::getPp).reversed());
        game.setRankings(rankings);
    }
}
