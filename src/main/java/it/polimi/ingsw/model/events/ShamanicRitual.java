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

        for (Player player : players){
            for (Building building : player.getBuildings()){
                building.getEffect().applyEffectEventShamanicRitual(player, building);
            }
        }

        for (Player player : players) {
            int stars = player.getNumStars();

            if (stars > maxStar) {
                maxStar = stars;
            }

            if (stars < minStar) {
                minStar = stars;
            }
        }

        for (Player player : players){
            int stars = player.getNumStars();

            if(stars == maxStar){
                //check se ha building con effetto3 shamanic ritual -> metto 2pp
                if (player.getDoublePp() /* TODO: controlalre che non ci sia parità*/ ) {
                    player.addPp(winnerPp * 2);
                } else {
                    player.addPp(winnerPp);
                }
            }

            if(stars == minStar && !player.getDontLosePp()){
                player.addPp(-loserPp);
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
