package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.events.Event;

public enum Effect {
    D1 {
        @Override
        public void applyEffectDraw (Player player, Building building) {
            int set = player.countSets();
            if (set > 0){
                player.addFood(5);
            }
        }
    },

    /** Event sustenance:
     * During the Sustenance Event, you have a discount of 1 Food token on the total you would have to pay,
     * for each of the indicated Characters in your tribe (Artists/Inventors/Gatherers).
     */
    ES1 {
        @Override
        public void applyEffectEventSustenance (Player player, Building building) {
            player.setFoodDiscount(building.getNumCharacter.apply(player));
        }
    },

    /**
     * Event shamanic:
     * During the Shamanic Ritual Event, you do not lose Prestige Points if you have fewer star icons
     * than all other players.
     */
    ESC1 {
        @Override
        public void whenDrawn (Player player) {
            player.setDontLosePp(true);
        }
    },

    /**
     * Tile bonus:
     * If at the end of your turn (also during the last round), when you move your Totem back to
     * the Turn Order tile, you place it in a space that provides a bonus in Food, you immediately
     * take 1 additional Food token. If you place the Totem in the last space, you pay 1 Food token
     * normally, and the building has no effect.
     */
    ET1 {
        // Must be executed after players have moved back to the order tile
        @Override
        public void applyEffectTileBonus (Player player, Building building) {
            if (player.getGame().getBoard().getOrder().getFoodBonus(/*TODO: player position on the ordr tile*/ 0) > 0) {
                player.addFood(1);
            }
        }
    },

    D2 {
        @Override
        public void applyEffectDraw (Player player, Building building) {
            if (player.getNumInventors() > 1){
                //to do:
            }
        }
    },

    /**
     * Event shamanic:
     * During the Shamanic Ritual Event, your tribe has 3 additional star icons
     */
    ESC2 {
        @Override
        public void whenDrawn (Player player) {
            player.addAdditionalStars(3);
        }
    },

    /**
     * Event shamanic:
     * During the Shamanic Ritual Event, if you have more  icons than any of the other players,
     * you gain double the indicated Prestige Points. You still gain Prestige Points in case of a tie.
     */
    ESC3 {
        @Override
        public void applyEffectEventShamanicRitual (Player player, Building building) {
            // Assuming this is a shamanic event
            // This needs to be handled only by the controller. Add a counter like above?

        }
    },

    /**
     * Event hunt:
     * During the Hunt Event, you take 1 Food token and gain 1 additional Prestige Point for each
     * Hunter in your tribe.
     */
    EH {
        @Override
        public void applyEffectEventHunt (Player player, Building building) {
            player.addFood(player.getNumHunters());
            player.addPp(player.getNumHunters());
        }
    },

    EG1 {
        @Override
        public void applyEffectEndGame (Player player, Building building) {
            //effetto 3 per costruttori -> lo implemento a livello di costruttore?
        }
    },

    /**
     * Event cave painting:
     * During the Cave Paintings Event, you take 1 Food token for each Artist in your tribe.
     */
    ECP {
        @Override
        public void applyEffectEventCavePaintings (Player player, Building building) {
            player.addFood(player.getNumArtists());
        }
    },

    EG2 {
        @Override
        public void applyEffectEndGame (Player player, Building building) {
            player.addPp(6 * player.countSets());
        }
    },

    EG3 {
        @Override
        public void applyEffectEndGame (Player player, Building building) {
            //effetto 4 -> guadagno tot pp in base a numchar - a livello controller?
        }
    },

    ET2 {
        @Override
        public void applyEffectEndTurn (Player player, Building building) {
            //the only method in EffectEndTurn, no need for enumeration
            //will never call it
        }
    },

    EG4 {
        @Override
        public void applyEffectEndGame (Player player, Building building) {
            player.addPp(building.getEffectPp());
        }
    };

    public void applyEffectEventCavePaintings (Player player, Building building) {}
    public void applyEffectEventHunt (Player player, Building building) {}
    public void applyEffectEventShamanicRitual (Player player, Building building) {}
    public void applyEffectEventSustenance (Player player, Building building) {}

    public void applyEffectDraw (Player player, Building building) {}
    public void applyEffectEndGame (Player player, Building building) {}
    public void applyEffectEndTurn (Player player, Building building) {}
    public void applyEffectTileBonus (Player player, Building building) {}

    public void whenDrawn (Player player) {}
}
