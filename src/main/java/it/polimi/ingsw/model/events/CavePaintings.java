package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.events.DTO.CavePaintingsDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * If you have less than x Artists you lose x pps,
 * otherwise if you have more or equal than x Artists you gain x pps
 */

public class CavePaintings extends Event {

    private final int topPp;
    private final int bottomPp;
    private final int minArtist;

    public CavePaintings(int minArtist, Era era, int topPp, int bottomPp){
        super(era);
        this.minArtist = minArtist;
        this.topPp = topPp;
        this.bottomPp = bottomPp;
        this.name = "CavePaintings";
    }

    @Override
    public List<EventResult> applyEffect(List<Player> players){
        List<EventResult> results = new ArrayList<>();
        int deltaPp;
        int deltaFood;
        int prevFood;

        for (Player player : players){
            prevFood = player.getFood();

            //Apply building effects
            for (Building building : player.getBuildings()){
                building.getEffect().applyEffectEventCavePaintings(player, building);
            }
            deltaFood = player.getFood() - prevFood;

            if (player.getNumArtists() < minArtist) {
                player.addPp(-topPp);
                deltaPp = -topPp;
            }else {
                deltaPp = bottomPp * player.getNumArtists();
                player.addPp(deltaPp);
            }

            results.add(new EventResult("CavePaintings", player.toDTO(), deltaPp, deltaFood));
        }

        return results;
    }

    @Override
    public String getName(){
        return name;
    }

    public CavePaintingsDTO toDTO(){
        return new CavePaintingsDTO(getEra().name(), minArtist, topPp, bottomPp, getId());
    }
}