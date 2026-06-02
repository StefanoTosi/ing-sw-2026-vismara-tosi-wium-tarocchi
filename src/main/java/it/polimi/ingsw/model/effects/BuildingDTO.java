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

    @Override
    public StringBuilder[] printCard(){
        StringBuilder[] lines = new StringBuilder[7];
        for(int i = 0; i < 7; i++){
            lines[i] = new StringBuilder();
        }

        lines[0].append(String.format("+----------+"));
        lines[1].append(String.format("|%-10s|", "Build"));
        lines[2].append(String.format("|%-10s|", getCost()));
        lines[3].append(String.format("|%-10s|", getEffect()));
        if(getEffect().equals("EG3")){
            lines[4].append(String.format("|%-10s|", getEffectCharacter()));
        } else {
            lines[4].append(String.format("|%-10s|", " "));
        }
        lines[5].append(String.format("|%-10s|", getEra()));

        lines[6].append("+----------+");
        return lines;
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
        return new Building(Era.valueOf(this.getEra()), this.cost, this.pp, this.effectPp, null /*TODO*/, this.effectCharacter, Effect.valueOf(this.effect), getId());
    }
}
