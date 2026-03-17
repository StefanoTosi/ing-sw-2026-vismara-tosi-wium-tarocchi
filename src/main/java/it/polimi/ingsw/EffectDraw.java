package it.polimi.ingsw;

public class EffectDraw extends Effect{

    public EffectDraw(int food){
        this.food = food;
    }

    public void applyEffectDraw(Player player){
        int set = player.countSets();
        if (set > 0){
            player.addFood(food);
        }
        if(player.getNumInventors() > 1){
            //to do:
        }
    }


    public int getFood(){
        return this.food;
    }
}
