package it.polimi.ingsw.model;

import it.polimi.ingsw.model.effects.Effect;

public class Building extends Card {
    private int cost;
    private int pp;
    private Effect effect;

    public Building(int cost, int pp, Effect effect, Era era){
        super(era);
        this.cost = cost;
        this.pp = pp;
        this.effect = effect;
        this.TYPE = "Building";
    }

    @Override
    public void addToPlayer(Player player) {
        player.addBuilding(this);
    }

    public int getPp() { return pp; }
}
