package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Event;

/**
 * pay 1 food for every Character in your clan. If you finish your rations
 * before feeding all your characters you will lose Pps for every unfed Character.
 */

public class Sustenance extends Event {
    private String NAME = "Sustenance";
    private int pp;
    private int numCharacter;
    private Era era;

    public Sustenance(int pp){
        this.pp = pp;
        this.era = era;
    }

    public void applyeffect(Player player){
        numCharacter = player.countNumCharacters();
        int food = player.getFood();
        if(food >= numCharacter){
            player.setFood(food - numCharacter);
        }
        else{
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
    public Era getEra(){return era;}

    @Override
    public String getName(){
        return NAME;
    }
}
