package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.effects.BuildingDTO;

import java.io.Serializable;
import java.util.List;

public class BoardDTO implements Serializable {
    private int numTopRowTribe;
    private List<BuildingDTO> topRowBuilding;
    private int numBottomRowTribe;
    private List<BuildingDTO> bottomRowBuilding;

    private int deckTribe;
    private int deckE1Building;
    private int deckE2Building;
    private int deckE3Building;

    private OrderDTO order;
    private List<OfferDTO> offerPath;

    public BoardDTO(int numTopRowTribe, List<BuildingDTO> topRowBuilding, int numBottomRowTribe, List<BuildingDTO> bottomRowBuilding, int deckTribe, int deckE1Building, int deckE2Building, int deckE3Building, OrderDTO order, List<OfferDTO> offerPath) {
        this.numTopRowTribe = numTopRowTribe;
        this.topRowBuilding = List.copyOf(topRowBuilding);
        this.numBottomRowTribe = numBottomRowTribe;
        this.bottomRowBuilding = List.copyOf(bottomRowBuilding);
        this.deckTribe = deckTribe;
        this.deckE1Building = deckE1Building;
        this.deckE2Building = deckE2Building;
        this.deckE3Building = deckE3Building;
        this.order = order;
        this.offerPath = List.copyOf(offerPath);
    }

    public int getNumTopRowTribe() {
        return numTopRowTribe;
    }

    public List<BuildingDTO> getTopRowBuilding() {
        return topRowBuilding;
    }
    public int getNumBottomRowTribe() {
        return numBottomRowTribe;
    }
    public List<BuildingDTO> getBottomRowBuilding() {
        return bottomRowBuilding;
    }
    public int getDeckTribe() {
        return deckTribe;
    }
    public int getDeckE1Building() {
        return deckE1Building;
    }
    public int getDeckE2Building() {
        return deckE2Building;
    }
    public int getDeckE3Building() {
        return deckE3Building;
    }
    public OrderDTO getOrder() {
        return order;
    }
    public List<OfferDTO> getOfferPath() {
        return offerPath;
    }
}
