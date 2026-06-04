package it.polimi.ingsw.model.controller;

import it.polimi.ingsw.controller.actions.*;
import it.polimi.ingsw.controller.states.*;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Totem;
import it.polimi.ingsw.model.events.CavePaintings;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Test
    void drawCardState() throws IllegalActionException, IOException{
        // Create from DTO
        Game game = new Game(List.of(new Player("A"), new Player("B")), new Random(1));
        game.getBoard().initialize(2);
        new FillBoardState(game).refillBoard();
        game.getPlayers().get(0).setOffer('B');
        game.getPlayers().get(1).setOffer('C');

        GameState state = StateDTO.DRAWCARD.getNewState(game);

        assertNotNull(state);
        assertTrue(state instanceof DrawCardState);
    }

    @Test
    void endGameState() throws IllegalActionException, IOException {
        Game game = createGame();

        GameState state = StateDTO.ENDGAME.getNewState(game);

        assertNotNull(state);
        assertInstanceOf(EndGameState.class, state);
    }

    @Test
    void endTurnState() throws IllegalActionException, IOException{
        // Create from DTO
        Game game = new Game(List.of(new Player("A"), new Player("B")), new Random(1));
        game.getBoard().initialize(2);
        new FillBoardState(game).refillBoard();

        GameState state = StateDTO.ENDTURN.getNewState(game);

        assertNotNull(state);
        assertTrue(state instanceof EndTurnState);


        // Create from constructor, without crashing
        game.getPlayers().get(0).setCanPickFromTop(true);
        EndTurnState e = new EndTurnState(game);


        // Draw card when not allowed
        assertThrows(IllegalActionException.class, () -> {
            e.drawCardFromTop(game.getPlayers().get(1), 0);
        });

        // Draw card when allowed
        e.drawCardFromTop(game.getPlayers().get(0), 0);

        // Skip when allowed
        e.skipDraw(game.getPlayerTurn());
    }

    @Test
    void fillBoardState() throws IllegalActionException, IOException {
        // Create from DTO
        Game game = createGame();
        game.getBoard().initialize(2);

        GameState state = StateDTO.FILLBOARD.getNewState(game);

        assertNotNull(state);
        assertInstanceOf(FillBoardState.class, state);

        // Create from constructor, without crashing
        FillBoardState f = new FillBoardState(game);

        // Check the board is filled
        f.refillBoard();
        assertFalse(game.getBoard().getTopRowTribe().isEmpty());
        assertFalse(game.getBoard().getBottomRowTribe().isEmpty());

        // Check it transitions when round > 10
        game.setTurnNumber(11);
        f.refillBoard();
        assertInstanceOf(EndGameState.class, game.getState());

    }

    @Test
    void resolveEventState() throws IllegalActionException, IOException {
        // Create from DTO
        Game game = createGame();
        game.getBoard().initialize(2);

        GameState state = StateDTO.RESOLVEEVENT.getNewState(game);

        assertNotNull(state);
        assertInstanceOf(ResolveEventsState.class, state);

        // Create from constructor, without crashing
        game.getBoard().getBottomRowTribe().add(
                new CavePaintings(3, Era.II, 5, 10, 0)
        );
        ResolveEventsState r = new ResolveEventsState(game);
        assertEquals(StateDTO.RESOLVEEVENT, r.getStateDTO());

        // Check events are resolved
        assertEquals(null, game.getEventResults());
        r.resolveEvents();
        assertEquals(1, game.getEventResults().size());
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
        assertThrows(IllegalActionException.class, () -> state.skipDraw(player));

        assertThrows(IllegalActionException.class, state::resolveEvents);
        assertThrows(IllegalActionException.class, state::calculateRankings);
    }

    @Test
    void getStateDTO() {
        GameState state = new DummyState();

        assertEquals(StateDTO.RESOLVEEVENT, state.getStateDTO());
    }

    @Test
    void skipDraw() throws IllegalActionException, IOException {
        Player p1 = new Player("A");
        Player p2 = new Player("A");
        Player p3 = new Player("A");
        Game g = new Game(new ArrayList<Player>(Arrays.asList(p1, p2, p3)), new Random(42));
        g.setNumPlayers(3);
        p1.setGame(g);
        p2.setGame(g);
        p3.setGame(g);

        g.getState().startGame(g);

        g.getState().chooseTotem(p1, Totem.BLUE);
        g.getState().chooseTotem(p2, Totem.ORANGE);
        g.getState().chooseTotem(p3, Totem.PURPLE);

        g.getState().chooseOffer(p2, 'C');
        g.getState().chooseOffer(p3, 'D');
        g.getState().chooseOffer(p1, 'B');

        assertThrows(IllegalActionException.class, () -> {
            g.getState().skipDraw(p1);
        });

        assertThrows(IllegalActionException.class, () -> {
            g.getState().skipDraw(p2);
        });

        assertThrows(IllegalActionException.class, () -> {
            g.getState().skipDraw(p3);
        });

        // Remove all valid cards
        g.getBoard().getBottomRowTribe().clear();
        g.getState().skipDraw(p1);

        // Check the player turn has advanced
        assertEquals(p2, g.getPlayerTurn());
    }
}