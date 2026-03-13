package it.polimi.ingsw;

public class Hunt extends Event {
    private String NAME = "Hunt";
    private int pp;

    @Override
    public Era getEra(){return null;}

    @Override
    public String getName(){
        return NAME;
    }

    public int getPp(){
        return this.pp;
    }
}
