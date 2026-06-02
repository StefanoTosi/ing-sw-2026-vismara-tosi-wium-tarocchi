package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.DTO.ArtistDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

/**
 * Represents the {@code Artist} character.<br>
 * At the end of the game, 10 prestige points are awarded every 2 {@code Artists} in the tribe.
 * During the {@code CavePaintings} event, a minimum number of artists is required in order to win prestige points.
 */
public class Artist extends Character {

    /**
     * Generates an {@code Artist} object, belonging to the specified era.
     * @param era the {@code Era} the card belongs to
     */
    public Artist(Era era, int id) {
        super(era, id);
        this.name = "Artist";
    }

    @Override
    public void addToPlayer(Player player) {
        player.addArtist(this);
    }

    public ArtistDTO toDTO(){
        return new ArtistDTO(getEra().name(), getId());
    }
}
