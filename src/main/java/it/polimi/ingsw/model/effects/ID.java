package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Player;

public enum ID {
    D1{
        int food;
        public void applyEffectDraw(Player player, int set){
            if(player.countSets() > set){
                player.addFood(food);
            }
        }
    },
    ES1,
    ESC1,
    ET,
    D2,
    ESC2,
    ESC3,
    EH,
    EG1,
    ECP,
    EG2,
    EG3,
    EG4
}
