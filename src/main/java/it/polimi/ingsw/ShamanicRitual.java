package it.polimi.ingsw;

public class ShamanicRitual extends Event {
    private String NAME = "ShamanicRitual";
    private int pp;

    @Override
    public Era getEra(){return null;}

    @Override
    public String getName(){
        return NAME;
    }

}
