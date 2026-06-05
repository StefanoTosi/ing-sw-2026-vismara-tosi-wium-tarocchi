package it.polimi.ingsw.UI.TUI;

import it.polimi.ingsw.controller.actions.*;
import it.polimi.ingsw.controller.states.ChooseTotemState;
import it.polimi.ingsw.controller.states.StateDTO;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.OfferDTO;
import it.polimi.ingsw.model.board.OrderDTO;
import it.polimi.ingsw.model.characters.DTO.*;
import it.polimi.ingsw.model.effects.BuildingDTO;
import it.polimi.ingsw.model.events.Event;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.model.exceptions.SkipActionException;
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
                    String username = in.nextLine();
                    System.out.print(BLUE + BOLD +"Password: ");
                    String password = in.nextLine();
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
            game = updates.take();
            int dim = updates.size();
            for(int i = 0; i < dim; i++) {
                game = updates.take(); // Takes the new state at the beginning of every action
            }
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
                            char chosenRow;
                            do {
                                System.out.println("From which row do you wish to draw your card? [T]op or [B]ottom: \n");
                                chosenRow = readChar();
                            }while(chosenRow != 'T' && chosenRow != 'B');
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
                    if (client.getNickname().equals(game.getPlayerTurn().getName())) {
                        System.out.println("EndTurnState\nYou just finished round " + game.getTurnNumber());
                        drawInitialiazed = false;
                        // Check if the player has the building with the EndTurn effect
                        if (game.getPlayerTurn().getCanPickFromTop()) {
                            try{
                                int effectDraw = 1;
                                effectDraw = drawCardTopRow();
                            }catch (SkipActionException e){
                                System.out.println("Skip executed, wait for update");
                            }
                        }
                    }
                    else {
                        System.out.println(BLUE + "Current player turn: " + game.getPlayerTurn().getName());
                    }
                    break;
                case StateDTO.ENDGAME:
                    // EndGameState
                    printRankings();
                    client.leaveMatch();
                    anotherGame();
                    break;
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
    private int drawCardBottomRow() throws Exception {
        int card;
        printRowTribe(game.getBoard().getBottomRowTribe(), game.getBoard().getBottomRowBuilding());
        int size = game.getBoard().getBottomRowTribe().size() + game.getBoard().getBottomRowBuilding().size();

        do {
            System.out.println("Choose which card to draw from the Bottom Row (write its number): ");
            card = readInt(); // fixes index and pos mismatch
        } while (card <= 0 || card > size);
        if (!getGameClosed()) {
            try {
                client.executeAction(new DrawCardFromBottomAction(card - 1));
            } catch (Exception e) {
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
    private int drawCardTopRow() throws Exception {
        int card;
        printRowTribe(game.getBoard().getTopRowTribe(), game.getBoard().getTopRowBuilding());
        int size = game.getBoard().getTopRowTribe().size() + game.getBoard().getTopRowBuilding().size();
        try {
            do {
                System.out.println("Choose which card to draw from the Top Row (write its number): ");
                card = readInt();
            } while (card <= 0 || card > size);
        }catch (SkipActionException e){
            return -1;
        }
        if (!getGameClosed()) {
            try {
                client.executeAction(new DrawCardFromTopAction(card - 1));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return 0;
            }
            return -1;
        }

        return -10;
    }


    // -------------------------------- Render functions ---------------------------------------------------------------

    /**
     * Prints First Menu
     */
    public void renderStartMenu(){
        System.out.println(GREEN + BOLD + "MENU:");
        System.out.println("1. Login");
        System.out.println("2. LeaderBoard");
        System.out.println("3. Exit");
    }

    /**
     * Prints Restart Menu
     */
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
    public void printScoreBoard() throws IOException, IllegalActionException, InterruptedException, ClassNotFoundException {
        List<LeaderboardDTO> scoreBoard = client.getLeaderboard();
        System.out.println("Which ranking would you like to view? Number of players from 2 to 5:");
        int in = readInt();
        for(LeaderboardDTO player : scoreBoard){
            if(player.getNumPlayers() == in){
                System.out.println(player.getNickname() + " | " + player.getTotalScore() + " | " + player.getNumPlayers());
            }
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

        int numCard = cards.size() + 1;
        //------------- Adding the buildings
        for(BuildingDTO building : buildings){
            build = building.printCard();
            build[0] = new StringBuilder().append(String.format("+----%d-----+", numCard++));
            for(int i=0; i<7; i++){
                lines[i].append(build[i]);
            }
        }
        printLine(lines);
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
        for (BuildingDTO building : player.getBuildings()){
            appendLines(lines, building.printCard());
        }
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
        lines[0].append(ORANGE + "+----------------+");
        for(int i = 1; i < num-1; i++){
            lines[i].append(String.format("|%-16s|", "Food: " + orderTile.getFoodBonus().get(i-1) + " Pp: " + orderTile.getPpBonus().get(i-1)));
        }
        lines[num-1].append("+----------------+");

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

    /**
     * Prints the Info for the game
     */
    public void printInfo(){
        System.out.println(
                ORANGE + "Inline Commands\n" +
                GREEN + "exit/quit: let's you exit the game when you want\n" +
                "board: prints the board when you want to see it\n" +
                "info: prints this info card\n" +
                "skip: lets you skip the draw turn when there are only building to draw\n" +
                ORANGE + "Event Rules:\n" +
                BLUE + "During Shamanic Ritual if you are the one with most stars you win N1 PPs, the losers loose N2 PPs.\n" +
                "During Hunt for every hunter you get one food and N PPs.\n" +
                "During CavePaintings if you have less than N artists, you loose N PPs, if you have more, you win N PPs.\n" +
                "During Sustenance you have to pay 1 food for each of you tribe members, if you run out of food you have to pay N PPs for each.\n" +
                ORANGE + "Building Effects:\n" +
                BLUE + "D1: Starting from when you have this Building, every time you complete a set of 6 different \n" +
                        "Character cards, you take 5 Food tokens. You do not receive Food tokens for sets \n" +
                        "already completed at the time of acquiring the Building\n"+
                "ES1: During the Sustenance Event, you have a discount of 1 Food token on the total you \n" +
                        "would have to pay, for each of the indicated Characters in your tribe (Artists/Inventors/Gatherers).\n"+
                "ESC1: During the Shamanic Ritual Event, you do not lose Prestige Points if you have fewer star icons \n" +
                        "than all other players.\n"+
                "ET1: If at the end of your turn (also during the last round), when you move your Totem back to \n" +
                        "the Turn Order tile, you place it in a space that provides a bonus in Food, you immediately \n" +
                        "take 1 additional Food token. If you place the Totem in the last space, you pay 1 Food token \n" +
                        "normally, and the building has no effect.\n"+
                "D2: Starting from when you have this Building, every time you obtain a pair of identical \n" +
                        "Inventors (with the same Invention icon), you take 3 Food tokens. You do not take Food for \n" +
                        "pairs already owned at the time of acquiring the Building.\n"+
                "ESC2: During the Shamanic Ritual Event, your tribe has 3 additional  icons.\n"+
                "ESC3: During the Shamanic Ritual Event, if you have more  icons than any of the other players, \n" +
                        "you gain double the indicated Prestige Points. You still gain Prestige Points in case of a tie.  \n"+
                "EH: During the Hunt Event, you take 1 Food token and gain 1 additional Prestige Point for each \n" +
                        "Hunter in your tribe.\n"+
                "EG1: At the end of the game, you gain double the Prestige Points indicated on the Builder cards \n" +
                        "in your tribe.\n"+
                "EG2: At the end of the game, you gain 6 Prestige Points for each set of 6 different Character \n" +
                        "cards in your tribe.\n"+
                "EG3: At the end of the game, you gain the indicated amount of Prestige Points for each Character \n" +
                        "card of the indicated type in your tribe.\n"+
                "ECP: During the Cave Paintings Event, you take 1 Food token for each Artist in your tribe.\n"+
                "ET2: After resolving all actions (once all Totems have been moved back to the Turn Order tile) \n" +
                        "and before the End of the Round phase, you can take 1 Character or 1 Building card (paying \n" +
                        "its cost) from the top row.\n"+
                "EG4: At the end of the game, you gain 25 Prestige Points.\n"
        );
    }

    // --------------------------- Helper functions ----------------------------------------------------------

    /**
     * Helper function to print
     * @param lines
     */
    public void printLine(StringBuilder[] lines){
        for(StringBuilder line : lines){
            System.out.println(line);
        }
    }

    /**
     * Helper function to unite two lines
     * @param first
     * @param second
     */
    public void appendLines(StringBuilder[] first, StringBuilder[] second){
        for(int i = 0; i < first.length; i++){
            first[i].append(second[i]);
        }
    }

    /**
     * Helper function to check if a char is contained in a string
     * @param array
     * @param s
     * @return
     */
    private boolean contains(char[] array, char s){
        for(char c : array){
            if(c == s){
                return true;
            }
        }

        return false;
    }

    /**
     * Handles manual inputs
     * @param input
     * @return boolean
     * @throws Exception
     */
    private boolean handleCommand(String input) throws Exception {
        switch (input.toLowerCase()){
            case "exit":
            case "quit":
                if(gameClosed){
                    client.leaveGame();
                    System.exit(0);
                } else {
                    client.leaveMatch();
                    client.stopGame(client.getNickname());
                    anotherGame();
                }
                return true;
            case "board":
                printBoard();
                return true;
            case "info":
                printInfo();
                return true;
            case "skip":
                try {
                    client.executeAction(new SkipDrawAction());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return true;
                } throw new SkipActionException();
            default:
                return false;
        }
    }

    /**
     * Reads the input when it's a String
     * @return String input
     * @throws Exception
     */
    private String readLine() throws Exception {
        while(true){
            String input = in.nextLine().trim();
            if(handleCommand(input)){
                continue;
            }
            return  input;
        }
    }

    /**
     * Reads the input when it's an Int
     * @return Int input
     * @throws IllegalActionException
     * @throws IOException
     * @throws InterruptedException
     * @throws ClassNotFoundException
     */
    private int readInt() throws IllegalActionException, IOException, InterruptedException, ClassNotFoundException {
        while(true){
            try{
                return Integer.parseInt(readLine());
            } catch (NumberFormatException e) {
                System.out.println(RED + "Please enter a valid number!");
            }catch (SkipActionException e){
                throw e;
            }catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Reads the input when it's a Char
     * @return Char
     * @throws Exception
     */
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

