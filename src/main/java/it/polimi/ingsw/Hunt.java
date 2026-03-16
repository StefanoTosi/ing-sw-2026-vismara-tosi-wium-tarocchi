package it.polimi.ingsw;

/**
 * Get +1 food and the Pps on the card for every Hunter in your clan
 */

public class Hunt extends Event {
    private String NAME = "Hunt";
    private int pp;

    public Hunt(int pp){
        this.pp = pp;
    }

    public void applyeffect(Player player){
        int numHunter = player.getNumHunters();
        player.addFood(numHunter);
        int ppToAdd = numHunter * getPp();
        player.addPp(ppToAdd);
    }

    public int getPp(){
        return this.pp;
    }

    public void applyEffect(Player player){

    }
    /**
     * Event doesn't have an era
     * @return null
     */
    @Override
    public Era getEra(){return null;}

    @Override
    public String getName(){
        return NAME;
    }

}
