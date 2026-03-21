package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Player;

public enum IDEffect {
    D1{
        public void applyEffectDraw (Player player, int food){
            int set = player.countSets();
            if (set > 0){
                player.addFood(food);
            }
        };
    },
    ES1{
        public void applyEffect (Player player){};
    },
    ESC1{
        public void applyEffect (Player player){};
    },
    ET1{
        public void applyEffect (Player player){};
    },
    D2{
        public void applyEffectDraw (Player player, int food){
            if(player.getNumInventors() > 1){
                //to do:
            }
        };
    },
    ESC2{
        public void applyEffect (Player player){};
    },
    ESC3{
        public void applyEffect (Player player){};
    },
    EH{
        public void applyEffect (Player player){};
    },
    EG1{
        public void applyEffect (Player player){};
    },
    ECP{
        public void applyEffect (Player player){};
    },
    EG2{
        public void applyEffect (Player player){};
    },
    EG3{
        public void applyEffect (Player player){};
    },
    ET2{
        public void applyEffect (Player player){};
    },
    EG4{
        public void applyEffect (Player player){};
    }
}
