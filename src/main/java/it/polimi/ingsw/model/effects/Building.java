package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.Builder;

import java.util.function.Function;

/**
 * Represents the {@code Building} card.<p>
 * In order to draw a {@code Building}, players must pay their cost in food.
 * At the end of the game, each {@code Building} awards a certain amount of prestige points to the player who owns it.
 * <p>
 * Each {@code Building} has an {@link Effect}, which can allow the player special actions or grant them additional resources in different phases of the game.
 */
public class Building extends Card {
    // Building properties
    private final int cost;
    private final int pp;

    // Effect properties
    protected int effectPp;
    protected Function<Player, Integer> getNumCharacter;
    protected String effectCharacter = null;
    protected Effect effect;

    /**
     * Generates a {@code Building} object, filled with the specified parameters.
     * @param era the {@code Era} the card belongs to
     * @param cost the {@code Building}'s food cost
     * @param pp
     * @param effectPp
     * @param getNumCharacter
     * @param effect
     */
    public Building(Era era, int cost, int pp, int effectPp, Function<Player, Integer> getNumCharacter, String effectCharacter, Effect effect) {
        super(era);
        this.cost = cost;
        this.pp = pp;
        this.effectPp = effectPp;
        this.getNumCharacter = getNumCharacter;
        this.effectCharacter = effectCharacter;
        this.effect = effect;
        this.TYPE = "Building";
    }

    @Override
    public void addToPlayer(Player player) {
        player.addBuilding(this);
    }

    public int getCost() {
        return cost;
    }

    public int getPp() {
        return pp;
    }

    public int getEffectPp() {
        return effectPp;
    }

    public Function<Player, Integer> getGetNumCharacter() {
        return getNumCharacter;
    }

    public Effect getEffect() {
        return effect;
    }

    public String getEffectCharacter() {
        return effectCharacter;
    }

    /**
     * Converts the current {@code Building} object into DTO format.
     * @return the corresponding {@code BuildingDTO} object
     */
    public BuildingDTO toDTO() {
        return new BuildingDTO(getCost(), getPp(), getEffectPp(), getEffectCharacter(), getEffect().name(), getEra().name(), getId());
    }

    /**
     * Calculates the {@code Building}'s cost, considering the possible discount applied by the {@code Builders} in the player's tribe.
     * @param player the {@code Player} who is trying to draw the card
     * @return the resulting cost after subtracting the discount provided by {@code Builders}
     */
    public int discountedCost(Player player) {
        int buildingCost = this.cost;

        //Calculate discount provided by builders in the tribe
        for (Builder b : player.getBuilders()) {
            buildingCost -= b.getFoodDiscount();
        }
        if (buildingCost <= 0) {
            buildingCost = 0;
        }

        return buildingCost;
    }
}