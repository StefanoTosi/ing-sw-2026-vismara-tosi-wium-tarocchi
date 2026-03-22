package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Building;
import it.polimi.ingsw.model.Card;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.DataFormatException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.characters.*;
import it.polimi.ingsw.model.characters.Character;
import it.polimi.ingsw.model.effects.Effect;
import it.polimi.ingsw.model.events.*;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

/**
 * Game board, holds information about all elements present on the board
 */
public class Board {
    private List<Card> topRow;
    private List<Card> bottomRow;

    private Deck deckTribe;
    private Deck deckE1Building;
    private Deck deckE2Building;
    private Deck deckE3Building;

    private List<Tile> tiles;

    /**
     * Generates a starting board given the number of players
     * @param numPlayers
     */
    public Board(int numPlayers) throws IllegalActionException {
        if (numPlayers < 2 || numPlayers > 5) {
            throw new IllegalArgumentException("'numPlayers' must be in the range 2 - 5");
        }
        // Load cards
        List<Card> cards = loadCards(numPlayers);

        // Create decks
        List<Card> tribeI = new ArrayList<>();
        List<Card> tribeII = new ArrayList<>();
        List<Card> tribeIII = new ArrayList<>();

        List<Card> e1Building = new ArrayList<>();
        List<Card> e2Building = new ArrayList<>();
        List<Card> e3Building = new ArrayList<>();

        List<Card> finalEvents = new ArrayList<>();

        // Parse cards.json to load cards
        try {
            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(new File("src/main/resources/it/polimi/ingsw/cards.json"));
            JsonNode cardsNode = root.get("cards");

            for (JsonNode node : cardsNode) {
                Era era = Era.valueOf(node.get("era").asText());
                switch (node.get("role").asText()) {
                    case "Character":
                        Character character;
                        if (node.get("minNumPlayers").asInt() <= numPlayers) {
                            switch (node.get("type").asText()) {
                                case "Artist":
                                    character = new Artist(era);
                                    break;
                                case "Builder":
                                    character = new Builder(node.get("discount").asInt(), node.get("pp").asInt(), era);
                                    break;
                                case "Gatherer":
                                    character = new Gatherer(era);
                                    break;
                                case "Hunter":
                                    character = new Hunter(node.get("foodIcon").asBoolean(), era);
                                    break;
                                case "Inventor":
                                    character = new Inventor(Icon.valueOf((node.get("icon").asText().toUpperCase())), era);
                                    break;
                                case "Shaman":
                                    character = new Shaman(node.get("stars").asInt(), era);
                                    break;
                                default:
                                    throw new DataFormatException("Unrecognized character '" + node.get("type").asText() + "' while parsing cards.json");
                            }

                            // Insert character in the correct tribe deck
                            switch (era) {
                                case I:
                                    tribeI.add(character);
                                    break;
                                case II:
                                    tribeII.add(character);
                                    break;
                                case III:
                                    tribeIII.add(character);
                                    break;
                            }
                        }
                        break;

                    case "Event":
                        Event event;
                        switch (node.get("type").asText()) {
                            case "CavePaintings":
                                event = new CavePaintings(node.get("minNumArtists").asInt(), era);
                                break;
                            case "ShamanicRitual":
                                event = new ShamanicRitual(node.get("winnerPp").asInt(), node.get("loserPp").asInt(), era);
                                break;
                            case "Hunt":
                                event = new Hunt(node.get("pp").asInt(), era);
                                break;
                            case "Sustenance":
                                event = new Sustenance(node.get("pp").asInt(), Era.valueOf(node.get("era").asText()));
                                break;
                            default:
                                throw new DataFormatException("Unrecognized event '" + node.get("type").asText() + "' while parsing cards.json");
                        }

                        if (node.get("final").asBoolean()) {
                            finalEvents.add(event);
                        } else {
                            switch (era) {
                                case I:
                                    tribeI.add(event);
                                    break;
                                case II:
                                    tribeII.add(event);
                                    break;
                                case III:
                                    tribeIII.add(event);
                                    break;
                            }
                        }

                        break;
                    case "Building":
                        //manca il passaggio di effect nel costruttore di building
                        Building building = new Building(node.get("cost").asInt(), node.get("pp").asInt(), null, Era.valueOf(node.get("era").asText()));

                        switch (era) {
                            case I:
                                e1Building.add(building);
                                break;
                            case II:
                                e2Building.add(building);
                                break;
                            case III:
                                e3Building.add(building);
                                break;
                        }
                        break;
                    default:
                        throw new DataFormatException("Unrecognized role '" + node.get("role").asText() + "' while parsing cards.json");
                }
            }
        } catch (Exception e) {
            System.out.println("Error in loading cards from json file");
            e.printStackTrace();
        }

        Deck deckTribeI = new Deck(tribeI);
        Deck deckTribeII = new Deck(tribeII);
        Deck deckTribeIII = new Deck(tribeIII);

        Deck deckE1Building = new Deck(e1Building);
        Deck deckE2Building = new Deck(e2Building);
        Deck deckE3Building = new Deck(e3Building);

        Deck deckFinalEvents = new Deck(finalEvents);

        // Shuffle
        deckTribeI.shuffle();
        deckTribeII.shuffle();
        deckTribeIII.shuffle();

        deckE1Building.shuffle();
        deckE2Building.shuffle();
        deckE3Building.shuffle();

        deckFinalEvents.shuffle();

        // Stack tribe deck
        this.deckTribe = deckFinalEvents.stack(deckTribeIII.stack(deckTribeII.stack(deckTribeI)));
        this.deckE1Building = deckE2Building;
        this.deckE2Building = deckE2Building;
        this.deckE3Building = deckE2Building;

        // Arrange tiles
        // TODO: we need to load tiles

        // Filling top and bottom rows is done in a GameState
    }

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

    public Deck getDeckE1Building() {
        return deckE1Building;
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
    
    public List<Card> loadCards(int numPlayers) {

        return null;
    }
}
