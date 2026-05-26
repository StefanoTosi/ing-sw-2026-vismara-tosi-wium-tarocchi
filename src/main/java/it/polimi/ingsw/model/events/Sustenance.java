package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.events.DTO.SustenanceDTO;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

/**
 * During this {@code Event}, players are required to pay 1 food token for each {@code Character} card in their tribe ({@code Building} cards do not count).
 * If their food is insufficient, players are deducted the amount of prestige points indicated on the {@code Event} card
 * multiplied by the number of {@code Characters} they couldn’t feed. Players cannot choose to lose prestige points if they own food tokens.
 */
public class Sustenance extends Event {
    private final int pp;

    /**
     * Generates a {@code Sustenance} object, filled with the specified parameters.
     * @param pp the amount of prestige points players are deducted for each unfed {@code Character}
     * @param era the {@code Era} the card belongs to
     */
    public Sustenance(int pp, Era era){
        super(era);
        this.pp = pp;
        this.name = "Sustenance";
    }

    /**
     * Resolves the {@code Event} effect. Each player in the specified list is required 1 food for each one of their {@Characters}, otherwise they lose pps.
     * @param players the list of {@code Players} involved in the {@code Event}
     * @return the list of {@code EventResults}, containing the food & pps deltas for each player
     */
    @Override
    public List<EventResult> applyEffect(List<Player> players){
        List<EventResult> results = new ArrayList<>();
        int deltaPp = 0;
        int deltaFood;

        // Apply building effects for sustenance
        for (Player player : players){
            player.setFoodDiscount(0);
            for (Building building : player.getBuildings()){
                building.getEffect().applyEffectEventSustenance(player, building);
            }
        }

        // Take food from players
        for(Player player : players){
            // Required food
            deltaFood = max(player.countNumCharacters() - player.getNumGatherers() * 3 - player.getFoodDiscount(), 0);

            // Verify they can pay
            if (player.getFood() >= deltaFood) {
                player.addFood(-deltaFood);
            } else{
                int unfedCharacters = deltaFood - player.getFood();
                deltaFood = player.getFood();
                player.setFood(0);
                deltaPp = - unfedCharacters * pp;
                player.addPp(deltaPp);
            }

            results.add(new EventResult("Sustenance", this.id, player.toDTO(), deltaPp, deltaFood));
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

    public SustenanceDTO toDTO() {
        return new SustenanceDTO(getEra().name(), getPp(), getId());
    }
}
