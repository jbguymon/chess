package ui;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import client.NotificationHandler;
import client.WebSocketClient;

import java.util.Scanner;
import java.util.Objects;

import websocket.commands.MakeMoveCommand;
import websocket.messages.*;

import chess.ChessGame;
import client.ServerFacade;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;


public class GameplayUI {
    private final String authToken;
    private final int gameID;
    private final String playerColor;
    private final ServerFacade facade;
    private WebSocketClient webSocketClient;
    private ChessGame currentGame;

    public GameplayUI(String authToken, int gameID, String playerColor, ServerFacade facade){
        this.authToken = authToken;
        this.gameID = gameID;
        this.playerColor = playerColor;
        this.facade = facade;
    }

    public void run(){
        NotificationHandler handler = this::handleServerMessage;
        String wsUrl = "ws://localhost:" + facade.getPort() + "/ws";
        this.webSocketClient = new WebSocketClient(wsUrl, handler);
        sendConnectCommand();
        runGamePlayLoop();
    }

    private void runGamePlayLoop() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type 'help' to see available commands.");
        while(true){
            System.out.print("game> ");
            String input = scanner.nextLine().trim();
            if(input.equalsIgnoreCase("leave")){
                sendLeaveCommand();
                webSocketClient.close();
                System.out.println("You left the game.");
                return;
            }
            handleGameplayCommand(input);
        }
    }

    private void sendConnectCommand(){
        try{
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            String json = facade.getGson().toJson(command);
            webSocketClient.sendMessage(json);
        }
        catch (Exception exception){
            System.out.println("Failed to send CONNECT: " + exception.getMessage());
        }
    }

    private void handleServerMessage(String json){
        try{
            ServerMessage message = facade.getGson().fromJson(json, ServerMessage.class);
            if(message == null){
                return;
            }
            switch (message.getServerMessageType()){
                case LOAD_GAME:
                    LoadGameMessage loadMsg = facade.getGson().fromJson(json, LoadGameMessage.class);
                    this.currentGame = loadMsg.getGame();
                    boolean isWhite = Objects.equals(playerColor, "WHITE") || playerColor == null;
                    ChessBoardUI.displayBoard(currentGame.getBoard(), isWhite);
                    break;

                case NOTIFICATION:
                    NotificationMessage notif = facade.getGson().fromJson(json, NotificationMessage.class);
                    System.out.println(notif.getMessage());
                    break;

                case ERROR:
                    ErrorMessage err = facade.getGson().fromJson(json, ErrorMessage.class);
                    System.out.println("Error: " + err.getErrorMessage());
                    break;

                default:
                    System.out.println("Unknown message received.");
            }
        }
        catch(Exception exception){
            System.out.println("Failed to parse message: " + json);
        }
    }

    private void sendLeaveCommand(){
        try{
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            String json = facade.getGson().toJson(command);
            webSocketClient.sendMessage(json);
        }
        catch(Exception exception){
            System.out.println("Failed to leave game.");
        }
    }

    private void handleGameplayCommand(String input){
        if(input.isBlank()){
            return;
        }
        String[] tokens = input.trim().split("\\s+");
        String command = tokens[0].toLowerCase();
        try{
            switch (command){
                case "help":
                    printGameplayHelp();
                    break;

                case "redraw":
                    redrawBoard();
                    break;

                case "move":
                    handleMakeMove(tokens);
                    break;
                case "highlight":
                    handleHighlight(tokens);
                    break;
                case "resign":
                    handleResign();
                    break;
                default:
                    System.out.println("Unknown Command.");
                    printGameplayHelp();
            }
        }
        catch(Exception exception){
            System.out.println("Error: " + exception.getMessage());
        }
    }

    private void printGameplayHelp(){
        System.out.println("""
                Gameplay Commands:
                move <start> <end>
                highlight <square>
                redraw
                resign
                leave
                help
                """);
    }

    private void redrawBoard(){
        if(currentGame == null){
            System.out.println("No board to display currently.");
            return;
        }
        boolean isWhite = Objects.equals(playerColor, "WHITE") || playerColor == null;
        ChessBoardUI.displayBoard(currentGame.getBoard(), isWhite);
    }

    private void handleResign(){
        System.out.print("Do you actually want to resign? (y/n): ");
        Scanner scanner = new Scanner(System.in);
        if(scanner.nextLine().trim().equalsIgnoreCase("y")){
            sendResignCommand();
        }
        else{
            System.out.println("Not resigning.");
        }
    }

    private void sendResignCommand(){
        try{
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            String json = facade.getGson().toJson(command);
            webSocketClient.sendMessage(json);
        }
        catch (Exception exception){
            System.out.println("Failed to resign.");
        }
    }

    private void handleMakeMove(String[] tokens){
        if(currentGame.isGameOver()){
            System.out.println("Game is over.");
            return;
        }
        if(tokens.length < 3){
            System.out.println("Usage: move <start> <end>.");
            return;
        }
        try{
            chess.ChessPosition start = parsePosition(tokens[1]);
            chess.ChessPosition end = parsePosition(tokens[2]);
            ChessPiece.PieceType promotion = null;
            if(isPromotionMove(start, end)){
                System.out.print("What would you like to promote to (q/r/b/n)?: ");
                String promChoice = new Scanner(System.in).nextLine().trim().toLowerCase();
                promotion = switch (promChoice){
                    case "q" -> ChessPiece.PieceType.QUEEN;
                    case "r" -> ChessPiece.PieceType.ROOK;
                    case "b" -> ChessPiece.PieceType.BISHOP;
                    case "n" -> ChessPiece.PieceType.KNIGHT;
                    default -> ChessPiece.PieceType.QUEEN;
                };
            }
            chess.ChessMove move = new ChessMove(start, end, promotion);
            MakeMoveCommand command = new MakeMoveCommand(authToken, gameID, move);
            String json = facade.getGson().toJson(command);
            webSocketClient.sendMessage(json);
            System.out.println("Moved: " + tokens[1] + " to " + tokens[2]);
        }
        catch(IllegalArgumentException exception){
            System.out.println("Invalid format for position.");
        }
        catch(Exception exception){
            System.out.println("Error creating or sending move.");
        }
    }

    private void handleHighlight(String[] tokens){
        if (tokens.length < 2) {
            System.out.println("Usage: highlight <tile>");
            return;
        }
        try {
            chess.ChessPosition position = parsePosition(tokens[1]);
            if (currentGame == null) {
                System.out.println("No game loaded.");
                return;
            }
            var validMoves = currentGame.validMoves(position);
            boolean isWhitePerspective = Objects.equals(playerColor, "WHITE") || playerColor == null;

            if (validMoves == null || validMoves.isEmpty()) {
                System.out.println("No legal moves from " + tokens[1]);
                ChessBoardUI.displayBoard(currentGame.getBoard(), isWhitePerspective); // normal redraw
            } else {
                ChessBoardUI.displayBoardWithHighlights(currentGame.getBoard(), isWhitePerspective, position, validMoves);
                System.out.println("Highlighted legal moves from " + tokens[1]);
            }
        } catch (Exception e) {
            System.out.println("Invalid square.");
        }
    }

    private chess.ChessPosition parsePosition(String pos){
        if (pos == null || pos.length() != 2) {
            throw new IllegalArgumentException("Invalid position");
        }
        char file = pos.charAt(0);
        char rank = pos.charAt(1);
        if(file < 'a' || file > 'h' || rank < '1' || rank > '8'){
            throw new IllegalArgumentException("Invalid position: " + pos);
        }

        int column = file - 'a' + 1;
        int row = Character.getNumericValue(rank);

        return new chess.ChessPosition(row, column);
    }

    private boolean isPromotionMove(ChessPosition start, ChessPosition end){
        return (start.getRow() == 7 && end.getRow() == 8) || (start.getRow() == 2 && end.getRow() == 1);
    }
}
