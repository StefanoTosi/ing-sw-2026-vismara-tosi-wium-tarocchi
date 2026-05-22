package it.polimi.ingsw.networking.TCP;

/**
 * Enum representing all possible TCP request types exchanged
 * between client and server.
 *
 * Each value corresponds to a specific operation in the game protocol.
 * Used for request routing / dispatching on the server side.
 */
public enum RequestType {
    UPDATE, ADDUSER, JOINGAME, CREATEGAME, EXECUTEACTION, LEAVEMATCH, LEAVEGAME, CLOSEGAME, PING, PRINTLEADERBOARD
}