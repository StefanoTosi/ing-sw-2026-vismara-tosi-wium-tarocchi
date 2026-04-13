package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.function.Function;

public class BuildingDTO implements Serializable {

    private int cost;
    private int pp;

    private int effectPp;
    private String effect;

    private String era;

    public BuildingDTO(int cost, int pp, int effectPp, String effect, String era) {
        this.cost = cost;
        this.pp = pp;
        this.effectPp = effectPp;
        this.effect = effect;
        this.era = era;
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

    public String getEra() {
        return era;
    }
}
