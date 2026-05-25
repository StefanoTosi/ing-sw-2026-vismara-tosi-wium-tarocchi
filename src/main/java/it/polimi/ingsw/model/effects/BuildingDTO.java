package it.polimi.ingsw.model.effects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.CardDTO;
import it.polimi.ingsw.model.Era;
import java.io.Serializable;

/**
 * Simple data version of Building class (no logic)
 */
public class BuildingDTO extends CardDTO implements Serializable {
    private final int cost;
    private final int pp;
    private final int effectPp;
    private final String effectCharacter;
    private final String effect;

    @JsonCreator
    public BuildingDTO(@JsonProperty("cost") int cost, @JsonProperty("pp") int pp, @JsonProperty("effectPp") int effectPp, @JsonProperty("effectCharacter") String effectCharacter, @JsonProperty("effect") String effect, @JsonProperty("era") String era, @JsonProperty("id") int id) {
        this.cost = cost;
        this.pp = pp;
        this.effectPp = effectPp;
        this.effectCharacter = effectCharacter;
        this.effect = effect;
        super(era, "Building", "Building", id);
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

    public String getEffectCharacter() {
        return effectCharacter;
    }

    public Building fromDTO() {
        return new Building(Era.valueOf(this.getEra()), this.cost, this.pp, this.effectPp, null /*TODO*/, this.effectCharacter, Effect.valueOf(this.effect));
    }
}
