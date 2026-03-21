package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;

import java.util.List;

/**
 * The player with the most stars wins the pps, the one with less loose them
 */

public class ShamanicRitual extends Event {

    private int pp;

    public ShamanicRitual(int pp, Era era){
        super(era);
        this.pp = pp;
        this.name = "ShamanicRitual";
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

    @Override
    public Era getEra(){return era;}

    @Override
    public String getName(){
        return name;
    }
}
