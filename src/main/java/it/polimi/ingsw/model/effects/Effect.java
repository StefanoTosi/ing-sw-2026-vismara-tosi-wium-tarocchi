package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.events.Event;
import it.polimi.ingsw.model.Player;

public abstract class Effect {
    protected int pp;
    protected int food;
    protected int stars;
    protected String ID;

    public void applyEffectDraw (Player player){};
    public void applyEffectEvent (Player player, Event event){};
    public void applyEffectEndGame (Player player){};
    public void applyEffectEndTurn (Player player){};
    public void applyEffectTileBonus (Player player){};
    public int getAdditionalStarts() {
        return 0;
    }
}
