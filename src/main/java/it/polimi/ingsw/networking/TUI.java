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
import it.polimi.ingsw.model.board.OfferDTO;
import it.polimi.ingsw.model.characters.DTO.ArtistDTO;
import it.polimi.ingsw.model.characters.DTO.GathererDTO;
import it.polimi.ingsw.model.characters.DTO.HunterDTO;
import it.polimi.ingsw.model.characters.DTO.InventorDTO;
import it.polimi.ingsw.model.effects.Building;
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
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[32m";
    public static final String BLUE = "\u001B[34m";
    public static final String ORANGE ="\u001B[38;5;208m";
    public static final String BG_RED = "\u001B[41m";
    public static final String BG_GREEN = "\u001B[42m";
    public static final String BG_BLUE = "\u001B[44m";
    public static final String BG_YELLOW = "\u001B[43m";
    public static final String BOLD = "\u001B[1m";
    public static final String UNDERLINE = "\u001B[4m";

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
                    System.out.print(BLUE + BOLD + "Username: ");
                    String username = in.nextLine();
                    System.out.print(BLUE + BOLD +"Password: ");
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
                        System.out.print("Waiting for other players to connect...");
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
        this.game = game;
        printBoard();

        System.out.println("Current state: "+ game.getState());

        // If its this players turn, query the player for the action, otherwise do nothing
        if (client.getNickname().equals(game.getPlayerTurn())) {
            System.out.println("It's your turn");
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
                    System.out.println("Choose the on which offer tile to go ");
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
        printTopRowTribe();
        printBottomRowTribe();
        printOfferRow();
        printRowBuilding();
        printRowBuilding();
    }
    /* Al momento è ridondante - da fare con printRowTribe e
    differenziare all'interno con metodo chooseRow -> assegno dinamicamente o top o bottom alla rowtribeprint
    */
    public void printTopRowTribe(){
        StringBuilder[] lines = new StringBuilder[7];
        System.out.println(RED + "TopRowTribe:\n");
        List<CardDTO> cards = game.getBoard().getTopRowTribe();

        for(int i=0; i < 7; i++){
            lines[i] = new StringBuilder("");
        }

        for(CardDTO card : cards) {
            lines[0].append("+----------+");
            lines[1].append(String.format("|%-10s|", card.getName()));
            lines[2].append(String.format("|%-10s|", card.getEra()));
            lines[3].append("|          |");
            lines[4].append("|          |");
            lines[5].append("|          |");
            lines[6].append("+----------+");
        }

        for(StringBuilder line : lines){
            System.out.println(line);
        }
    }

    public void printBottomRowTribe(){
        StringBuilder[] lines = new StringBuilder[7];
        List<CardDTO> cards = game.getBoard().getBottomRowTribe();

        System.out.println("BottomRowTribe:\n");

        for(int i=0; i < 7; i++){
            lines[i] = new StringBuilder("");
        }

        for(CardDTO card : cards) {
            lines[0].append(ORANGE + "+----------+");
            lines[1].append(ORANGE).append(String.format("|%-10s|", card.getType()));
            lines[2].append(ORANGE).append(String.format("|%-10s|", card.getName()));
            lines[3].append(ORANGE).append(String.format("|%-10s|", card.getEra()));
            lines[4].append(ORANGE + "|          |");
            lines[5].append(ORANGE + "|          |");
            lines[6].append(ORANGE + "+----------+");
        }

        for(StringBuilder line : lines){
            System.out.println(line);
        }
    }

    public void printRowBuilding(){
        StringBuilder[] lines = new StringBuilder[7];
        for(int i=0; i < 7; i++){
            lines[i] = new StringBuilder();
        }

        List<BuildingDTO> buildings = chooseRowBuilding();

        for(BuildingDTO building : buildings) {
            lines[0].append("+----------+");
            lines[1].append(String.format("|%-10s|", "Build"));
            lines[2].append(String.format("|%-10s|", building.getCost()));
            lines[3].append(String.format("|%-10s|", building.getEffect()));
            lines[4].append(String.format("|%-10s|", building.getEra()));
            lines[5].append("|          |");
            lines[6].append("+----------+");
        }

        for(StringBuilder line : lines){
            System.out.println(line);
        }
    }

    public List<CardDTO> chooseRowTribe(){
        String answer;
        do {
            System.out.println("Which tribe row do you wish to view? [T]op or [B]ottom:");
            answer = in.nextLine();
        }while(!answer.equals("T") && !answer.equals("B"));

        if(answer == "T"){
            return game.getBoard().getTopRowTribe();
        }else{
            return game.getBoard().getBottomRowTribe();
        }
    }

    public List<BuildingDTO> chooseRowBuilding(){
        String answer;
        do {
            System.out.println("Which building row do you wish to view? [T]op or [B]ottom:");
            answer = in.nextLine();
        }while(!answer.equals("T") && !answer.equals("B"));

        if(answer == "T"){
            return game.getBoard().getTopRowBuilding();
        }else{
            return game.getBoard().getBottomRowBuilding();
        }
    }

    public void printPlayerCards(){
        List<PlayerDTO> players  = game.getPlayers();

        for(PlayerDTO player : players){
            System.out.println("\n======== " + player.getName() + " ========");
            printRowBuilding();
            printArtist(player.getArtists());
            printGatherer(player.getGatherers());
            printHunter(player.getHunters());
            printInventor(player.getInventors());
        }
    }

    public void printArtist(List<ArtistDTO> artists) {
        StringBuilder[] lines = new StringBuilder[7];

        for(int i=0; i < 7; i++){
            lines[i] = new StringBuilder();
        }


        for (ArtistDTO artist : artists) {
            lines[0].append("+----------+");
            lines[1].append("|          |");
            lines[2].append(String.format("|%-10s|", "Artist"));
            lines[3].append(String.format("|%-10s|", artist.getEra()));
            lines[4].append("|          |");
            lines[5].append("|          |");
            lines[6].append("+----------+");
        }
        for(StringBuilder line : lines){
            System.out.println(line);
        }
    }

    public void printGatherer(List<GathererDTO> gatherers) {
        StringBuilder[] lines = new StringBuilder[7];

        for(int i=0; i < 7; i++){
            lines[i] = new StringBuilder();
        }

        for (GathererDTO gatherer : gatherers) {
            lines[0].append("+----------+");
            lines[1].append("|          |");
            lines[2].append(String.format("|%-10s|", "Gather"));
            lines[3].append(String.format("|%-10s|", gatherer.getEra()));
            lines[4].append("|          |");
            lines[5].append("|          |");
            lines[6].append("+----------+");
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
            lines[0].append("+----------+");
            lines[1].append("|          |");
            lines[2].append(String.format("|%-10s|", "Hunter"));
            lines[3].append(String.format("|%-10s|", hunter.getEra()));
            lines[4].append(String.format("|%-10s|", hunter.getIcon()));
            lines[5].append("|          |");
            lines[6].append("+----------+");
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
            lines[0].append("+----------+");
            lines[1].append("|          |");
            lines[2].append(String.format("|%-10s|", "Invent"));
            lines[3].append(String.format("|%-10s|", inventor.getEra()));
            lines[4].append(String.format("|%-10s|", inventor.getInventionIcon()));
            lines[5].append("|          |");
            lines[6].append("+----------+");
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

        System.out.println("OfferPath:\n");
        for(int i=0; i < 7; i++){
            lines[i] = new StringBuilder();
        }

        lines[0] = new StringBuilder("+----------+");
        lines[6] = new StringBuilder("+----------+");
        for(int i = 1; i < 6; i++){
            lines[i] = new StringBuilder("|          |");
        }
        for(PlayerDTO player : game.getPlayers()) {
            lines[player.getOrder()+1] = new StringBuilder(String.format("|%-10s|", player.getName()));
        }

        //parliamo di offer, devo fare in modo che carichi quello che c'è scritto nel json - dobbiamo creare una classe offertile mi sa
        StringBuilder[] offer = new StringBuilder[7];
        for(int i=0; i < 7; i++){
            offer[i] = new StringBuilder();
        }
        List<OfferDTO> offerTiles = game.getBoard().getOfferPath();

        for(OfferDTO offerTile : offerTiles){
            lines[0].append("+----------+");
            lines[1].append("|          |");
            lines[2].append(String.format("|%-10s|", "drawT: " + offerTile.getDrawTop()));
            lines[3].append(String.format("|%-10s|", "drawB: " + offerTile.getDrawBottom()));
            lines[4].append(String.format("|%-10s|", "Food: " + offerTile.getFoodBonus()));
            lines[5].append("|          |");
            lines[6].append("+----------+");
        }

        for (int i = 0; i < 7; i++) {
            lines[i].append(offer[i]);
        }

        for(StringBuilder line : lines){
            System.out.println(line);
        }
    }

}

