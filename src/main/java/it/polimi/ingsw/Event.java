package it.polimi.ingsw;

public abstract class Event implements Card {
    private String TYPE = "Event";

    @Override
    public String getType (){
        return TYPE;
    }
}
