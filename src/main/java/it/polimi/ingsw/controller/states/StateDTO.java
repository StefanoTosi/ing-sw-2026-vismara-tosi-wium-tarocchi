package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.IOException;

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
     * Returns the corresponding {@code GameState} object.
     * @param game
     * @return
     * @throws IllegalActionException
     * @throws IOException
     */
    public GameState getNewState(Game game) throws IllegalActionException, IOException {
        return null;
    }
}