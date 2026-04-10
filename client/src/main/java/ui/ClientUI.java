package ui;

import client.ServerFacade;
import client.WebSocketClient;
import model.*;

import java.util.Arrays;
import java.util.Scanner;
import java.util.List;
import ui.GameplayUI;

public class ClientUI {
    private final ServerFacade facade;
    private boolean isLoggedIn = false;
    private Integer currentGameID;
    private boolean isInGame = false;
    private String playerColor;
    private WebSocketClient webSocketClient;
    private String authToken;

    public ClientUI(int port){
        this.facade = new ServerFacade(port);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            try {
                handleCommand(input);
            } catch (Exception exception) {
                String msg = exception.getMessage();
                if(msg.contains("unauthorized")){
                    System.out.println("Invalid username or password.");
                }
                else if(msg.contains("already taken")){
                    if(!isLoggedIn) {
                        System.out.println("Username already exists.");
                    }
                    else{
                        System.out.println("Color already taken.");
                    }
                }
                else if(msg.contains("bad request")){
                    System.out.println("Invalid input.");
                }
                else{
                    System.out.println("Error: " + msg);
                }
            }
        }
    }

    private void handleCommand(String input) throws Exception{
        String[] tokens = input.trim().split("\\s+");
        String command = tokens[0].toLowerCase();
        if(!isLoggedIn){
            preLogin(command, tokens);
        }
        else{
            postLogin(command, tokens);
        }
    }

    private void preLogin(String command, String[] tokens) throws Exception{
        switch(command){
            case "help":
                printHelpPreLogin();
                break;
            case "register":
                if(tokens.length != 4){
                    System.out.println("Incorrect usage. Correct usage: register <username> <password> <email>");
                    return;
                }
                facade.register(tokens[1], tokens[2], tokens[3]);
                this.authToken = facade.getAuthToken();
                System.out.println("Registered and automatically logged in.");
                isLoggedIn = true;
                break;
            case "login":
                if(tokens.length != 3){
                    System.out.println("Incorrect usage. Correct usage: login <username> <password>");
                    return;
                }
                facade.login(tokens[1], tokens[2]);
                this.authToken = facade.getAuthToken();
                System.out.println("Successfully logged in.");
                isLoggedIn = true;
                break;
            case "quit":
                System.exit(0);
                break;
            default:
                System.out.println("Command Unknown.");
                printHelpPreLogin();
        }
    }

    private void postLogin(String command, String[] tokens) throws Exception{
        switch(command){
            case "help":
                printHelpPostLogin();
                break;
            case "logout":
                facade.logout();
                isLoggedIn = false;
                authToken = null;
                System.out.println("Logged out.");
                break;
            case "create":
                if(tokens.length >= 2 && tokens[1].equalsIgnoreCase("game")){
                    if(tokens.length < 3){
                        System.out.println("Incorrect Usage. Correct Usage: create game <game name>");
                        return;
                    }
                    String gameName = String.join(" ", Arrays.copyOfRange(tokens, 2, tokens.length));
                    facade.createGame(gameName);
                    System.out.println("Game Successfully Created");
                }
                else{
                    System.out.println("Unknown command. Did you mean 'create game'?");
                }
                break;
            case "list":
                if(tokens.length == 2 && tokens[1].equalsIgnoreCase("games")) {
                    List<GameData> games = facade.listGames();
                    printGames(games);
                }
                else{
                    System.out.println("Unknown Command. Did you mean 'list games'?");
                }
                break;
            case "play":
                if(tokens.length == 4 && tokens[1].equalsIgnoreCase("game")){
                    List<GameData> games = facade.listGames();
                    int gameNum;
                    try{
                        gameNum = Integer.parseInt(tokens[2]);
                    }
                    catch (NumberFormatException exception){
                        System.out.println("That is not a number. Please use a valid game number.");
                        return;
                    }
                    String color = tokens[3].toUpperCase();
                    if(!color.equals("WHITE") && !color.equals("BLACK")){
                        System.out.println("Enter a valid color. Correct usage: play game <number> <WHITE/BLACK>");
                        return;
                    }
                    if(gameNum > facade.listGames().size() || gameNum < 1){
                        System.out.println("That game doesn't exist. Please use a valid game number.");
                        return;
                    }
                    GameData chosenGame = games.get(gameNum - 1);
                    int gameID = chosenGame.gameID();
                    facade.joinGame(gameNum, color);
                    System.out.println("Successfully join game as " + color + ".");
                    new GameplayUI(authToken, gameID, color, facade).run();
                }
                else{
                    System.out.println("Unknown command. Did you mean 'play game'?");
                }
                break;
            case "observe":
                if(tokens.length != 3){
                    System.out.println("Correct usage: observe game <game number>.");
                }
                if(tokens.length == 3 && tokens[1].equalsIgnoreCase("game")){
                    List<GameData> games = facade.listGames();
                    int num;
                    try{
                        num = Integer.parseInt(tokens[2]);
                    }
                    catch (NumberFormatException exception){
                        System.out.println("That is not a number. Please use a valid game number.");
                        return;
                    }
                    if(num > facade.listGames().size() || num < 1){
                        System.out.println("That game doesn't exist. Please use a valid game number.");
                        return;
                    }
                    GameData chosenGame = games.get(num - 1);
                    int gameID = chosenGame.gameID();
                    facade.observeGame(num);
                    System.out.println("Successfully observing game");
                    new GameplayUI(authToken, gameID, null, facade).run();
                }
                else{
                    System.out.println("Unknown command. Did you mean 'observe game'?");
                }
                break;
            case "quit":
                System.exit(0);
                break;
            default:
                System.out.println("Unknown command.");
                printHelpPostLogin();
        }
    }

    private void printHelpPreLogin(){
        System.out.println("""
                        Commands:
                        register <username> <password> <email>
                        login <username> <password>
                        help
                        quit
                """);
    }

    private void printHelpPostLogin(){
        System.out.println("""
                Commands:
                create game <game name>
                list games
                play game <game number> <WHITE/BLACK>
                observe game <game number>
                logout
                help
                quit""");
    }

    private void printGames(List<GameData> games){
        for(int i = 0; i < games.size(); i++){
            GameData gameData = games.get(i);
            System.out.printf("%d. %s (White: %s, Black: %s)%n", i + 1, gameData.gameName(),
                    gameData.whiteUsername() == null ? "-" : gameData.whiteUsername(),
                    gameData.blackUsername() == null ? "-" : gameData.blackUsername());
        }
    }
}
