package it.polimi.ingsw;

public interface Card {
    public Era getEra();
    public String getName();
    public String getType();
    public void applyEffect(Player p);
}
