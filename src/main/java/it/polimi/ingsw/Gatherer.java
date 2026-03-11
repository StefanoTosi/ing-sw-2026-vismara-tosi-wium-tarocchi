package it.polimi.ingsw;

public class Gatherer extends Character {
    public Gatherer() {
        this.name = "Gatherer";
    }

    @Override
    public Era getEra() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void applyEffect(Player p) {

    }
}
