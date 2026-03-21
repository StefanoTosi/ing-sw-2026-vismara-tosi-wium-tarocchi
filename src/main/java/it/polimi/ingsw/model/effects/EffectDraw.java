package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Player;

public class EffectDraw extends Effect {

    public EffectDraw(int food, IDEffect id){
        this.food = food;
        this.id = id;
    }

    public void applyEffectDraw(Player player){
        id.applyEffectDraw(player, food);
    }

    public int getFood(){
        return this.food;
    }
}
