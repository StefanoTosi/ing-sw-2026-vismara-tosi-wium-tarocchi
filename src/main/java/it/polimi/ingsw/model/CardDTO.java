package it.polimi.ingsw.model;

import com.fasterxml.jackson.annotation.*;
import it.polimi.ingsw.model.characters.DTO.*;
import it.polimi.ingsw.model.characters.Icon;
import it.polimi.ingsw.model.effects.BuildingDTO;
import it.polimi.ingsw.model.events.DTO.*;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "name",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ArtistDTO.class, name = "Artist"),
        @JsonSubTypes.Type(value = BuilderDTO.class, name = "Builder"),
        @JsonSubTypes.Type(value = GathererDTO.class, name = "Gatherer"),
        @JsonSubTypes.Type(value = HunterDTO.class, name = "Hunter"),
        @JsonSubTypes.Type(value = InventorDTO.class, name = "Inventor"),
        @JsonSubTypes.Type(value = ShamanDTO.class, name = "Shaman"),

        @JsonSubTypes.Type(value = BuildingDTO.class, name = "Building"),

        @JsonSubTypes.Type(value = CavePaintingsDTO.class, name = "CavePaintings"),
        @JsonSubTypes.Type(value = HuntDTO.class, name = "Hunt"),
        @JsonSubTypes.Type(value = ShamanicRitualDTO.class, name = "ShamanicRitual"),
        @JsonSubTypes.Type(value = SustenanceDTO.class, name = "Sustenance")
})

/**
 * Holds information about a {@code Card} in DTO format.
 */
public class CardDTO implements Serializable {
    private final String era;
    private final String type;
    private final String name;
    private final int id;

    /**
     * Generates a {@code CardDTO} object, filled with the specified parameters.
     * @param era the era the card belongs to
     * @param type the {@code String} identifying the card's subtype
     * @param name the {@code String} further specifying a deeper subtype, such as {@code Artist} or {@code Sustenance}
     * @param id number which identifies the card, used to get the corresponding texture
     */
    @JsonCreator
    public CardDTO(@JsonProperty("era") String era, @JsonProperty("type") String type, @JsonProperty("name") String name, @JsonProperty("id") int id) {
        this.era = era;
        this.type = type;
        this.name = name;
        this.id = id;
    }

    /**
     * Converts the current {@code CardDTO} object into the corresponding {@code Card}.
     * @return the {@code Card} containing the same data as the current DTO object
     */
    public Card fromDTO() {
        return null;
    }

    /**
     * Prints the current {@code Card} and its variables.
     * @return a {@code String} to visualize the card and its data
     */
    public StringBuilder[] printCard(){
        return new StringBuilder[0];
    }

    public String getEra() {
        return era;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return 0;
    }

    public int getPp() {
        return 0;
    }

    public int getEffectPp() {
        return 0;
    }

    public String getEffectCharacter() {
        return null;
    }

    public String getEffect() {
        return "";
    }

    public int getFoodDiscount() {
        return 0;
    }

    public boolean getIcon() {
        return false;
    }

    public Icon getInventionIcon() {
        return Icon.ARROW;
    }

    public int getStars() {
        return 0;
    }

    public int getMinArtist() {
        return 0;
    }

    public int getTopPp() {
        return 0;
    }

    public int getBottomPp() {
        return 0;
    }

    public int getWinnerPp() {
        return 0;
    }

    public int getLoserPp() {
        return 0;
    }

    public int getId() {
        return id;
    }
}