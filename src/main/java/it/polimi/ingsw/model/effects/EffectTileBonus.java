package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Effect;
import it.polimi.ingsw.model.Player;

public class EffectTileBonus extends Effect {

    public void applyEffectTileBonus(Player player){
        player.addFood(1);
    }
}