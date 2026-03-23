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
        boolean Loss = true;
        for(Player player : players){
            int stars = player.getNumStars();

            for(Building building : player.getBuildings()){
                if(building.getId() == IDEffect.ESC2){
                    stars += 3;
                }
                if(building.getId() == IDEffect.ESC1){
                   Loss = false;
                }
            }
            if(stars > maxStar){
                maxStar = stars;
            }

            if(stars < minStar){
                minStar = stars;
            }

        }
        for(Player player : players){
            int stars = player.getNumStars();
            int multiple = 0;
            for(Building building : player.getBuildings()){
                if(building.getId() == IDEffect.ESC2){
                    stars += 3;
                }
                if(building.getId() == IDEffect.ESC3){
                    multiple = winnerPp; //errore perchè il bro non dovrebbe guadagnarli se anche un altro li guadagna con lui, come lo modifico accordingly
                }
            }
            if(stars == maxStar){
                //check se ha building con effetto3 shamanic ritual -> metto 2pp
                player.addPp(winnerPp);
                player.addPp(multiple);
            }

            if(stars == minStar && Loss){
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
