package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.RemoteException;

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
        public GameState getNewState(Game game) throws IllegalActionException, RemoteException {
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
        public GameState getNewState(Game game) throws IllegalActionException, RemoteException {
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

    public GameState getNewState(Game game) throws IllegalActionException, RemoteException {
        return null;
    }
}
