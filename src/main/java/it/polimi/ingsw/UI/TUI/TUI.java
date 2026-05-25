package it.polimi.ingsw.UI.TUI;

import it.polimi.ingsw.controller.actions.ChooseOfferAction;
import it.polimi.ingsw.controller.actions.ChooseTotemAction;
import it.polimi.ingsw.controller.actions.DrawCardFromBottomAction;
import it.polimi.ingsw.controller.actions.DrawCardFromTopAction;
import it.polimi.ingsw.controller.states.ChooseTotemState;
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
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class TUI implements UIObserver {
    private final Scanner in = new Scanner(System.in);
    private GameDTO game;
    private Client client;
    private final int portTCP;
    private final int portRMI;
    private final String serverAddress;
    private boolean gameClosed;
    private boolean drawInitialiazed;
    private int drawBottom;
    private int drawTop;

    // TUI design colors
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String GREEN = "\u001B[32m";
    public static final String ORANGE ="\u001B[38;5;208m";
    public static final String RESET ="\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String UNDERLINE = "\u001B[4m";

    // NOTIFY QUEUE
    private final LinkedBlockingQueue<GameDTO> updates = new LinkedBlockingQueue<>();

    /**
     *TUI
     */
    public TUI (int portRMI, int portTCP, String serverAddress) {
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
    public void start() throws Exception {
        int input;
        do{
            renderStartMenu();
            input = readInt();
        }while(input != 1 && input != 2 && input!=3);

        switch (input) {
            case 1:
                boolean flag = true;
                while (flag) {
                    System.out.print(BLUE + BOLD + "Username: ");
                    String username = readLine();
                    System.out.print(BLUE + BOLD +"Password: ");
                    String password = readLine();
                    flag = !client.addUser(password, username);
                }
                client.ping();
                joinGame();
                break;
            case 2:
                printScoreBoard();
                start();
                break;
            case 3:
                client.leaveGame();
                System.exit(0);
            default:
                System.out.println("Invalid input");
        }
    }

    /**
     * Function to initialize another game after one ended
     * @throws Exception
     */
    public void anotherGame() throws Exception {
        int input;
        do{
            renderMenu();
            input = readInt();
        }while(input != 1 && input != 2 && input !=3);

        switch (input) {
            case 1:
                joinGame();
                break;
            case 2:
                printScoreBoard();
                anotherGame();
                break;
            case 3:
                client.leaveGame();
                System.exit(0);
            default:
                System.out.println("Invalid input");
        }
    }

    /**
     * Function to create and join a new game
     * @throws Exception
     */
    public void joinGame() throws Exception {
        if (!client.joinGame()) {
            int num;
            System.out.println(BLUE + "No game found, let's create a new one!\n");
            System.out.print("How many players do you want? ");
            do {
                System.out.print("(2 to 5 players): ");
                num = readInt();
            } while(num < 2 || num > 5);
            client.createGame(num);
        }
        System.out.print(BLUE + "Waiting for other players to connect...\n");
        handleState();
    }


    /**
     * Function that changes the states throwout the game
     * @param game
     * @throws RemoteException
     * @throws IllegalActionException
     */
    @Override
    public void update(GameDTO game) throws IOException, IllegalActionException, InterruptedException {
        updates.offer(game);
    }

    /**
     * Function of closing game
     * @param game
     * @throws IOException
     * @throws IllegalActionException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    @Override
    public void closingGame(GameDTO game) throws IOException, IllegalActionException, ClassNotFoundException, InterruptedException {
        setGameClosed(true);
        updates.offer(game);
    }

    /**
     * Message of crashed Server
     */
    @Override
    public void serverCrashed() {
        System.out.println(RED + "Sorry, the server crashed\n");
        System.exit(1);
    }

    /**
     * Function to handle the states of the gameplay
     * @throws Exception
     */
    public void handleState() throws Exception {
        while(true){
            game = updates.take(); // Takes the new state at the beginning of every action
            // If a player disconnects:
            if(getGameClosed()){
                client.leaveMatch();
                setGameClosed(false);
                System.out.println(RED + "\nSorry, the game as been closed due to a disconnection of a player\n");
                anotherGame();
            }
            // State of the game and Turn number:
            System.out.println(BLUE + "Current state: " + game.getState());
            if(!game.getState().equals(StateDTO.ENDGAME)){
                System.out.println(BLUE + "Current turn " + game.getTurnNumber());
            }


            // If its this players turn, query the player for the action, otherwise do nothing
            switch (game.getState()) {
                case RESOLVEEVENT:
                    break;
                case StateDTO.SETUPGAME:
                    // SetupGameState
                    System.out.print(BLUE + "Connected players: ");
                    for (PlayerDTO p : game.getPlayers()) {
                        System.out.print(p.getName() + ", ");
                    }
                    System.out.println();
                    break;
                case StateDTO.FILLBOARD:
                    // FillBoardState
                    System.out.print("Filling board - this message should never be printed...");
                    break;
                case StateDTO.CHOOSETOTEM:
                    if (client.getNickname().equals(game.getPlayerTurn().getName())){
                        // taken totems
                        Set<Totem> takenTotems = game.getPlayers().stream().map(PlayerDTO::getTotem).filter(Objects::nonNull).collect(Collectors.toSet());

                        // available totems
                        List<Totem> availableTotems = Arrays.stream(Totem.values()).filter(t -> !takenTotems.contains(t)).toList();

                        System.out.println(BLUE + "Choose a totem:\n");

                        for(Totem t : Totem.values()){
                            System.out.println(t.getId() + ") " + t.getColor() + BLUE);
                        }

                        Totem chosenTotem = null;

                        while(chosenTotem == null){
                            int input = readInt();

                            for(Totem t : availableTotems){
                                if(t.getId() == input){
                                    chosenTotem = t;
                                    break;
                                }
                            }
                            if (chosenTotem == null){
                                System.out.println(RED + "Invalid choice\n");
                            }
                        }
                        client.executeAction(new ChooseTotemAction(chosenTotem));
                    }
                    else {
                        System.out.println(BLUE + "Current player turn: " + game.getPlayerTurn().getName());
                    }
                    break;
                case StateDTO.CHOOSEOFFER:
                    printBoard();
                    // ChooseOfferState
                    if (client.getNickname().equals(game.getPlayerTurn().getName())){
                        List<OfferDTO> offerTiles = game.getBoard().getOfferPath();
                        char[] freeTiles = new char[game.getNumPlayers() + 2];
                        char[] occupiedTiles = new char[game.getNumPlayers()];

                        int i = 0;
                        for (OfferDTO offerTile : offerTiles) {
                            freeTiles[i++] = offerTile.getOrder();
                        }
                        i = 0;
                        for(PlayerDTO p : game.getPlayers()){
                            occupiedTiles[i++] = p.getOffer();
                        }

                        char offer;
                        System.out.println(BLUE + "Choose on which offer tile to go ");

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
                        System.out.println(BLUE + "Current player turn: " + game.getPlayerTurn().getName());
                    }
                    break;
                case StateDTO.DRAWCARD:
                    // DrawCardsState
                    if (client.getNickname().equals(game.getPlayerTurn().getName())){
                        int drawn = 0;
                        OfferDTO playerOffer = new OfferDTO('A', 0, 0, 0);
                        for (OfferDTO offerTile : game.getBoard().getOfferPath()) {
                            if (offerTile.getOrder() == game.getPlayerTurn().getOffer()) {
                                playerOffer = offerTile;
                            }
                        }
                        // First time the player goes into DrawCardsState we inizialize the variables drawtop and drawbottom to keep count the draws
                        if(!drawInitialiazed){
                            drawInitialiazed = true; // Setting flag of "first time in DrawCardsState" as true
                            drawTop = playerOffer.getDrawTop();
                            drawBottom = playerOffer.getDrawBottom();
                        }

                        System.out.println("It's your turn!");

                        // Handling if the player has to draw from both Top and Bottom or just one
                        if (drawTop != 0 && drawBottom != 0) {
                            System.out.println("From which row do you wish to draw your card? [T]op or [B]ottom: \n");
                            char chosenRow = readChar();
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

                        if(drawTop == 0 && drawBottom == 0){
                            drawInitialiazed = false;
                        }

                    }
                    else {
                        printBoard();
                        System.out.println(BLUE + "Current player turn: " + game.getPlayerTurn().getName());
                        drawInitialiazed = false;
                    }
                    break;
                case StateDTO.ENDTURN:
                    // EndTurnState
                    System.out.println("EndTurnState\nYou just finished round " + game.getTurnNumber());
                    drawInitialiazed = false;
                    // Check if the player has the building with the EndTurn effect
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
    }

    /**
     * Function that handles the input from the player to draw from bottom row
     * @return
     * @throws IllegalActionException
     * @throws IOException
     * @throws InterruptedException
     * @throws ClassNotFoundException
     */
    private int drawCardBottomRow() throws IllegalActionException, IOException, InterruptedException, ClassNotFoundException {
        int card;
        printRowTribe(game.getBoard().getBottomRowTribe(), game.getBoard().getBottomRowBuilding());
        int size = game.getBoard().getBottomRowTribe().size() + game.getBoard().getBottomRowBuilding().size();
        do{
            System.out.println("Choose which card to draw from the Bottom Row (write its number): ");
            card = readInt() - 1; // fixes index and pos mismatch
        }while(card < 0 || card > size);
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
    }

    /**
     * Function that handles the input from the player to draw from top row
     * @return
     * @throws IllegalActionException
     * @throws IOException
     * @throws InterruptedException
     * @throws ClassNotFoundException
     */
    private int drawCardTopRow() throws IllegalActionException, IOException, InterruptedException, ClassNotFoundException {
        int card;
        printRowTribe(game.getBoard().getTopRowTribe(), game.getBoard().getTopRowBuilding());
        int size = game.getBoard().getTopRowTribe().size() + game.getBoard().getTopRowBuilding().size();
        do{
            System.out.println("Choose which card to draw from the Bottom Row (write its number): ");
            card = readInt() - 1; // fixes index and pos mismatch
        }while(card < 0 || card > size);
        if(!getGameClosed()){
            try {
                client.executeAction(new DrawCardFromTopAction(card));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return 0;
            }
            return -1;
        }
        return -10;
    }


    // -------------------------------- Render functions ---------------------------------------------------------------

    //TODO : what if li uniamo?
    public void renderStartMenu(){
        System.out.println(GREEN + BOLD + "MENU:");
        System.out.println("1. Login");
        System.out.println("2. LeaderBoard");
        System.out.println("3. Exit");
    }

    public void renderMenu(){
        System.out.println(GREEN + BOLD + "MENU:");
        System.out.println("1. Play again");
        System.out.println("2. LeaderBoard");
        System.out.println("3. Exit");
    }

    /**
     * Calls the functions to print each part of the board
     */
    public void printBoard(){
        System.out.println(ORANGE);
        printRowTribe(game.getBoard().getTopRowTribe(), game.getBoard().getTopRowBuilding());
        printRowTribe(game.getBoard().getBottomRowTribe(), game.getBoard().getBottomRowBuilding());
        printOrderTile();
        printOfferRow();
        for(PlayerDTO player : game.getPlayers()){
            printPlayerCards(player);
        }

    }

    /**
     * Function to print the player rankings at the end of the game
     */
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
    //TODO: add che se il player non è tra i primi stampi cmq il suo standing in generale

    /**
     * Prints the otherall scores of all games played available in the database
     * @throws RemoteException
     */
    public void printScoreBoard() throws RemoteException {
        List<LeaderboardDTO> scoreBoard = client.getLeaderboard();
        for(LeaderboardDTO player : scoreBoard){
            System.out.println(String.format("|%-25s|" + player.getNickname() + player.getTotalScore() + player.getNumPlayers()));
        }
    }

    /**
     * Prints both rows based on the input it receives
     * @param cards
     * @param buildings
     */
    public void printRowTribe(List<CardDTO> cards, List<BuildingDTO> buildings){
        System.out.println(ORANGE);
        StringBuilder[] lines = new StringBuilder[7];
        for(int i=0; i < 7; i++){
            lines[i] = new StringBuilder();
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

    //TODO: SPOSTA ANCHE QUESTO IN BUILDING DTO
    public StringBuilder[] printBuildings(List<BuildingDTO> buildings, int numCard){
        numCard++;
        StringBuilder[] lines = new StringBuilder[7];
        for(int i = 0; i < 7; i++){
            lines[i] = new StringBuilder();
        }

        StringBuilder[] printed = new StringBuilder[7];
        for(int i = 0; i < 7; i++){
            printed[i] = new StringBuilder(ORANGE);
        }

        for(BuildingDTO building : buildings){
            printed = building.printCard();
            printed[0] = new StringBuilder().append(String.format("+----%d-----+", numCard++));
            for(int i=0; i<7; i++){
                lines[i].append(printed[i]);
            }
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
        int num_card = 1;

        StringBuilder[] printed = new StringBuilder[7];
        for(int i = 0; i < 7; i++){
            printed[i] = new StringBuilder(ORANGE);
        }

        for(CardDTO card : cards){
            printed = card.printCard();
            printed[0] = new StringBuilder().append(String.format("+----%d-----+", num_card++));
            for(int i=0; i<7; i++){
                lines[i].append(printed[i]);
            }
        }

        return lines;
    }

    /**
     * Function to print a single player's cards
     * @param player
     */
    public void printPlayerCards(PlayerDTO player){
        System.out.println(ORANGE + "\n======== " + player.getName() + " ========");
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
            if(!line.isEmpty()){
                isEmpty = false;
                break;
            }
        }
        if(!isEmpty){
            printLine(lines);
        }else{
            System.out.println(player.getName() + " has no cards...\n");
        }

        System.out.println("Food: " + player.getFood());
        System.out.println("PP: " + player.getPp());
    }

    /**
     * Function to ask whose cards does the user wish to view
     */
    public void choosePlayerCards () throws Exception {
        List<PlayerDTO> players = game.getPlayers();
        System.out.println("Whose cards do you wish to view?");
        String name = readLine();
        for(PlayerDTO player : players){
            if(name.equals(player.getName())){
                printPlayerCards(player);
            }
        }
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
                String paddedName = String.format("|%-10s|", player.getName());
                lines[player.getOrder() + 1] =  new StringBuilder(player.getTotem().getAscii() + paddedName + RESET + ORANGE);
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
                lines[1].append(playerOnTile.getTotem().getAscii() + String.format("|%-10s|", playerOnTile.getName())+ RESET + ORANGE);
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

    private boolean contains(char[] array, char s){
        for(char c : array){
            if(c == s){
                return true;
            }
        }

        return false;
    }

    private boolean handleCommand(String input) throws Exception {
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
            case "info":
                // TODO
            default:
                return false;
        }
    }

    private String readLine() throws Exception {
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
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private char readChar() throws Exception {
        while(true){
            String input = readLine().trim().toUpperCase();
            if(input.length() == 1){
                return input.charAt(0);
            }
            System.out.println(RED + "Please enter a single letter!");
        }
    }
}

