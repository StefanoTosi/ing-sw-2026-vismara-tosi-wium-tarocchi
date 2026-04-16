package it.polimi.ingsw.model.characters.DTO;

import it.polimi.ingsw.model.CardDTO;
import java.io.Serializable;

public class ArtistDTO extends CardDTO implements Serializable {
    public ArtistDTO(String era) {
        super(era, "Character", "Artist");
    }
}