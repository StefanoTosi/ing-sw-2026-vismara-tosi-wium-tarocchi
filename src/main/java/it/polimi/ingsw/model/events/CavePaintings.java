package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.events.DTO.CavePaintingsDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * During this {@code Event}, each player is awarded the amount of prestige points specified in the bottom row of the card
 * if they own at least the indicated number of {@code Artist} cards in their tribe.
 * Otherwise, they lose the amount of prestige points specified in the top row of the card.
 */
public class CavePaintings extends Event {

    private final int topPp;
    private final int bottomPp;
    private final int minArtist;

    /**
     * Generates a {@code CavePaintings} object, filled with the specified parameters.
     * @param minArtist the minimum number of {@code Artists} the players are required in order to win pps
     * @param era the {@code Era} the card belongs to
     * @param topPp the amount of prestige points players are deducted if they do not own enough {@code Artists}. This parameter is required to be positive.
     * @param bottomPp the amount of prestige point awarded to players who own enough {@code Artists}
     */
    public CavePaintings(int minArtist, Era era, int topPp, int bottomPp){
        super(era);
        this.minArtist = minArtist;
        this.topPp = topPp;
        this.bottomPp = bottomPp;
        this.name = "CavePaintings";
    }

    /**
     * Resolves the {@code Event} effect. Each player in the specified list wins pps if they have enough {@code Artists}, otherwise they lose pps.
     * @param players the list of {@code Players} involved in the {@code Event}
     * @return the list of {@code EventResults}, containing the food & pps deltas for each player
     */
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

            results.add(new EventResult("CavePaintings", this.id, player.toDTO(), deltaPp, deltaFood));
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