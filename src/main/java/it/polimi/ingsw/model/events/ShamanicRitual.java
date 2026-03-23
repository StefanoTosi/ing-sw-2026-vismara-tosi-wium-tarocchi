package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Building;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.effects.IDEffect;

import java.util.List;

/**
 * The player with the most stars wins the pps, the one with less loose them
 */

public class ShamanicRitual extends Event {

    private int winnerPp;
    private int loserPp;

    public ShamanicRitual(int winnerPp, int loserPp, Era era){
        super(era);
        this.winnerPp = winnerPp;
        this.loserPp = loserPp;
        this.name = "ShamanicRitual";
    }

    public void applyEffect(List<Player> players){
        int maxStar = Integer.MIN_VALUE;
        int minStar = Integer.MAX_VALUE;
        int equal = 0;
        for(Player player : players){
            int stars = player.getNumStars();

            if(stars > maxStar){
                maxStar = stars;
            }

            if(stars < minStar){
                minStar = stars;
            }

        }
        for(Player player : players){

            int stars = player.getNumStars();
            if(stars == maxStar){
                equal++;
            }
        }
        for(Player player : players){
            int stars = player.getNumStars();
            int multiple = 0;

            if(stars == maxStar && equal == 1){
                //check se ha building con effetto3 shamanic ritual -> metto 2pp
                player.addPp(winnerPp);
                player.addPp(multiple);
            }

            if(stars == minStar){
                player.addPp(-loserPp);
            }
        }
    }

    public int getStars(Player player){
        return player.getNumStars();
    }

    @Override
    public Era getEra(){return era;}

    @Override
    public String getName(){
        return name;
    }
}
