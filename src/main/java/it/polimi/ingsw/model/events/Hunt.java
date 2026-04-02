package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;

/**
 * Represents the Event Hunt
 */

public class Hunt extends Event {

    private int pp;

    public Hunt(int pp, Era era){
        super(era);
        this.pp = pp;
        this.name = "Hunt";
    }

    /**
     * Apply the effect of the event Sustenance.
     * Get +1 food and the Pps on the card for every Hunter in your clan
     * @param player
     */

    public void applyEffect(Player player){
        int numHunter = player.getNumHunters();
        player.addFood(numHunter);
        int ppToAdd = numHunter * getPp();
        player.addPp(ppToAdd);
    }

    public int getPp(){
        return this.pp;
    }

    @Override
    public String getName(){
        return name;
    }
}
