package it.polimi.ingsw.model.characters.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.CardDTO;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.characters.Hunter;
import java.io.Serializable;

/**
 * Holds information about a {@code Hunter} card in DTO format.
 */
public class HunterDTO extends CardDTO implements Serializable {
    private final boolean icon;

    /**
     * Generates a {@code HunterDTO} object, filled with the specified parameters.
     * @param icon {@code true} if the {@code Hunter} awards food when drawn, {@code false} otherwise
     * @param era the {@code Era} the card belongs to
     * @param id the {@code Card}'s id, used to get the corresponding texture
     */
    @JsonCreator
    public HunterDTO(@JsonProperty("icon") boolean icon, @JsonProperty("era") String era, @JsonProperty("id") int id){
        super(era, "Character", "Hunter", id);
        this.icon = icon;
    }

    @Override
    public StringBuilder[] printCard() {
        StringBuilder[] lines = new StringBuilder[7];

        for(int i=0; i < 7; i++){
            lines[i] = new StringBuilder();
        }

        lines[0].append("+----------+");
        lines[1].append(String.format("|%-10s|", "Hunter"));
        lines[2].append(String.format("|%-10s|", getIcon()));
        lines[3].append("|          |");
        lines[4].append("|          |");
        lines[5].append(String.format("|%-10s|", getEra()));
        lines[6].append("+----------+");

        return lines;
    }

    @Override
    public boolean getIcon() {
        return icon;
    }

    public Hunter fromDTO() {
        return new Hunter(this.icon, Era.valueOf(this.getEra()));
    }
}