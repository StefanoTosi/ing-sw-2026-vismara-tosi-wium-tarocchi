package it.polimi.ingsw.model.effects;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.CardDTO;
import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Simple data version of Building class (no logic)
 */
public class BuildingDTO extends CardDTO implements Serializable {
    private final int cost;
    private final int pp;
    private final int effectPp;
    private final String effect;

    @JsonCreator
    public BuildingDTO() {
        this.cost = 0;
        this.pp = 0;
        this.effectPp = 0;
        this.effect = "";
    }

    public BuildingDTO(int cost, int pp, int effectPp, String effect, String era) {
        this.cost = cost;
        this.pp = pp;
        this.effectPp = effectPp;
        this.effect = effect;
        super(era, "Building", "Building");
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

    public String getEffect() {
        return effect;
    }
}
