package it.polimi.ingsw.model.board;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.CardDTO;
import it.polimi.ingsw.model.effects.BuildingDTO;

import java.io.Serializable;
import java.util.List;

public class BoardDTO implements Serializable {
    private List<CardDTO> topRowTribe;
    private List<BuildingDTO> topRowBuilding;
    private List<CardDTO> bottomRowTribe;
    private List<BuildingDTO> bottomRowBuilding;

    private List<CardDTO> deckTribe;
    private List<BuildingDTO> deckE1Building;
    private List<BuildingDTO> deckE2Building;
    private List<BuildingDTO> deckE3Building;

    private OrderDTO order;
    private List<OfferDTO> offerPath;

    @JsonCreator
    private BoardDTO() {}

    public BoardDTO(List<CardDTO> topRowTribe, List<BuildingDTO> topRowBuilding, List<CardDTO> bottomRowTribe, List<BuildingDTO> bottomRowBuilding,
                    List<CardDTO> deckTribe, List<BuildingDTO> deckE1Building, List<BuildingDTO> deckE2Building, List<BuildingDTO> deckE3Building,
                    OrderDTO order, List<OfferDTO> offerPath) {
        this.topRowTribe = topRowTribe;
        this.topRowBuilding = topRowBuilding;
        this.bottomRowTribe = bottomRowTribe;
        this.bottomRowBuilding = bottomRowBuilding;
        this.deckTribe = deckTribe;
        this.deckE1Building = deckE1Building;
        this.deckE2Building = deckE2Building;
        this.deckE3Building = deckE3Building;
        this.order = order;
        this.offerPath = offerPath;
    }

    public List<CardDTO> getTopRowTribe() {
        return topRowTribe;
    }

    public List<BuildingDTO> getTopRowBuilding() {
        return topRowBuilding;
    }

    public List<CardDTO> getBottomRowTribe() {
        return bottomRowTribe;
    }

    public List<BuildingDTO> getBottomRowBuilding() {
        return bottomRowBuilding;
    }

    public List<CardDTO> getDeckTribe() {
        return deckTribe;
    }

    public List<BuildingDTO> getDeckE1Building() {
        return deckE1Building;
    }

    public List<BuildingDTO> getDeckE2Building() {
        return deckE2Building;
    }

    public List<BuildingDTO> getDeckE3Building() {
        return deckE3Building;
    }

    public OrderDTO getOrder() {
        return order;
    }

    public List<OfferDTO> getOfferPath() {
        return offerPath;
    }

    public Board fromDTO() {
        return new Board(this.topRowTribe.stream().map(CardDTO::fromDTO).toList(),
                this.topRowBuilding.stream().map(BuildingDTO::fromDTO).toList(),
                this.bottomRowTribe.stream().map(CardDTO::fromDTO).toList(),
                this.bottomRowBuilding.stream().map(BuildingDTO::fromDTO).toList(),
                new Deck(this.deckTribe.stream().map(CardDTO::fromDTO).toList(), null),
                new Deck(this.deckE1Building.stream().map(CardDTO::fromDTO).toList(), null),
                new Deck(this.deckE2Building.stream().map(CardDTO::fromDTO).toList(), null),
                new Deck(this.deckE3Building.stream().map(CardDTO::fromDTO).toList(), null),
                (getOrder() != null) ? getOrder().fromDTO() : null,
                (getOfferPath() != null) ? getOfferPath().stream().map(OfferDTO::fromDTO).toList() : null);
    }
}