package it.polimi.ingsw.model;

import it.polimi.ingsw.networking.TCP.ObserverTCP;

public class FakeTCPObserver implements ObserverTCP {
    public int updateCalls = 0;
    public int closingCalls = 0;

    @Override
    public void update(GameDTO game) {
        updateCalls++;
    }

    @Override
    public void closingGame(GameDTO game) {
        closingCalls++;
    }
}
