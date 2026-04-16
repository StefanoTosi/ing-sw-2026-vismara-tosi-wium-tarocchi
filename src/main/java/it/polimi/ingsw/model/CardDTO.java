package it.polimi.ingsw.model;

import com.fasterxml.jackson.annotation.*;
import it.polimi.ingsw.model.effects.BuildingDTO;

import java.io.Serializable;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "TYPE")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CharacterDTO.class, name = "Character"),
        @JsonSubTypes.Type(value = BuildingDTO.class, name = "Building"),
        @JsonSubTypes.Type(value = EventDTO.class, name = "Event")
})
public class CardDTO implements Serializable {
    private final String era;
    private final String TYPE;

    public CardDTO(String era, String type) {
        this.era = era;
        this.TYPE = type;
    }

    public String getEra() {
        return era;
    }

    public String getType() {
        return TYPE;
    }

    public String getName() {
        return "";
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