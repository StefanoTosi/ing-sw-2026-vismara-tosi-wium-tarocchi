package it.polimi.ingsw.model.effects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EffectEndGameTest {

    @Test
    void applyEffectEndGame() {
        //aggiunge pp a fine gioco

        //aggiunge 6pp per set di personaggio
        //diversi numeri di set, deve ricevere il punteggio giusto
        //set: 0, 1,2,5

        //guadagno doppio dei pp

        //in base a personaggio rappresentato dall'effetto da pp rappresentati a fine gioco
        //per ogni carta di quel tipo
    }

    @Test
    void getPp() {
        EffectEndGame effect = new EffectEndGame(3, IDEffect.EG1);
        assertEquals(3, effect.getPp());
    }
}