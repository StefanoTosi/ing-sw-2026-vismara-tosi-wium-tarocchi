package it.polimi.ingsw.model.effects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    public BuildingDTO(@JsonProperty("cost") int cost, @JsonProperty("pp") int pp, @JsonProperty("effectPp") int effectPp, @JsonProperty("effect") String effect, @JsonProperty("era") String era) {
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
