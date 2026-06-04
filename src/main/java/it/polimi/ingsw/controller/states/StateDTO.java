package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.IOException;

/**
 * Enumeration of all possible game states in DTO form.
 * <p>
 * This enum is used to serialize/deserialize the current game state across
 * network boundaries and to reconstruct the correct {@link GameState}
 * implementation when needed.
 * </p>
 *
 * <p>
 * Each constant acts as a factory for its corresponding concrete state.
 * </p>
 */
public enum StateDTO {
    CHOOSEOFFER {
        @Override
        public GameState getNewState(Game game) {
            return new ChooseOfferState(game);
        }
    },
    CHOOSETOTEM {
        @Override
        public GameState getNewState(Game game)  {
            return new ChooseTotemState(game);
        }
    },
    DRAWCARD {
        @Override
        public GameState getNewState(Game game) throws IllegalActionException, IOException {
            return new DrawCardState(game);
        }
    },

    ENDGAME {
        @Override
        public GameState getNewState(Game game) {
            return new EndGameState(game);
        }
    },
    ENDTURN {
        @Override
        public GameState getNewState(Game game) throws IllegalActionException, IOException {
            return new EndTurnState(game);
        }
    },
    FILLBOARD {
        @Override
        public GameState getNewState(Game game) {
            return new FillBoardState(game);
        }
    },
    RESOLVEEVENT {
        @Override
        public GameState getNewState(Game game) {
            return new ResolveEventsState(game);
        }
    },
    SETUPGAME {
        @Override
        public GameState getNewState(Game game) {
            return new SetupGameState();
        }
    };

    /**
     * Factory method that returns the concrete {@link GameState}
     * corresponding to this DTO.
     *
     * @param game the game instance to bind the state to
     * @return a concrete GameState instance
     * @throws IllegalActionException if state construction fails (rare)
     * @throws IOException if IO-related initialization fails
     */
    public abstract GameState getNewState(Game game) throws IllegalActionException, IOException;
}