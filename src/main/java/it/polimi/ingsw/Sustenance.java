package it.polimi.ingsw;

/**
 * pay 1 food for every Character in your clan. If you finish your rations
 * before feeding all your characters you will lose Pps for every unfed Character.
 */

public class Sustenance extends Event {
    private String NAME = "Sustenance";
    private int pp;
    private int numCharacter;

    public Sustenance(int pp){
        this.pp = pp;
    }

    public void applyeffect(Player player){
        numCharacter = player.countNumCharacters();
        int food = player.getFood();
        if(food >= numCharacter){
            player.setFood(food - numCharacter);
        }
        else{
            player.setFood(0);
            int unfedCharacter = numCharacter - food;
            int ppLoss = - unfedCharacter * pp;
            player.addPp(ppLoss);
        }
    }

    public int getPp(){
        return this.pp;
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
