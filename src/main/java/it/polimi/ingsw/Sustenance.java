package it.polimi.ingsw;

public class Sustenance extends Event {
    public String NAME = "Sustenance";

    @Override
    public Era getEra(){return null;}

    @Override
    public String getName(){
        return NAME;
    }
    @Override
    public void applyEffect(Player p){

    }
}
