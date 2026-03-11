package it.polimi.ingsw;

public abstract class Event implements Card {
    public String TYPE = "Event";

    @Override
    public String getType (){
        return TYPE;
    }
}
