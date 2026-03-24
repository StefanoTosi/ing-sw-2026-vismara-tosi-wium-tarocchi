package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;

public class Building extends Card {
    // Building properties
    private int cost;
    private int pp;

    // Effect properties
    protected int effectPp;
    protected Effect effect;

    public Building(Era era, int cost, int pp, int effectPp, Effect effect) {
        super(era);
        this.cost = cost;
        this.pp = pp;
        this.effectPp = effectPp;
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

    public Effect getEffect() {
        return effect;
    }
}
