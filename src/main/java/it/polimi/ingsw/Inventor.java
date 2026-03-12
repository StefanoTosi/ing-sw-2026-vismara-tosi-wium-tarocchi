package it.polimi.ingsw;

public class Inventor extends Character {
    private Icon inventionIcon;

    public Inventor(Icon inventionIcon) {
        this.inventionIcon = inventionIcon;
        this.name = "Inventor";
    }

    public Inventor() {
        this.name = "Inventor";
    }

    public Icon getInventionIcon() {
        return inventionIcon;
    }

    @Override
    public Era getEra() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }
}
