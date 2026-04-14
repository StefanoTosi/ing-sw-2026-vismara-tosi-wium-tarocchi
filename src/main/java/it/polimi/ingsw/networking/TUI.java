package it.polimi.ingsw.networking;
import it.polimi.ingsw.controller.actions.ChooseOfferAction;
import it.polimi.ingsw.controller.states.ChooseOfferState;
import it.polimi.ingsw.controller.states.GameState;
import it.polimi.ingsw.model.CardDTO;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.PlayerDTO;
import it.polimi.ingsw.model.board.BoardDTO;
import it.polimi.ingsw.model.characters.DTO.ArtistDTO;
import it.polimi.ingsw.model.characters.DTO.GathererDTO;
import it.polimi.ingsw.model.characters.DTO.HunterDTO;
import it.polimi.ingsw.model.characters.DTO.InventorDTO;
import it.polimi.ingsw.model.effects.BuildingDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Gatherer;

public class TUI implements UIObserver {
    private Scanner in = new Scanner(System.in);
    private GameDTO game;
    private final ClientRMI client;
    private final Controller controller;

    public TUI () throws RemoteException, NotBoundException {
        this.client = new ClientRMI(this);
        this.controller = client.connectToServer();
        this.game = null;
    }

    public void start() throws RemoteException, IllegalActionException {
        System.out.println("MENU:");
        System.out.println("1. Login");
        System.out.println("2. Exit");

        int input = Integer.parseInt(in.nextLine());

        switch (input) {
            case 1:
                boolean flag = true;
                while (flag) {
                    System.out.print("Username: ");
                    String username = in.nextLine();
                    System.out.print("Password: ");
                    String password = in.nextLine();
                    if(controller.addUser(password, username, client) == 0) flag = false;
                }
                if (!controller.joinGame(client.getNickname())) {
                    int num;
                    System.out.println("No game found, let's create a new one!\n");
                    System.out.print("How many players do you want? ");
                    do {
                        System.out.print("(2 to 5 players): ");
                        num = in.nextInt();
                        //free the buffer
                        in.nextLine();
                    } while(num < 2 || num > 5);
                    controller.createGame(client.getNickname(), num);
                }
                break;
            case 2:
                System.exit(0);
            default:
                System.out.println("Invalid input");
        }
    }

    public void main(String[] args) throws RemoteException, NotBoundException, IllegalActionException {
        TUI tui = new TUI();
        tui.start();
    }

    @Override
    public void update(GameDTO game) throws RemoteException, IllegalActionException {
        System.out.println("Rendering of the wonderful GameBoard");
        this.game = game;

        System.out.println("Curren state: "+ game.getState());

        // If its this players turn, query the player for the action, otherwise do nothing
        if (client.getNickname().equals(game.getPlayerTurn())) {
            System.out.println("It's your turn");
            // TODO: sostituire con lo strategy pattern?
            switch (game.getState()) {
                case 0:
                    // SetupGameState
                    System.out.print("Connected players: ");
                    for (PlayerDTO p : game.getPlayers()) {
                        System.out.print(p.getName());
                    }
                    System.out.println();
                    break;
                case 1:
                    // FillBoardState
                    System.out.print("Filling board - this message should never be printed...");
                    break;
                case 2:
                    // ChooseOfferState
                    String offer;
                    System.out.println("Choose the on which offer tile to go ");
                    do {
                        System.out.print("(from A to G): ");
                        offer = in.nextLine();
                    } while(offer.length() != 1);
                    controller.executeAction(new ChooseOfferAction(offer.toUpperCase().charAt(0)), client.getNickname());
                    break;
                case 3:
                    // DrawCardsState
                    System.out.println("Choose which card to draw - TODO");
                    break;
                default:
                    System.out.println("Unhandled state id " + game.getState());
            }
        } else {
            System.out.println("Current player turn: " + game.getPlayerTurn());
        }
    }

    public void printBoard(BoardDTO board){
        printRowTribe(board.getTopRowTribe());
        printRowTribe(board.getBottomRowTribe());
        printRowBuilding(board.getTopRowBuilding());
        printRowBuilding(board.getBottomRowBuilding());
    }

    public void printRowTribe(List<CardDTO> cards){

        StringBuilder line1 = new StringBuilder();
        StringBuilder line2 = new StringBuilder();
        StringBuilder line3 = new StringBuilder();
        StringBuilder line4 = new StringBuilder();
        StringBuilder line5 = new StringBuilder();

        for(CardDTO card : cards) {
            line1.append("+-------+");
            line2.append("|" + card.getName() + "|");
            line3.append("|" + card.getEra() + "|");
            line4.append("| idk   |");
            line5.append("+-------+");

            System.out.println(line1);
            System.out.println(line2);
            System.out.println(line3);
            System.out.println(line4);
            System.out.println(line5);
        }
    }

    public void printRowBuilding(List<BuildingDTO> buildings){
        StringBuilder line1 = new StringBuilder();
        StringBuilder line2 = new StringBuilder();
        StringBuilder line3 = new StringBuilder();
        StringBuilder line4 = new StringBuilder();
        StringBuilder line5 = new StringBuilder();

        for(BuildingDTO building : buildings) {
            line1.append("+-------+");
            line2.append("| Build |");
            line3.append("|" + building.getCost() + "|");
            line4.append("| " + building.getEffect() + " |");
            line5.append("+-------+");
        }
    }

    public void printPlayerCards(List<PlayerDTO> players){
        for(PlayerDTO player : players){
            System.out.println("\n=== " + player.getName() + " ===");
            printRowBuilding(player.getBuildings());
            printArtist(player.getArtists());
            printGatherer(player.getGatherers());
            printHunter(player.getHunters());
            printInventor(player.getInventors());
        }
    }

    public void printArtist(List<ArtistDTO> artists) {
        StringBuilder line1 = new StringBuilder();
        StringBuilder line2 = new StringBuilder();
        StringBuilder line3 = new StringBuilder();
        StringBuilder line4 = new StringBuilder();
        StringBuilder line5 = new StringBuilder();

        for (ArtistDTO artist : artists) {
            line1.append("+-------+");
            line2.append("|Artist |");
            line3.append("|" + artist.getEra() + "|");
            line4.append("|       |");
            line5.append("+-------+");
        }
    }

    public void printGatherer(List<GathererDTO> gatherers) {
        StringBuilder line1 = new StringBuilder();
        StringBuilder line2 = new StringBuilder();
        StringBuilder line3 = new StringBuilder();
        StringBuilder line4 = new StringBuilder();
        StringBuilder line5 = new StringBuilder();

        for (GathererDTO gatherer : gatherers) {
            line1.append("+-------+");
            line2.append("|Gather |");
            line3.append("|       |");
            line4.append("|       |");
            line5.append("+-------+");
        }
    }

    public void printHunter(List<HunterDTO> hunters) {
        StringBuilder line1 = new StringBuilder();
        StringBuilder line2 = new StringBuilder();
        StringBuilder line3 = new StringBuilder();
        StringBuilder line4 = new StringBuilder();
        StringBuilder line5 = new StringBuilder();

        for (HunterDTO hunter : hunters) {
            line1.append("+-------+");
            line2.append("|Hunter |");
            line3.append("| " + hunter.getEra() + " |");
            line4.append("| " + hunter.getIcon() + " |");
            line5.append("+-------+");
        }
    }

    public void printInventor(List<InventorDTO> inventors) {
        StringBuilder line1 = new StringBuilder();
        StringBuilder line2 = new StringBuilder();
        StringBuilder line3 = new StringBuilder();
        StringBuilder line4 = new StringBuilder();
        StringBuilder line5 = new StringBuilder();

        for (InventorDTO inventor : inventors) {
            line1.append("+-------+");
            line2.append("|Invent |");
            line3.append("|" + inventor.getEra() + "|");
            line4.append("|" + inventor.getInventionIcon() + "|");
            line5.append("+-------+");
        }
    }
}

