package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Building;
import it.polimi.ingsw.model.Card;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.characters.*;
import it.polimi.ingsw.model.effects.Effect;
import it.polimi.ingsw.model.events.CavePaintings;
import it.polimi.ingsw.model.events.Hunt;
import it.polimi.ingsw.model.events.ShamanicRitual;
import it.polimi.ingsw.model.events.Sustenance;

/**
 * Game board, holds information about all elements present on the board
 */
public class Board {
    private List<Card> topRow;
    private List<Card> bottomRow;

    private Deck deckTribe;
    private Deck deckE2Building;
    private Deck deckE3Building;

    private List<Tile> tiles;

    /**
     * Generates a starting board given the number of players
     * @param numPlayers
     */
    public Board(int numPlayers) {}

    public List<Card> getTopRow() {
        return topRow;
    }

    /**
     * Picks the card in the top row at position pos
     */
    public Card drawFromTopRow(int pos) throws IllegalArgumentException {
        if (pos < 0 || pos >= topRow.size()) {
            throw new IllegalArgumentException("'pos' is not a valid index");
        }
        return topRow.remove(pos);
    }

    public List<Card> getBottomRow() {
        return bottomRow;
    }

    /**
     * Picks the card in the bottom row at position pos
     */
    public Card drawFromBottomRow(int pos) throws IllegalArgumentException {
        if (pos < 0 || pos >= topRow.size()) {
            throw new IllegalArgumentException("'pos' is not a valid index");
        }
        return bottomRow.remove(pos);
    }

    public Deck getDeckTribe() {
        return deckTribe;
    }

    public Deck getDeckE2Building() {
        return deckE2Building;
    }

    public Deck getDeckE3Building() {
        return deckE3Building;
    }

    public List<Tile> getTiles() {
        return tiles;
    }
    
    private List<Card> loadCards(int numPlayers) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(new File("cards.json"));
            JsonNode cardsNode = root.get("cards");
            List<Card> cards = new ArrayList<>();

            for (JsonNode node : cardsNode) {
                if (node.get("minNumPlayers").asInt() <= numPlayers) {

                    switch (node.get("role").asText()) {
                        case "Character":
                            switch ((node.get("type").asText()).toLowerCase()) {
                                case "artist":
                                    Artist artist = new Artist(Era.valueOf(node.get("era").asText()));
                                    cards.add(artist);
                                    break;
                                case "builder":
                                    Builder builder = new Builder(node.get("discount").asInt(), node.get("pp").asInt(), Era.valueOf(node.get("era").asText()));
                                    cards.add(builder);
                                    break;
                                case "gatherer":
                                    Gatherer gatherer = new Gatherer(Era.valueOf(node.get("era").asText()));
                                    cards.add(gatherer);
                                    break;
                                case "hunter":
                                    Hunter hunter = new Hunter(node.get("foodIcon").asBoolean(), Era.valueOf(node.get("era").asText()));
                                    cards.add(hunter);
                                    break;
                                case "inventor":
                                    Inventor inventor = new Inventor(Icon.valueOf((node.get("icon").asText().toUpperCase())), Era.valueOf(node.get("era").asText()));
                                    cards.add(inventor);
                                    break;
                                case "shaman":
                                    Shaman shaman = new Shaman(node.get("stars").asInt(), Era.valueOf(node.get("era").asText()));
                                    cards.add(shaman);
                                    break;
                            }
                            break;
                        case "Event":
                            switch ((node.get("type").asText())) {
                                case "CavePaintings":
                                    CavePaintings cavePainting = new CavePaintings(node.get("minNumArtists").asInt(), Era.valueOf(node.get("era").asText()));
                                    cards.add(cavePainting);
                                    break;
                                case "ShamanicRitual":
                                    ShamanicRitual shamanicRitual = new ShamanicRitual(node.get("pp").asInt(), Era.valueOf(node.get("era").asText()));
                                    cards.add(shamanicRitual);
                                    break;
                                case "Hunt":
                                    Hunt hunt = new Hunt(node.get("pp").asInt(), Era.valueOf(node.get("era").asText()));
                                    cards.add(hunt);
                                    break;
                                case "Sustenance":
                                    Sustenance sustenance = new Sustenance(node.get("pp").asInt(), Era.valueOf(node.get("era").asText()));
                                    cards.add(sustenance);
                                    break;
                            }
                            break;
                        case "Building":
                            //manca il passaggio di effect nel costruttore di building
                            Building building = new Building(node.get("cost").asInt(), node.get("pp").asInt(), null, Era.valueOf(node.get("era").asText()));
                            cards.add(building);
                            break;
                    }
                }
            }
            return cards;

        } catch (Exception e) {
            System.out.println("Error in loading cards from json file");
            e.printStackTrace();
        }
        return null;
    }
}
