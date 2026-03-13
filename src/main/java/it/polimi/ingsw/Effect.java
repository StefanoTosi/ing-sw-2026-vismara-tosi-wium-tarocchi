package it.polimi.ingsw;

public abstract class Effect {
    protected int pp;
    protected int food;
    protected int stars;

    public void applyEffectDraw (Player player){};
    public void applyEffectEvent (Player player){};
    public void applyEffectEndGame (Player player){};
    public void applyEffectEndTurn (Player player){};
    public void applyEffectTileBonus (Player player){};
}
