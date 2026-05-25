package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.events.DTO.HuntDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Get +1 food and the Pps on the card for every Hunter in your clan
 */

public class Hunt extends Event {

    private final int pp;

    public Hunt(int pp, Era era){
        super(era);
        this.pp = pp;
        this.name = "Hunt";
    }

    @Override
    public List<EventResult> applyEffect(List<Player> players){
        List<EventResult> results = new ArrayList<>();
        int deltaPp;
        int deltaFood;
        int prevFood;
        int prevPp;

        for (Player player : players){
            prevFood = player.getFood();
            prevPp = player.getPp();

            //Apply building effects
            for (Building building : player.getBuildings()){
                building.getEffect().applyEffectEventHunt(player, building);
            }

            //Calculate and add food & pp amount
            deltaFood = player.getNumHunters();
            player.addFood(deltaFood);
            deltaPp = deltaFood * getPp();
            player.addPp(deltaPp);

            //Update results list
            deltaPp = player.getPp() - prevPp;
            deltaFood = player.getFood() - prevFood;
            results.add(new EventResult("Hunt", this.id, player.toDTO(), deltaPp, deltaFood));
        }

        return results;
    }

    public int getPp(){
        return this.pp;
    }

    @Override
    public String getName(){
        return name;
    }

    public HuntDTO toDTO(){
        return new HuntDTO(getEra().name(), getPp(), getId());
    }
}
