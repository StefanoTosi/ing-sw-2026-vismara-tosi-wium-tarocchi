package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.events.DTO.HuntDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * During this {@code Event}, each player is awarded 1 food token and the specified amount of prestige points
 * for every {@code Hunter} in their tribe.
 */
public class Hunt extends Event {

    private final int pp;

    /**
     * Generates a {@code Hunt} object, filled with the specified parameters.
     * @param pp the amount of prestige points players are awarded for each {@code Hunter} in their tribe
     * @param era the {@code Era} the card belongs to
     */
    public Hunt(int pp, Era era){
        super(era);
        this.pp = pp;
        this.name = "Hunt";
    }

    /**
     * Resolves the {@code Event} effect. Each player receives 1 food token and the specified amount of prestige points for every {@code Hunter} in their tribe.
     * @param players the list of {@code Players} involved in the {@code Event}
     * @return the list of {@code EventResults}, containing the food & pps deltas for each player
     */
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
