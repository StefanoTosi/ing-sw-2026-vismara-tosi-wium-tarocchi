package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characters.DTO.*;
import it.polimi.ingsw.model.effects.BuildingDTO;

import java.io.Serializable;
import java.util.List;

public class PlayerDTO implements Serializable {
    private String name;

    private List<ArtistDTO> artists;
    private List<GathererDTO> gatherers;
    private List<HunterDTO> hunters;
    private List<InventorDTO> inventors;
    private List<ShamanDTO> shamans;
    private List<BuilderDTO> builders;
    private List<BuildingDTO> buildings;

    private int pp;
    private int food;
    private int order;
    private char offer;

    public PlayerDTO(String name, List<ArtistDTO> artists, List<GathererDTO> gatherers, List<HunterDTO> hunters, List<InventorDTO> inventors, List<ShamanDTO> shamans, List<BuilderDTO> builders, List<BuildingDTO> buildings, int pp, int food, int order, char offer) {
        this.name = name;
        this.artists = List.copyOf(artists);
        this.gatherers = List.copyOf(gatherers);
        this.hunters = List.copyOf(hunters);
        this.inventors = List.copyOf(inventors);
        this.shamans = List.copyOf(shamans);
        this.builders = List.copyOf(builders);
        this.buildings = List.copyOf(buildings);
        this.pp = pp;
        this.food = food;
        this.order = order;
        this.offer = offer;
    }

    public String getName() {
        return name;
    }

    public List<ArtistDTO> getArtists() {
        return artists;
    }

    public List<GathererDTO> getGatherers() {
        return gatherers;
    }

    public List<HunterDTO> getHunters() {
        return hunters;
    }

    public List<InventorDTO> getInventors() {
        return inventors;
    }

    public List<ShamanDTO> getShamans() {
        return shamans;
    }

    public List<BuilderDTO> getBuilders() {
        return builders;
    }

    public List<BuildingDTO> getBuildings() {
        return buildings;
    }

    public int getPp() {
        return pp;
    }

    public int getFood() {
        return food;
    }

    public int getOrder() {
        return order;
    }

    public char getOffer() {
        return offer;
    }
}
