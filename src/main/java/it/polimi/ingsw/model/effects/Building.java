package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.Builder;

import java.util.function.Function;

/**
 * Represents the elements of the Building card
 */
public class Building extends Card {
    // Building properties
    private final int cost;
    private final int pp;

    // Effect properties
    protected int effectPp;
    protected Function<Player, Integer> getNumCharacter;
    protected Effect effect;

    /**
     * Represents the elements of the Building card
     * @param era
     * @param cost
     * @param pp
     * @param effectPp
     * @param getNumCharacter
     * @param effect
     */
    public Building(Era era, int cost, int pp, int effectPp, Function<Player, Integer> getNumCharacter, Effect effect) {
        super(era);
        this.cost = cost;
        this.pp = pp;
        this.effectPp = effectPp;
        this.getNumCharacter = getNumCharacter;
        this.effect = effect;
        this.TYPE = "Building";
    }

    /**
     * Adds Building card to player
     * @param player
     */
    @Override
    public void addToPlayer(Player player) {
        player.addBuilding(this);
    }

    /**
     * Returns the cost of the card
      * @return cost
     */

    public int getCost() {
        return cost;
    }

    /**
     * Returns the pp of the card
     * @return pp
     */
    public int getPp() {
        return pp;
    }

    /**
     * Returns the pp of the effect of the card
     * @return effectPp
     */
    public int getEffectPp() {
        return effectPp;
    }

    /**
     * Returns the number of the character needed for the effect
     * @return getNumCharacter
     */
    public Function<Player, Integer> getGetNumCharacter() {
        return getNumCharacter;
    }

    /**
     * Returns the effect associated to the card
     * @return effect
     */
    public Effect getEffect() {
        return effect;
    }

    /**
     * Gets the building class and converts it to a DTO class, so that we can pass the data to the client
     * @return BuildingDTO
     */
    public BuildingDTO toDTO() {
        return new BuildingDTO(getCost(), getPp(), getEffectPp(), getEffect().name(), getEra().name(), getId());
    }

    /**
     * Calculates the cost considering the discount applied by the builders in the players' tribe
     * @param player
     * @return int
     */
    public int discountedCost(Player player) {
        int buildingCost = this.cost;

        //Calculate discount provided by builders in the tribe
        for(Builder b : player.getBuilders()) {
            buildingCost -= b.getFoodDiscount();
        }
        if(buildingCost <= 0) {
            buildingCost = 0;
        }

        return buildingCost;
    }
}