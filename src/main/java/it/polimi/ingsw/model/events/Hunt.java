package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;

import java.util.List;

/**
 * Get +1 food and the Pps on the card for every Hunter in your clan
 */

public class Hunt extends Event {

    private int pp;

    public Hunt(int pp, Era era){
        super(era);
        this.pp = pp;
        this.name = "Hunt";
    }

    @Override
    public void applyEffect(List<Player> players){
        for (Player player : players){
            for (Building building : player.getBuildings()){
                building.getEffect().applyEffectEventHunt(player, building);
            }
        }

        for(Player player : players){
            int numHunter = player.getNumHunters();
            player.addFood(numHunter);
            int ppToAdd = numHunter * getPp();
            player.addPp(ppToAdd);
        }
    }

    public int getPp(){
        return this.pp;
    }

    @Override
    public String getName(){
        return name;
    }
}
