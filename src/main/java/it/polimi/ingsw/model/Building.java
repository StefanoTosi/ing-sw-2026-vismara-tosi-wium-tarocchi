package it.polimi.ingsw.model;

public abstract class Building implements Card {
    protected int cost;
    protected int pp;

    private String TYPE = "Building";

    @Override
    public String getType (){
        return TYPE;
    }

    public int getPp() { return pp; }
}
