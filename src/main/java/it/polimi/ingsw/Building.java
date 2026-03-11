package it.polimi.ingsw;

public abstract class Building implements Card {
    public int cost;
    public int pp;

    public String TYPE = "Building";
    @Override
    public String getType (){
        return TYPE;
    }

}
