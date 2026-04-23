package it.polimi.ingsw.model;

import com.fasterxml.jackson.annotation.*;
import it.polimi.ingsw.model.characters.DTO.*;
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

        @JsonSubTypes.Type(value = CavePaintingsDTO.class, name = "Cave Paintings"),
        @JsonSubTypes.Type(value = HuntDTO.class, name = "Hunt"),
        @JsonSubTypes.Type(value = ShamanicRitualDTO.class, name = "Shamanic Ritual"),
        @JsonSubTypes.Type(value = SustenanceDTO.class, name = "Sustenance")
})
public class CardDTO implements Serializable {
    private final String era;
    private final String type;
    private final String name;

    @JsonCreator
    public CardDTO(@JsonProperty("era") String era, @JsonProperty("type") String type, @JsonProperty("name") String name) {
        this.era = era;
        this.type = type;
        this.name = name;
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

    public String getEffect() {
        return "";
    }

    public int getFoodDiscount() {
        return 0;
    }

    public boolean getIcon() {
        return false;
    }

    public String getInventionIcon() {
        return "";
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
}