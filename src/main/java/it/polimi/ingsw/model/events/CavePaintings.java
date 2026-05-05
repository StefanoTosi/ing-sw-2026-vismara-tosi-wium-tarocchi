package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.events.DTO.CavePaintingsDTO;

import java.util.List;

/**
 * If you have less than x Artists you lose x pps,
 * otherwise if you have more or equal than x Artists you gain x pps
 */

public class CavePaintings extends Event {

    private int topPp;
    private int bottomPp;
    private int minArtist;

    public CavePaintings(int minArtist, Era era, int topPp, int bottomPp){
        super(era);
        this.minArtist = minArtist;
        this.topPp = topPp;
        this.bottomPp = bottomPp;
        this.name = "CavePaintings";
    }

    @Override
    public void applyEffect(List<Player> players){
        for (Player player : players){
            for (Building building : player.getBuildings()){
                building.getEffect().applyEffectEventCavePaintings(player, building);
            }
        }

        for(Player player : players) {
            if(player.getNumArtists() < minArtist){
                player.addPp(-topPp);
            }else {
                player.addPp(bottomPp);
            }
        }
    }

    @Override
    public String getName(){
        return name;
    }

    public CavePaintingsDTO toDTO(){
        return new CavePaintingsDTO(getEra().name(), minArtist, topPp, bottomPp, getId());
    }
}
