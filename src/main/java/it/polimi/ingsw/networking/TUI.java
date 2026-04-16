package it.polimi.ingsw.networking;
import it.polimi.ingsw.controller.actions.ChooseOfferAction;
import it.polimi.ingsw.controller.states.ChooseOfferState;
import it.polimi.ingsw.controller.states.GameState;
import it.polimi.ingsw.controller.states.StateDTO;
import it.polimi.ingsw.model.CardDTO;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.Player;
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
import java.util.ArrayList;
import java.util.Arrays;
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
        printBoard();

        System.out.println("Current state: "+ game.getState());

        // If it's this player's turn, query the player for the action, otherwise do nothing
        if (client.getNickname().equals(game.getPlayerTurn())) {
            System.out.println("It's your turn");
            printPlayerCards();
            // TODO: sostituire con lo strategy pattern?
            switch (game.getState()) {
                case StateDTO.SETUPGAME:
                    // SetupGameState
                    System.out.print("Connected players: ");
                    for (PlayerDTO p : game.getPlayers()) {
                        System.out.print(p.getName());
                    }
                    System.out.println();
                    break;
                case StateDTO.FILLBOARD:
                    // FillBoardState
                    System.out.print("Filling board - this message should never be printed...");
                    break;
                case StateDTO.CHOOSEOFFER:
                    // ChooseOfferState
                    String offer;
                    System.out.println("Choose an offer tile ");
                    do {
                        System.out.print("(from A to G): ");
                        offer = in.nextLine();
                    } while(offer.length() != 1);
                    controller.executeAction(new ChooseOfferAction(offer.toUpperCase().charAt(0)), client.getNickname());
                    break;
                case StateDTO.DRAWCARD:
                    // DrawCardsState
                    System.out.println("Choose which card to draw - TODO");
                    break;
                case StateDTO.ENDTURN:
                    // EndTurnState
                    System.out.println("EndTurnState - TODO");
                    break;
                default:
                    System.out.println("Unhandled state id " + game.getState());
            }
        } else {
            System.out.println("Current player turn: " + game.getPlayerTurn());
        }
    }

    public void printBoard(){
        System.out.println("TopRowTribe:\n");
        printTopRowTribe();
        System.out.println("BottomRowTribe:\n");
        printBottomRowTribe();
        System.out.println("OfferPath:\n");
        printOfferRow();
        System.out.println("TopRowBuilding:\n");
        printRowBuilding(game.getBoard().getTopRowBuilding());
        System.out.println("BottomRowBuilding:\n");
        printRowBuilding(game.getBoard().getBottomRowBuilding());
    }
    /* Al momento è ridondante - da fare con printRowTribe e
    differenziare all'interno con metodo chooseRow -> assegno dinamicamente o top o bottom alla rowtribeprint
    */
    public void printTopRowTribe(){
        StringBuilder[] lines = new StringBuilder[7];

        List<CardDTO> cards = game.getBoard().getTopRowTribe();

        for(int i=0; i < 7; i++){
            lines[i] = new StringBuilder("");
        }

        for(CardDTO card : cards) {
            lines[0].append("+-------+");
            lines[1].append(String.format("|%-7s|", card.getName()));
            lines[2].append(String.format("|%-7s|", card.getEra()));
            lines[3].append("|       |");
            lines[4].append("|       |");
            lines[5].append("|       |");
            lines[6].append("+-------+");
        }

        for(StringBuilder line : lines){
            System.out.println(line);
        }
    }

    public void printBottomRowTribe(){
        StringBuilder[] lines = new StringBuilder[7];
        List<CardDTO> cards = game.getBoard().getBottomRowTribe();

        for(int i=0; i < 7; i++){
            lines[i] = new StringBuilder("");
        }

        for(CardDTO card : cards) {
            lines[0].append("+-------+");
            lines[1].append(String.format("|%-7s|", card.getType()));
            lines[2].append(String.format("|%-7s|", card.getName()));
            lines[3].append(String.format("|%-7s|", card.getEra()));
            lines[4].append("|       |");
            lines[5].append("|       |");
            lines[6].append("+-------+");
        }

        for(StringBuilder line : lines){
            System.out.println(line);
        }
    }

    public void printRowBuilding(List<BuildingDTO> buildings){
        StringBuilder[] lines = new StringBuilder[7];
        for(int i=0; i < 7; i++){
            lines[i] = new StringBuilder();
        }

        for(BuildingDTO building : buildings) {
            lines[0].append("+-------+");
            lines[1].append(String.format("|%-7s|", "Build"));
            lines[2].append(String.format("|%-7s|", building.getCost()));
            lines[3].append(String.format("|%-7s|", building.getEffect()));
            lines[4].append(String.format("|%-7s|", building.getEra()));
            lines[5].append("|       |");
            lines[6].append("+-------+");
        }

        for(StringBuilder line : lines){
            System.out.println(line);
        }
    }

    public void printPlayerCards(){
        List<PlayerDTO> players  = game.getPlayers();

        for(PlayerDTO player : players){
            System.out.println("\n=== " + player.getName() + " ===");
            printRowBuilding(player.getBuildings());
            //printArtist(player.getArtists());
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
        StringBuilder[] lines = new StringBuilder[7];

        for(int i=0; i < 7; i++){
            lines[i] = new StringBuilder();
        }

        for (GathererDTO gatherer : gatherers) {
            lines[0].append("+-------+");
            lines[1].append(String.format("|%-7s|", ""));
            lines[2].append(String.format("|%-7s|", "Gather"));
            lines[3].append(String.format("|%-7s|", gatherer.getEra()));
            lines[4].append("|       |");
            lines[5].append("|       |");
            lines[6].append("+-------+");
        }

        for(StringBuilder line : lines){
            System.out.println(line);
        }
    }

    public void printHunter(List<HunterDTO> hunters) {
        StringBuilder[] lines = new StringBuilder[7];

        for(int i=0; i < 7; i++){
            lines[i] = new StringBuilder();
        }

        for (HunterDTO hunter : hunters) {
            lines[0].append("+-------+");
            lines[1].append("|%-7s|");
            lines[2].append(String.format("|%-7s|", "Hunter"));
            lines[3].append(String.format("|%-7s|", hunter.getEra()));
            lines[4].append(String.format("|%-7s|", hunter.getIcon()));
            lines[5].append("|       |");
            lines[6].append("+-------+");
        }

        for(StringBuilder line : lines){
            System.out.println(line);
        }
    }

    public void printInventor(List<InventorDTO> inventors) {
        StringBuilder[] lines = new StringBuilder[7];

        for(int i=0; i < 7; i++){
            lines[i] = new StringBuilder();
        }

        for (InventorDTO inventor : inventors) {
            lines[0].append("+-------+");
            lines[1].append("|       |");
            lines[2].append(String.format("|%-7s|", "Invent"));
            lines[3].append(String.format("|%-7s|", inventor.getEra()));
            lines[4].append(String.format("|%-7s|", inventor.getInventionIcon()));
            lines[5].append("|       |");
            lines[6].append("+-------+");
        }

        for(StringBuilder line : lines){
            System.out.println(line);
        }

    }

    /**
     * Function to print the order of players on the OrderTile followed by the offerTiles
     */
    public void printOfferRow(){
        StringBuilder[] lines = new StringBuilder[7];

        for(int i=0; i < 7; i++){
            lines[i] = new StringBuilder();
        }

        lines[0] = new StringBuilder("+-------+");
        lines[6] = new StringBuilder("+-------+");
        for(int i = 1; i < 6; i++){
            lines[i] = new StringBuilder("|       |");
        }
        for(PlayerDTO player : game.getPlayers()) {
            lines[player.getOrder()] = new StringBuilder(String.format("|%-7s|", player.getName()));
        }

        //parliamo di offer, devo fare in modo che carichi quello che c'è scritto nel jason - dobbiamo creare una classe offertile mi sa
        StringBuilder[] offer = new StringBuilder[7];
        offer[0] = new StringBuilder("+-------+");
        offer[6] = new StringBuilder("+-------+");
        for(int i = 1; i < 6; i++){
            offer[i] = new StringBuilder("|       |");
        }
        for (int i = 0; i < 7; i++) {
            lines[i].append(offer[i]);
        }

        for(StringBuilder line : lines){
            System.out.println(line);
        }
    }

}

