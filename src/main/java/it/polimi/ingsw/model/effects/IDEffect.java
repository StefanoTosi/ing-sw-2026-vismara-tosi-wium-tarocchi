package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.Inventor;

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
        public void applyEffect (Player player, int food, int pp, int stars){
            //da capire in base a come vogliamo gestire l'opzione di più personaggi
        };
    },
    ESC1{
        public void applyEffect (Player player, int food, int pp, int stars){
            //gestito a livello evento sciamanico
        };
    },
    ET1{
        public void applyEffect (Player player, int food, int pp, int stars){
            player.addFood(1);
        };
    },
    D2{
        public void applyEffect (Player player, int food, int pp, int stars){
            if(player.getNumInventors() % 2 == 0){
            }
        };
    },
    ESC2{
        public void applyEffect (Player player, int food, int pp, int stars){
            // gestito a livello evento Shamanico
        };
    },
    ESC3{
        public void applyEffect (Player player, int food, int pp, int stars){
            // gestito a livello evento Shamanico
        };
    },
    EH{
        public void applyEffect (Player player, int food, int pp, int stars){
            // gestito a livello Hunt
        };
    },
    EG1{
        public void applyEffect (Player player, int food, int pp, int stars){
            player.addPp(player.countBuildersPp());
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
            //Se creassi un ID per ogni personaggio? o sembra uno switch gigante dopo
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
