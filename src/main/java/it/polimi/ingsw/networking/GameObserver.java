package it.polimi.ingsw.networking;

import it.polimi.ingsw.model.Game;

public interface GameObserver {
    void update(Game game);
}
