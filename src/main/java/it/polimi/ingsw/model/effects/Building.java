package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;

import java.util.function.Function;

/**
 * Represents the elements of the Building card
 */

public class Building extends Card {
    // Building properties
    private int cost;
    private int pp;

    // Effect properties
    protected int effectPp;
    protected Function<Player, Integer> getNumCharacter;
    protected Effect effect;

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

    public BuildingDTO toDTO() {
        return new BuildingDTO(getCost(), getPp(), getEffectPp(), getEffect().name(), getEra().name());
    }
}
