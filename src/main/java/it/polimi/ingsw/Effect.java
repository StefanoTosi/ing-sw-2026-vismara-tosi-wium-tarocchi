package it.polimi.ingsw;

public abstract class Effect {
    protected int Pp;
    protected int Food;
    protected int Stars;

    public void resolveEffect(){};
    public void applyEffectDraw (Player p){};
    public void applyEffectEvent (Player p){};
    public void applyEffectEndGame (Player p){};
    public void applyEffectEndTurn (Player p){};
    public void applyEffectTileBonus (Player p){};
}
