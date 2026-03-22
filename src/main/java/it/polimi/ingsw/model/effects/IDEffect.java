package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Player;

public enum IDEffect {
    D1{
        public void applyEffect (Player player, int food, int pp, int stars){
            int set = player.countSets();
            if (set > 0){
                player.addFood(food);
            }
        };
    },
    ES1{
        public void applyEffect (Player player, int food, int pp, int stars){};
    },
    ESC1{
        public void applyEffect (Player player, int food, int pp, int stars){};
    },
    ET1{
        public void applyEffect (Player player, int food, int pp, int stars){
            player.addFood(1);
        };
    },
    D2{
        public void applyEffect (Player player, int food, int pp, int stars){
            if(player.getNumInventors() > 1){
                //to do:
            }
        };
    },
    ESC2{
        public void applyEffect (Player player, int food, int pp, int stars){};
    },
    ESC3{
        public void applyEffect (Player player, int food, int pp, int stars){};
    },
    EH{
        public void applyEffect (Player player, int food, int pp, int stars){};
    },
    EG1{
        public void applyEffect (Player player, int food, int pp, int stars){
            //effetto 3 per costruttori -> lo implemento a livello di costruttore?
        };
    },
    ECP{
        public void applyEffect (Player player, int food, int pp, int stars){};
    },
    EG2{
        public void applyEffect (Player player, int food, int pp, int stars){
            player.addPp(6 * player.countSets());
        };
    },
    EG3{
        public void applyEffect (Player player, int food, int pp, int stars){
            //effetto 4 -> guadagno tot pp in base a numchar - a livello controller?
        };
    },
    ET2{
        public void applyEffect (Player player, int food, int pp, int stars){
            //the only method in EffectEndTurn, no need for enumeration
            //will never call it
        };
    },
    EG4{
        public void applyEffect (Player player, int food, int pp, int stars){
            player.addPp(pp);
        };
    };

    public abstract void  applyEffect (Player player, int food, int pp, int stars);
}
