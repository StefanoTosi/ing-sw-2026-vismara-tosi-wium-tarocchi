package it.polimi.ingsw;

public abstract class Event implements Card {
    protected String TYPE = "Event";
    protected String name;
    protected Era era;

    @Override
    public String getType (){
        return TYPE;
    }

    public String toString() {
        return "name: " + name + "\ntype: " + TYPE;
    }
}
