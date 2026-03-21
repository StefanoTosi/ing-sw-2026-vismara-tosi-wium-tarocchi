package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;

public class Inventor extends Character {
    private Icon inventionIcon;

    public Inventor(Icon inventionIcon, Era era) {
        this.inventionIcon = inventionIcon;
        this.name = "Inventor";
        this.era = era;
    }

    public Icon getInventionIcon() {
        return inventionIcon;
    }

    /**
     * Characters doesn't have an era
     * @return null
     */
    @Override
    public Era getEra() {
        return era;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Add an Inventor to the tribe of the specified player
     * @param player
     */
    @Override
    public void addToPlayer(Player player) {
        player.addInventor(this);
    }
}
