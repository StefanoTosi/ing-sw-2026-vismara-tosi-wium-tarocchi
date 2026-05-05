package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.events.DTO.SustenanceDTO;

import java.util.List;

import static java.lang.Math.max;

/**
 * Pay 1 Food token for each Character card in your tribe (Building cards do not count). If, after
 * paying all the Food you have, you couldn’t feed all your Characters, you lose the amount of Prestige
 * Points indicated on the Event card for each Character card you couldn’t feed
 */
public class Sustenance extends Event {
    private int pp;

    public Sustenance(int pp, Era era){
        super(era);
        this.pp = pp;
        this.name = "Sustenance";
    }

    @Override
    public void applyEffect(List<Player> players){
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
            int numCharacter = player.countNumCharacters();
            int reqFood = max(numCharacter - player.getNumGatherers() * 3 - player.getFoodDiscount(), 0);

            // Verify they can pay
            if (player.getFood() >= reqFood) {
                player.addFood(-reqFood);
            } else{
                int unfedCharacters = reqFood - player.getFood();
                player.setFood(0);
                int ppLoss = - unfedCharacters * pp;
                player.addPp(ppLoss);
            }
        }
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
