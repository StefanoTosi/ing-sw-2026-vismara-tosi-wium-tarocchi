package it.polimi.ingsw;

public class Inventor extends Character {
    private int inventionIcon;

    public Inventor(int inventionIcon) {
        this.inventionIcon = inventionIcon;
        this.name = "Inventor";
    }

    public Inventor() {
        this.name = "Inventor";
    }

    public int getInventionIcon() {
        return inventionIcon;
    }

    public void setInventionIcon(int inventionIcon) {
        this.inventionIcon = inventionIcon;
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
