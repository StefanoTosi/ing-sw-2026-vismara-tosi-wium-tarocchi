package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.events.DTO.ShamanicRitualDTO;

import java.util.List;

/**
 * The player with the most stars wins the pps, the one with the least loses them
 */

public class ShamanicRitual extends Event {

    private final int winnerPp;
    private final int loserPp;

    public ShamanicRitual(int winnerPp, int loserPp, Era era){
        super(era);
        this.winnerPp = winnerPp;
        this.loserPp = loserPp;
        this.name = "ShamanicRitual";
    }

    public int getWinnerPp() {
        return winnerPp;
    }

    public int getLoserPp() {
        return loserPp;
    }

    @Override
    public void applyEffect(List<Player> players){
        int maxStar = Integer.MIN_VALUE;
        int minStar = Integer.MAX_VALUE;
        boolean tie = false;
        boolean winnerFound = false;

        //apply building effects
        for (Player player : players){
            for (Building building : player.getBuildings()){
                building.getEffect().applyEffectEventShamanicRitual(player, building);
            }
        }

        //look for max & min stars
        for (Player player : players) {
            int stars = player.getNumStars();

            if (stars > maxStar) {
                maxStar = stars;
            }

            if (stars < minStar) {
                minStar = stars;
            }
        }

        //look for winners and losers
        for (Player player : players){
            int stars = player.getNumStars();

            if(stars == maxStar){
                if(winnerFound) {
                    tie = true;
                }
                player.addPp(winnerPp);
                winnerFound = true;
            }

            if(stars == minStar && !player.getDontLosePp()){
                player.addPp(-loserPp);
            }
        }

        for(Player player : players) {
            if(player.getNumStars() == maxStar && player.getDoublePp() && !tie) {
                player.addPp(winnerPp);
            }
        }
    }

    @Override
    public String getName(){
        return name;
    }

    public ShamanicRitualDTO toDTO() {
        return new ShamanicRitualDTO(getWinnerPp(), getLoserPp(), getEra().name());
    }
}
