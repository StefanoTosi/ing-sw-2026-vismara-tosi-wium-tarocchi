package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Player;

import java.util.function.Function;

public class EffectEndGame extends Effect {
    private int pp;
    private Function<Player, Integer> getNumCharacter;


    public EffectEndGame(int pp, IDEffect id) {
        this.pp = pp;
        this.id = id;
    }

    public void applyEffectEndGame(Player player){
        id.applyEffect(player, 0, pp, 0);
    }

    public int getPp() {
        return pp;
    }
}
