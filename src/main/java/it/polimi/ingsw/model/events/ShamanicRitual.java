package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Event;

import java.util.List;

/**
 * 
 */

public class ShamanicRitual extends Event {
    private String NAME = "ShamanicRitual";
    private int pp;

    public ShamanicRitual(int pp){

    }

    public void applyEffect(List<Player> players){
        int maxStar = Integer.MAX_VALUE;
        int minStar = Integer.MIN_VALUE;

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
                //check se ha building con effetto3 shamanic ritual -> metto 2pp
                player.addPp(pp);
            }

            if(stars == minStar){
                player.addPp(-pp);
            }
        }
    }

    public int getStars(Player player){
        return player.getNumStars();
    }

    public int getPp(){
        return this.pp;
    }

    /**
     * Event doesn't have an era
     * @return null
     */
    @Override
    public Era getEra(){return null;}

    @Override
    public String getName(){
        return NAME;
    }
}
