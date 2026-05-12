package it.polimi.ingsw.UI.TUI;
import it.polimi.ingsw.controller.actions.ChooseOfferAction;
import it.polimi.ingsw.controller.actions.DrawCardFromBottomAction;
import it.polimi.ingsw.controller.actions.DrawCardFromTopAction;
import it.polimi.ingsw.controller.states.StateDTO;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.OfferDTO;
import it.polimi.ingsw.model.board.OrderDTO;
import it.polimi.ingsw.model.characters.DTO.*;
import it.polimi.ingsw.model.effects.BuildingDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.Client;
import it.polimi.ingsw.networking.DB.LeaderboardDTO;
import it.polimi.ingsw.networking.RMI.ClientRMI;
import it.polimi.ingsw.networking.TCP.ClientTCP;
import it.polimi.ingsw.networking.UIObserver;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class TUI implements UIObserver {
    private final Scanner in = new Scanner(System.in);
    private GameDTO game;
    private Client client;
    private int portTCP;
    private int portRMI;
    private String serverAddress;
    private boolean gameClosed;
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String GREEN = "\u001B[32m";
    public static final String ORANGE ="\u001B[38;5;208m";
    public static final String BG_RED = "\u001B[41m";
    public static final String BG_PURPLE = "\u001B[45m";
    public static final String BG_WHITE = "\u001B[47m";
    public static final String BG_GREEN = "\u001B[42m";
    public static final String BG_BLUE = "\u001B[44m";
    public static final String BG_YELLOW = "\u001B[43m";
    public static final String BOLD = "\u001B[1m";
    public static final String UNDERLINE = "\u001B[4m";
    private final LinkedBlockingQueue<GameDTO> updates = new LinkedBlockingQueue<>();

    /**
     *TUI
     * @throws RemoteException
     * @throws NotBoundException
     */
    public TUI (int portRMI, int portTCP, String serverAddress) throws RemoteException, NotBoundException {
        this.game = null;
        this.client = null;
        this.serverAddress = serverAddress;
        this.portRMI = portRMI;
        this.portTCP = portTCP;
        this.gameClosed = false;
    }

    public boolean getGameClosed(){
        return this.gameClosed;
    }

    public void setGameClosed(boolean gameClosed){
        this.gameClosed = gameClosed;
    }

    /**
     * Lets the user choose between a TCP connection and an RMI connection
     * @throws NotBoundException
     * @throws RemoteException
     */
    public void chooseTCPorRMI() throws NotBoundException, IOException, IllegalActionException, InterruptedException, ClassNotFoundException {
        int choice = 0;
        do{
            System.out.println(BLUE + BOLD + "Choose between RMI[1] or TCP[2] connection");
            choice = readInt();
        }while(choice != 1 && choice != 2);
        if(choice == 1) {
            this.client = new ClientRMI(this, portRMI, serverAddress);
        }else{
            this.client = new ClientTCP(this, portTCP, serverAddress);
        }
    }

    /**
     * Function that starts the game by adding the user and loading the game
     * @throws RemoteException
     * @throws IllegalActionException
     */

    public void start() throws IOException, IllegalActionException, ClassNotFoundException, InterruptedException {
        int input;
        do{
            renderStartMenu();
            input = readInt();
        }while(input != 1 && input != 2);

        switch (input) {
            case 1:
                boolean flag = true;
                while (flag) {
                    System.out.print(BLUE + BOLD + "Username: ");
                    String username = readLine();
                    System.out.print(BLUE + BOLD +"Password: ");
                    String password = readLine();
                    if(client.addUser(password, username) == 0) flag = false;
                }
                client.ping();
                joinGame();
                break;
            case 2:
                client.leaveGame();
                System.exit(0);
            default:
                System.out.println("Invalid input");
        }
    }

    public void anotherGame() throws IOException, IllegalActionException, ClassNotFoundException, InterruptedException {
        int input;
        do{
            renderMenu();
            input = readInt();
        }while(input != 1 && input != 2);

        switch (input) {
            case 1:
                joinGame();
                break;
            case 2:
                client.leaveGame();
                System.exit(0);
            default:
                System.out.println("Invalid input");
        }
    }

    public void joinGame() throws IllegalActionException, IOException, InterruptedException, ClassNotFoundException {
        if (!client.joinGame()) {
            int num;
            System.out.println("No game found, let's create a new one!\n");
            System.out.print("How many players do you want? ");
            do {
                System.out.print("(2 to 5 players): ");
                num = readInt();
            } while(num < 2 || num > 5);
            client.createGame(num);
        }
        System.out.print("Waiting for other players to connect...\n");
        handleState();
    }

    public String chooseTotem() throws IllegalActionException, IOException, InterruptedException, ClassNotFoundException {
        String totem = "";
        System.out.println("Choose a totem:\n");
        System.out.println("1. " + BG_RED + "|");
        System.out.println("2. " + BG_YELLOW + "|");
        System.out.println("3. " + BG_BLUE + "|");
        System.out.println("4. " + BG_PURPLE + "|");
        System.out.println("5. " + BG_WHITE + "|");
        int input = readInt();

        switch (input){
            case 1:
                totem = BG_RED;
            case 2:
                totem = BG_YELLOW;
            case 3:
                totem = BG_BLUE;
            case 4:
                totem = BG_PURPLE;
            case 5:
                totem = BG_WHITE;

        }
        return totem;
    }

    /**
     * Function that changes the states throwout the game
     * @param game
     * @throws RemoteException
     * @throws IllegalActionException
     */


    /*
     *
     * TODO: aggiungi stampa turno
     *
     * TODO: assicurati si stampi anche la order tile iniziale con l'ordine random
     *
     * TODO: make prettier con aggiunta colori + rendendo centrata la stampa
     *
     * */


    @Override
    public void update(GameDTO game) throws IOException, IllegalActionException, InterruptedException {
        updates.offer(game);
    }

    @Override
    public void closingGame(GameDTO game) throws IOException, IllegalActionException, ClassNotFoundException, InterruptedException {
        setGameClosed(true);
        updates.offer(game);
    }

    @Override
    public void serverCrashed() throws IOException, IllegalActionException, InterruptedException{
        System.out.println("Sorry, the server crashed\n");
        System.exit(1);
    }

    public void handleState() throws InterruptedException, IllegalActionException, IOException, ClassNotFoundException {
        while(true){
            game = updates.take();
            if(getGameClosed()){
                client.leaveMatch();
                setGameClosed(false);
                System.out.println("\nSorry, the game as been closed due to a disconnection of a player\n");
                anotherGame();
            }
            System.out.println(BLUE + "Current state: " + game.getState());
            System.out.println("Current turn " + game.getTurnNumber());

            // If its this players turn, query the player for the action, otherwise do nothing
                // TODO: sostituire con lo strategy pattern?
            switch (game.getState()) {
                case StateDTO.SETUPGAME:
                    // SetupGameState
                    System.out.print("Connected players: ");
                    for (PlayerDTO p : game.getPlayers()) {
                        System.out.print(p.getName() + ", ");
                    }
                    System.out.println();
                    break;
                case StateDTO.FILLBOARD:
                    // FillBoardState
                    System.out.print("Filling board - this message should never be printed...");
                    break;
                case StateDTO.CHOOSEOFFER:
                    printBoard();
                    // ChooseOfferState
                    if (client.getNickname().equals(game.getPlayerTurn().getName())){
                        List<OfferDTO> offerTiles = game.getBoard().getOfferPath();
                        char[] freeTiles = new char[game.getNumPlayers() * 2];
                        char[] occupiedTiles = new char[game.getNumPlayers()];

                        int i = 0;
                        for (OfferDTO offerTile : offerTiles) {
                            freeTiles[i++] = offerTile.getOrder();
                        }
                        i=0;
                        for(PlayerDTO p : game.getPlayers()){
                            occupiedTiles[i++] = p.getOffer();
                        }

                        char offer;
                        System.out.println("Choose on which offer tile to go ");

                        System.out.println("\nthe available tiles are: " + Arrays.toString(freeTiles));
                        do {
                            System.out.print("(write the letter): ");
                            offer = readChar();
                        } while (!contains(freeTiles, offer) || contains(occupiedTiles,offer));
                        if(!getGameClosed()){
                            client.executeAction(new ChooseOfferAction(offer));
                        }
                    }
                    else {
                        System.out.println("Current player turn: " + game.getPlayerTurn().getName());
                    }
                    break;
                case StateDTO.DRAWCARD:
                    // DrawCardsState
                    if (client.getNickname().equals(game.getPlayerTurn().getName())){
                        System.out.println("It's your turn!");
                        OfferDTO playerOffer = new OfferDTO('A', 0, 0, 0);
                        for (OfferDTO offerTile : game.getBoard().getOfferPath()) {
                            if (offerTile.getOrder() == game.getPlayerTurn().getOffer()) {
                                playerOffer = offerTile;
                            }
                        }
                        char chosenRow;
                        int drawn = 0;
                        int drawTop = playerOffer.getDrawTop();
                        int drawBottom = playerOffer.getDrawBottom();

                        while (drawTop + drawBottom > 0) {
                            drawn = 0;
                            if (drawTop != 0 && drawBottom != 0) {
                                System.out.println("From which row do you wish to draw your card? [T]op or [B]ottom: \n");
                                chosenRow = readChar();
                                if (chosenRow == 'T') {
                                    drawn = drawCardTopRow();
                                    drawTop += drawn;
                                }
                                if (chosenRow == 'B') {
                                    drawn = drawCardBottomRow();
                                    drawBottom += drawn;
                                }
                            } else if (drawTop != 0) {
                                drawn = drawCardTopRow();
                                drawTop += drawn;
                            } else {
                                drawn = drawCardBottomRow();
                                drawBottom += drawn;
                            }
                            if (drawTop + drawBottom > 0 && client.getNickname().equals(game.getPlayerTurn().getName()) && drawn != 0) { // CRASHA SE METTO NUMERO SBAGLIATO PER COLPA DI QUESTO
                                game = updates.take();
                            }
                        }
                    }
                    else {
                        System.out.println("Current player turn: " + game.getPlayerTurn().getName());
                    }
                    break;
                case StateDTO.ENDTURN:
                    // EndTurnState
                    System.out.println("EndTurnState\nYou just finished round " + game.getTurnNumber());
                    if(game.getPlayerTurn().getCanPickFromTop()){
                        int effectDraw = 1;
                        effectDraw = drawCardTopRow();
                    }
                    break;
                case StateDTO.ENDGAME:
                    // EndGameState
                    printRankings();
                    client.leaveMatch();
                    anotherGame();
                default:
                    System.out.println("Unhandled state id " + game.getState());
            }
        }
        // lockUpdate.wait(); //
        // lockUpdate.notify(); // faccio
        // while(true){ wait(); si svolge gioco }
        // notify
        // attivazione metodo idk - switch case sottostante
        // lock update -> wait(lock) finchè non
        // appena arriva notify(lock)
        // notify observer in game.java
    }

    private void printRankings(){
        List<PlayerDTO> ranking = game.getRankings();
        int displayedPos = 1;
        for(int i = 0; i < ranking.size(); i++){
            if(i > 0){
                PlayerDTO current = ranking.get(i);
                PlayerDTO previous = ranking.get(i-1);
                boolean tied =  current.getPp() == previous.getPp() && current.getFood() == previous.getFood();

                if(!tied){
                    displayedPos = i + 1;
                }
            }

            PlayerDTO p = ranking.get(i);
            System.out.printf(YELLOW + "%d. %-15s (%d PP, %d food)%n",displayedPos, p.getName(), p.getPp(), p.getFood());
        }
    }

    private int drawCardBottomRow() throws IllegalActionException, IOException, InterruptedException, ClassNotFoundException {
        int card;
        printRowTribe(game.getBoard().getBottomRowTribe(), game.getBoard().getBottomRowBuilding());
        System.out.println("Choose which card to draw from the Bottom Row (write its number): ");
        card = readInt() - 1; // fixes index and pos mismatch
        int size = game.getBoard().getBottomRowTribe().size() + game.getBoard().getBottomRowBuilding().size();
        if(card >= 0 && card < size){
            if(!getGameClosed()){
                    try{
                        client.executeAction(new DrawCardFromBottomAction(card));
                    }catch(Exception e){
                        //e.printStackTrace();
                        System.out.println(e.getMessage());
                        return 0;
                    }
                return -1;
            }
            return -10;
        }else{
            System.out.println("You chose a card out of range\n");
            return 0;
        }
    }

    private int drawCardTopRow() throws IllegalActionException, IOException, InterruptedException, ClassNotFoundException {
        int card;
        printRowTribe(game.getBoard().getTopRowTribe(), game.getBoard().getTopRowBuilding());
        System.out.println("Choose which card to draw from the Top Row (write its number): ");
        card = readInt() - 1; // fixes index and pos mismatch
        int size = game.getBoard().getTopRowTribe().size() + game.getBoard().getTopRowBuilding().size();
        if(card >= 0 && card < size){
            if(!getGameClosed()){
                try{
                    client.executeAction(new DrawCardFromTopAction(card));
                }catch(Exception e){
                    //e.printStackTrace();
                    System.out.println(e.getMessage());
                    return 0;
                }
                return -1;
            }
            return -10;
        }else{
            System.out.println("You chose a card out of range\n");
            return 0;
        }
    }

    // -------------------------------- Render functions ---------------------------------------------------------------

    public void renderStartMenu(){
        System.out.println(GREEN + BOLD + "MENU:");
        System.out.println("1. Login");
        System.out.println("2. Exit");
    }

    public void renderMenu(){
        System.out.println(GREEN + BOLD + "MENU:");
        System.out.println("1. Play again");
        System.out.println("2. Exit");
    }

    /**
     * Calls the functions to print each part of the board
     */
    public void printBoard(){
        printRowTribe(game.getBoard().getTopRowTribe(), game.getBoard().getTopRowBuilding());
        printRowTribe(game.getBoard().getBottomRowTribe(), game.getBoard().getBottomRowBuilding());
        printOrderTile();
        printOfferRow();
        for(PlayerDTO player : game.getPlayers()){
            printPlayerCards(player);
        }

    }

    /**
     * Prints both rows based on the input it receives
     * @param cards
     * @param buildings
     */
    public void printRowTribe(List<CardDTO> cards, List<BuildingDTO> buildings){
        StringBuilder[] lines = new StringBuilder[7];
        for(int i=0; i < 7; i++){
            lines[i] = new StringBuilder("");
        }
        StringBuilder[] build = new StringBuilder[7];
        for(int i=0; i < 7; i++){
            build[i] = new StringBuilder();
        }
        //------------- Adding the cards
        lines = handleCards(cards);
        //------------- Adding the buildings
        build = printBuildings(buildings, cards.size());
        for(int i = 0; i < 7; i++){
            lines[i].append(build[i]);
        }
        printLine(lines);
    }

    public StringBuilder[] printBuildings(List<BuildingDTO> buildings, int numCard){
        StringBuilder[] lines = new StringBuilder[7];
        for(int i = 0; i < 7; i++){
            lines[i] = new StringBuilder();
        }

        for(BuildingDTO building : buildings) {
            lines[0].append(ORANGE).append(String.format("+----%d-----+", numCard+1));
            lines[1].append(ORANGE).append(String.format("|%-10s|", "Build"));
            lines[2].append(ORANGE).append(String.format("|%-10s|", building.getCost()));
            lines[3].append(ORANGE).append(String.format("|%-10s|", building.getEffect()));
            lines[4].append(ORANGE).append(String.format("|%-10s|", building.getEra()));
            lines[5].append(ORANGE + "|          |");
            lines[6].append(ORANGE + "+----------+");
            numCard++;
        }
        return lines;
    }


    /**
     * Function that handles card input and prints them based on what they are, calling other functions as support
     * @return
     */
    public StringBuilder[] handleCards(List<CardDTO> cards){
        StringBuilder[] lines = new StringBuilder[7];
        for(int i = 0; i < 7; i++){
            lines[i] = new StringBuilder();
        }
        int num_card = 0;/*
        StringBuilder[] printed = new StringBuilder[7];
        for(int i = 0; i < 7; i++){
            printed[i] = new StringBuilder();
        }

        for(CardDTO card : cards){
            System.out.println(card.getClass().getSimpleName());
            System.out.println(card.getName());
            printed = card.printCard();
            printed[0] = new StringBuilder().append(String.format("+----%d-----+", num_card++));
            for(int i=1; i<7; i++){
                lines[i].append(printed[i]);
            }
        }*/

        for(CardDTO card : cards){
            if(card.getType().equals("Event")){
                num_card++;
                StringBuilder[] event = new StringBuilder[7];
                event = createEvent(card, num_card);
                for(int i = 0; i < 7; i++){
                    lines[i].append(event[i]);
                }
            } else if(card.getType().equals("Character")){
                num_card++;
                StringBuilder[] character = new StringBuilder[7];
                character = createCharacter(card, num_card);
                for(int i = 0; i < 7; i++){
                    lines[i].append(character[i]);
                }
            }
        }
        return lines;
    }

    /**
     * Function to build an Event card in order to print it
     * @param card
     * @return
     */
    public StringBuilder[] createEvent(CardDTO card, int num_card){
        StringBuilder[] event = new StringBuilder[7];
        for(int i = 0; i < 7; i++){
            event[i] = new StringBuilder();
        }
        event[0].append(ORANGE).append(String.format("+----%d-----+", num_card));
        if(card.getName().equals("ShamanicRitual")){
            event[1].append("|Shamanic  |");
            event[2].append("|Ritual    |");
            event[3].append(String.format("|%-10s|", card.getWinnerPp()));
            event[4].append(String.format("|%-10s|", card.getLoserPp()));
        } else if(card.getName().equals("CavePaintings")){
            event[1].append("|Cave      |");
            event[2].append("|Paintings |");
            event[3].append(String.format("|%-10s|", card.getMinArtist()));
            event[4].append(String.format("|%-10s|", card.getTopPp()));
        } else{
            event[1].append(String.format("|%-10s|", card.getName()));
            event[2].append("|          |");
            event[3].append(ORANGE + "|          |");
            event[4].append(ORANGE + "|          |");
        }
        event[5].append(ORANGE).append(String.format("|%-10s|", card.getEra()));
        event[6].append(ORANGE + "+----------+");


        return event;
    }

    public StringBuilder[] createCharacter(CardDTO card, int num_card){
        StringBuilder[] character = new StringBuilder[7];
        for(int i = 0; i < 7; i++){
            character[i] = new StringBuilder();
        }
        character[0].append(ORANGE).append(String.format("+----%d-----+", num_card));
        character[1].append(String.format("|%-10s|", card.getName()));
        if(card.getName().equals("Shaman")){
            character[2].append(String.format("|%-10s|", card.getStars()));
        } else if (card.getName().equals("Inventor")){
            character[2].append(String.format("|%-10s|", card.getInventionIcon()));
        } /*else if (card.getName().equals("Gatherer")){
            character[2].append(String.format("|%-10s|", card.getFoodDiscount()));
        }*/ else if (card.getName().equals("Builder")){
            character[2].append(String.format("|%-10s|", card.getFoodDiscount()));
        } else if (card.getName().equals("Hunter")){
            character[2].append(String.format("|%-10s|", card.getIcon()));
        } else{
            character[2].append("|          |");
        }
        character[3].append("|          |");
        character[4].append("|          |");
        character[5].append(String.format("|%-10s|", card.getEra()));
        character[6].append("+----------+");

        return character;
    }

    /**
     * Function to print a single player's cards
     * @param player
     */
    public void printPlayerCards(PlayerDTO player){
        System.out.println("\n======== " + player.getName() + " ========");
        StringBuilder[] lines = new StringBuilder[7];
        for(int i = 0; i < 7; i++){
            lines[i] = new StringBuilder();
        }
        for (GathererDTO gatherer :  player.getGatherers()){
            appendLines(lines, gatherer.printCard());
        }
        for(ArtistDTO artist : player.getArtists()) {
            appendLines(lines, artist.printCard());
        }
        for(HunterDTO hunter : player.getHunters()){
            appendLines(lines, hunter.printCard());
        }
        for(ShamanDTO shaman : player.getShamans()){
            appendLines(lines, shaman.printCard());
        }
        for(InventorDTO inventor : player.getInventors()){
            appendLines(lines, inventor.printCard());
        }
        for(BuilderDTO builder : player.getBuilders()){
            appendLines(lines, builder.printCard());
        }
        appendLines(lines,printBuildings(player.getBuildings(), 0));
        boolean isEmpty = true;

        for(StringBuilder line : lines){
            if(line.length() > 0){
                isEmpty = false;
                break;
            }
        }
        if(!isEmpty){
            printLine(lines);
        }else{
            System.out.println(player.getName() + " has no cards...\n");
        }

    }

    /**
     * Function to ask whose cards does the user wish to view
     */
    public void choosePlayerCards () throws IllegalActionException, IOException, ClassNotFoundException, InterruptedException {
        List<PlayerDTO> players = game.getPlayers();
        System.out.println("Whose cards do you wish to view?");
        String name = readLine();
        for(PlayerDTO player : players){
            if(name.equals(player.getName())){
                printPlayerCards(player);
            }
        }
    }

    //TODO: modifica tutte queste classi di printCharacter in un factory pattern -
    // tipo un handler della stampa a cui collego un CharacterDTO e tramite override
    // modifico la funzione stampa per il tipo di carta



    /**
     * Function to print the Gatherer cards of a player
     * @param gatherers
     */

    public StringBuilder[] printGatherer(List<GathererDTO> gatherers) {
        StringBuilder[] lines = new StringBuilder[7];

        for(int i=0; i < 7; i++){
            lines[i] = new StringBuilder();
        }

        for (GathererDTO gatherer : gatherers) {
            lines[0].append("+----------+");
            lines[1].append(String.format("|%-10s|", "Gather"));
            lines[2].append("|          |");
            lines[3].append("|          |");
            lines[4].append("|          |");
            lines[5].append(String.format("|%-10s|", gatherer.getEra()));
            lines[6].append("+----------+");
        }

        return lines;
    }

    /**
     * Function to print the order of players on the OrderTile followed by the OfferTiles
     */
    public void printOfferRow(){
        StringBuilder[] lines = new StringBuilder[7];

        System.out.println(BLUE + "\nOfferPath:");

        lines[0] = new StringBuilder(ORANGE + "+----------+");
        lines[6] = new StringBuilder("+----------+");
        for(int i = 1; i < 6; i++){
            lines[i] = new StringBuilder("|          |");
        }

        for(PlayerDTO player : game.getPlayers()) {
            if(player.getOffer() == '\0'){
                lines[player.getOrder() + 1] =  new StringBuilder(String.format("|%-10s|", player.getName()));
            }
        }

        List<OfferDTO> offerTiles = game.getBoard().getOfferPath();

        for(OfferDTO offerTile : offerTiles){
            PlayerDTO playerOnTile = null;
            for(PlayerDTO player : game.getPlayers()){
                if(player.getOffer() == offerTile.getOrder()){
                    playerOnTile = player;
                    break; // TODO: metodo bruttino da cambiare
                }
            }
            lines[0].append(String.format("+----%s-----+", offerTile.getOrder()));
            if(playerOnTile != null){
                lines[1].append(String.format("|%-10s|", playerOnTile.getName()));
            } else {
                lines[1].append("|          |");
            }
            lines[2].append(String.format("|%-10s|", "drawT: " + offerTile.getDrawTop()));
            lines[3].append(String.format("|%-10s|", "drawB: " + offerTile.getDrawBottom()));
            lines[4].append(String.format("|%-10s|", "Food: " + offerTile.getFoodBonus()));
            lines[5].append("|          |");
            lines[6].append("+----------+");
        }

        for(StringBuilder line : lines){
            System.out.println(line);
        }
    }

    /**
     * Function to print the OrderTile without the players on it - to view the bonus values on the positions
     */
    public void printOrderTile(){
        int num = game.getNumPlayers()+2;
        StringBuilder[] lines = new StringBuilder[num];
        System.out.println(BLUE + "\nOrderTile:");

        for(int i = 0; i < num; i++){
            lines[i] = new StringBuilder();
        }

        OrderDTO orderTile = game.getBoard().getOrder();
        lines[0].append(ORANGE + "+----------+");
        for(int i = 1; i < num-1; i++){
            lines[i].append(String.format("|%-10s|", "Food: " + orderTile.getFoodBonus().get(i-1) + " Pp: " + orderTile.getPpBonus().get(i-1)));
        }
        lines[num-1].append("+----------+");

        /*
        List<PlayerDTO> players = game.getPlayers();
        for(PlayerDTO player : players){
            if(player.getOffer() != '\0'){
                lines[player.getOrder()] = new StringBuilder(String.format("|%-10s|", player.getName()));
            }
        }*/

        for(StringBuilder line : lines){
            System.out.println(line);
        }
    }

    // --------------------------- Helper functions ----------------------------------------------------------
    public void printLine(StringBuilder[] lines){
        for(StringBuilder line : lines){
            System.out.println(line);
        }
    }

    public void appendLines(StringBuilder[] first, StringBuilder[] second){
        for(int i = 0; i < first.length; i++){
            first[i].append(second[i]);
        }
    }

    private boolean contains(char array[], char s){
        for(char c : array){
            if(c == s){
                return true;
            }
        }

        return false;
    }

    private boolean handleCommand(String input) throws IllegalActionException, IOException, ClassNotFoundException, InterruptedException {
        switch (input.toLowerCase()){
            case "exit":
            case "quit":
                if(gameClosed){
                    client.leaveGame();
                    System.exit(0);
                }
                client.stopGame(client.getNickname());
                anotherGame();
            case "board":
                printBoard();
                return true;
            default:
                return false;
        }
    }

    private String readLine() throws IllegalActionException, IOException, ClassNotFoundException, InterruptedException {
        while(true){
            String input = in.nextLine().trim();
            if(handleCommand(input)){
                continue;
            }
            return  input;
        }
    }

    private int readInt() throws IllegalActionException, IOException, InterruptedException, ClassNotFoundException {
        while(true){
            try{
                return Integer.parseInt(readLine());
            } catch (NumberFormatException e) {
                System.out.println(RED + "Please enter a valid number!");
            }
        }
    }

    private char readChar() throws IllegalActionException, IOException, ClassNotFoundException, InterruptedException {
        while(true){
            String input = readLine().trim().toUpperCase();
            if(input.length() == 1){
                return input.charAt(0);
            }
            System.out.println(RED + "Please enter a single letter!");
        }
    }
}

