package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.controller.SaveGames;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.networking.DB.UserDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Final game state responsible for computing results and rankings.
 * <p>
 * This state is reached when the game ends. It calculates each player's
 * final score, applies end-game effects, persists results to the database,
 * and determines the final ranking.
 * </p>
 */
public class EndGameState extends GameState {
    private final Game game;

    public EndGameState(Game game) {
        this.game = game;
    }

    public StateDTO getStateDTO() {
        return StateDTO.ENDGAME;
    }

    /**
     * Calculates the final PP scores for each player and orders them to form the rankings,
     * in a list where the first element is the winning player.
     */
    public void calculateRankings() throws IOException {
        List<Player> rankings = new ArrayList<>(game.getPlayers());

        for (Player player : rankings) {
            player.addPp(player.countTribePp());

            for(Building building : player.getBuildings()) {
                building.getEffect().applyEffectEndGame(player, building);
            }
        }

        //Add the score to the DB
        for(Player player : rankings) {
            UserDAO.addRankings(player.getName(), player.getPp(), game.getNumPlayers());
        }

        // first compares players based on PPs, then based on food, so that in case of a tie based on PP score
        // it checks if it is also a tie based on Food
        rankings.sort(
                Comparator.comparingInt(Player::getPp).reversed()
                .thenComparing(Comparator.comparingInt(Player::getFood).reversed())
        );

        game.setRankings(rankings);

        SaveGames.removeGame(game.toDTO());
        System.out.println("Calculated ranking, ended game");
    }
}
