package it.polimi.ingsw.model.controller;

import it.polimi.ingsw.controller.states.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Totem;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class StateTest{
    Game createGame(){
        return new Game(List.of(new Player("A")), new Random(1));
    }

    @Test
    void chooseOfferState() throws IllegalActionException, IOException {
        Game game = createGame();

        GameState state = StateDTO.CHOOSEOFFER.getNewState(game);

        assertNotNull(state);
        assertInstanceOf(ChooseOfferState.class, state);
    }

    @Test
    void chooseTotemState() throws IllegalActionException, IOException {
        Game game = createGame();

        GameState state = StateDTO.CHOOSETOTEM.getNewState(game);

        assertNotNull(state);
        assertInstanceOf(ChooseTotemState.class, state);
    }
    /*
    @Test
    void drawCardState() throws IllegalActionException, IOException{
        Game game = new Game(List.of(new Player("A"), new Player("B")), new Random(1));

        GameState state = StateDTO.DRAWCARD.getNewState(game);

        assertNotNull(state);
        assertTrue(state instanceof DrawCardState);
    }*/

    @Test
    void endGameState() throws IllegalActionException, IOException {
        Game game = createGame();

        GameState state = StateDTO.ENDGAME.getNewState(game);

        assertNotNull(state);
        assertInstanceOf(EndGameState.class, state);
    }
    /*
    @Test
    void endTurnState() throws IllegalActionException, IOException{
        Game game = new Game(List.of(new Player("A"), new Player("B")), new Random(1));

        GameState state = StateDTO.ENDTURN.getNewState(game);

        assertNotNull(state);
        assertTrue(state instanceof EndTurnState);
    }*/

    @Test
    void fillBoardState() throws IllegalActionException, IOException {
        Game game = createGame();

        GameState state = StateDTO.FILLBOARD.getNewState(game);

        assertNotNull(state);
        assertInstanceOf(FillBoardState.class, state);
    }

    @Test
    void resolveEventState() throws IllegalActionException, IOException {
        Game game = createGame();

        GameState state = StateDTO.RESOLVEEVENT.getNewState(game);

        assertNotNull(state);
        assertInstanceOf(ResolveEventsState.class, state);
    }

    @Test
    void setupGameState() throws IllegalActionException, IOException {
        Game game = createGame();

        GameState state = StateDTO.SETUPGAME.getNewState(game);

        assertNotNull(state);
        assertInstanceOf(SetupGameState.class, state);
    }

    /*
    Testing the abstract class GameState
     */

    private static class DummyState extends GameState {
        @Override
        public it.polimi.ingsw.controller.states.StateDTO getStateDTO() {
            return StateDTO.RESOLVEEVENT;
        }
    }

    @Test
    void ShouldThrowException() {
        GameState state = new DummyState();

        Game game = new Game(List.of(new Player("A")), null);
        Player player = new Player("A");

        assertThrows(IllegalActionException.class, () -> state.registerPlayer(game, player));
        assertThrows(IllegalActionException.class, () -> state.startGame(game));
        assertThrows(IllegalActionException.class, () -> state.chooseTotem(player, Totem.BLUE));

        assertThrows(IllegalActionException.class, state::refillBoard);

        assertThrows(IllegalActionException.class, () -> state.chooseOffer(player, 'A'));

        assertThrows(IllegalActionException.class, () -> state.drawCardFromTop(player, 0));
        assertThrows(IllegalActionException.class, () -> state.drawCardFromBottom(player, 0));

        assertThrows(IllegalActionException.class, state::resolveEvents);
        assertThrows(IllegalActionException.class, state::calculateRankings);
    }

    @Test
    void getStateDTO() {
        GameState state = new DummyState();

        assertEquals(StateDTO.RESOLVEEVENT, state.getStateDTO());
    }
}