package it.polimi.ingsw.model;

import it.polimi.ingsw.model.effects.Effect;

public abstract class Building implements Card {
    private int cost;
    private int pp;
    private Effect effect;

    public Building(int cost, int pp, Effect effect){
        this.cost = cost;
        this.pp = pp;
        this.effect = effect;
    }

    private String TYPE = "Building";

    @Override
    public String getType (){
        return TYPE;
    }

    public int getPp() { return pp; }
}
