package it.polimi.ingsw;

public class Sustenance extends Event {
    private String NAME = "Sustenance";

    @Override
    public Era getEra(){return null;}

    @Override
    public String getName(){
        return NAME;
    }

}
