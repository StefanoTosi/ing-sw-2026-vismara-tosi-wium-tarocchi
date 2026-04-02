package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;

/**
 * Represents the Event Sustenance
 */

public class Sustenance extends Event {
    private int pp;
    private int numCharacter;

    public Sustenance(int pp, Era era){
        super(era);
        this.pp = pp;
        this.name = "Sustenance";
    }

    /**
     * Apply the effect of the event Sustenance.
     * Pay 1 food for every Character in your clan. If you finish your rations
     * before feeding all your characters you will lose Pps for every unfed Character.
     * @param player
     */

    public void applyEffect(Player player){
        numCharacter = player.countNumCharacters();
        int food = player.getFood() + player.getNumGatherers()*3;
        if(food >= numCharacter && player.getNumGatherers()*3 < numCharacter){
            player.setFood(food - numCharacter);
        } else{
            player.setFood(0);
            int unfedCharacter = numCharacter - food;
            int ppLoss = - unfedCharacter * pp;
            player.addPp(ppLoss);
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
