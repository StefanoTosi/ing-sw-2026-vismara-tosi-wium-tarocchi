package it.polimi.ingsw.model.characters.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.CardDTO;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.characters.Icon;
import it.polimi.ingsw.model.characters.Inventor;
import java.io.Serializable;

/**
 * Holds information about an {@code Inventor} card in DTO format.
 */
public class InventorDTO extends CardDTO implements Serializable {
    private final Icon inventionIcon;

    /**
     * Generates an {@code InventorDTO} object, filled with the specified parameters.
     * @param era the {@code Era} the card belongs to
     * @param icon the invention {@code Icon} on the card
     * @param id the {@code Card}'s id, used to get the corresponding texture
     */
    @JsonCreator
    public InventorDTO(@JsonProperty("era") String era, @JsonProperty("inventionIcon") Icon icon, @JsonProperty("id") int id) {
        super(era, "Character", "Inventor", id);
        this.inventionIcon = icon;
    }

    @Override
    public StringBuilder[] printCard() {
        StringBuilder[] lines = new StringBuilder[7];

        for(int i=0; i < 7; i++){
            lines[i] = new StringBuilder();
        }

            lines[0].append("+----------+");
            lines[1].append(String.format("|%-10s|", "Inventor"));
            lines[2].append(String.format("|%-10s|",getInventionIcon()));
            lines[3].append("|          |");
            lines[4].append("|          |");
            lines[5].append(String.format("|%-10s|", getEra()));
            lines[6].append("+----------+");

        return lines;
    }

    @Override
    public Icon getInventionIcon() {
        return inventionIcon;
    }

    public Inventor fromDTO() {
        return new Inventor(getInventionIcon(), Era.valueOf(this.getEra()));
    }
}