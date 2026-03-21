package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Player;

public class EffectEndGame extends Effect {
    private int pp;

    public void applyEffectEndGame(Player player){
        //effetto 1 generico
        player.addPp(pp);

        //effetto 2 per set
        player.addPp(6 * player.countSets());

        //effetto 3 per costruttori -> lo implemento a livello di costruttore?

        //effetto 4 -> guadagno tot pp in base a numchar - a livello controller?
    }

    public int getPp() {
        return pp;
    }
}
