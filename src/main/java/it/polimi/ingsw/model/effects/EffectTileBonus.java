package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Player;

public class EffectTileBonus extends Effect {

    public EffectTileBonus(IDEffect id) {
        this.id = id;
    }

    public void applyEffectTileBonus(Player player){
        id.applyEffect(player, 1, 0, 0);
    }
}