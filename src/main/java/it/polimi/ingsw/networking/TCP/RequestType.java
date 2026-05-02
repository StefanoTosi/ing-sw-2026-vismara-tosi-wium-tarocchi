package it.polimi.ingsw.networking.TCP;

import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.RemoteException;

public enum RequestType {
    UPDATE, ADDUSER, JOINGAME, CREATEGAME, EXECUTEACTION, LEAVEMATCH, LEAVEGAME, CLOSEGAME, PING
}
