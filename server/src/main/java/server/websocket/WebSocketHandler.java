package server.websocket;

import chess.ChessMove;
import chess.ChessPosition;
import dataaccess.MySqlDataAccess;
import io.javalin.websocket.*;
import model.AuthData;
import websocket.commands.*;
import websocket.messages.*;
import chess.ChessGame;
import model.GameData;
import com.google.gson.Gson;

import java.util.concurrent.ConcurrentHashMap;

public class WebSocketHandler {
    private final ConcurrentHashMap<Integer, ConcurrentHashMap<WsContext, String>> gameSessions = new ConcurrentHashMap<>();
    private final MySqlDataAccess data;
    private final Gson gson = new Gson();

    public WebSocketHandler(MySqlDataAccess data) {
        this.data = data;
    }

    public void onConnect(WsConnectContext ctx) {
        System.out.println("WebSocket connected: " + ctx.sessionId());
    }

    public void onMessage(WsMessageContext ctx) {
        try {
            UserGameCommand baseCommand = gson.fromJson(ctx.message(), UserGameCommand.class);
            if (baseCommand == null || baseCommand.getAuthToken() == null || baseCommand.getGameID() == null) {
                sendError(ctx, "Bad request: missing authToken or gameID");
                return;
            }
            System.out.println("Received command: " + baseCommand.getCommandType() + " for game " + baseCommand.getGameID());
            switch (baseCommand.getCommandType()) {
                case CONNECT -> handleConnect(baseCommand, ctx);
                case MAKE_MOVE -> {
                    MakeMoveCommand moveCmd =
                            gson.fromJson(ctx.message(), MakeMoveCommand.class);
                    handleMakeMove(moveCmd, ctx);
                }
                case LEAVE -> handleLeave(baseCommand, ctx);
                case RESIGN -> handleResign(baseCommand, ctx);
                default -> sendError(ctx, "Unknown command type");
            }

        } catch (Exception exception) {
            sendError(ctx, "Error processing command: " + exception.getMessage());
        }
    }

    public void onClose(WsCloseContext ctx) {
        removeSessionFromAllGames(ctx);
        System.out.println("WebSocket closed: " + ctx.sessionId()
                + " (status: " + ctx.status() + ")");
    }

    public void onError(WsErrorContext ctx) {
        System.err.println("WebSocket error for " + ctx.sessionId() + ": " + ctx.error().getMessage());
        removeSessionFromAllGames(ctx);
    }

    private void handleConnect(UserGameCommand cmd, WsContext ctx) {
        int gameID = cmd.getGameID();
        String authToken = cmd.getAuthToken();
        try {
            AuthData auth = data.getAuth(authToken);
            if(auth == null){
                sendError(ctx, "Unauthorized");
                return;
            }
            GameData gameData = data.getGame(gameID);
            if (gameData == null || gameData.game() == null) {
                sendError(ctx, "Game not found");
                return;
            }
            gameSessions.computeIfAbsent(gameID, k -> new ConcurrentHashMap<>())
                    .put(ctx, authToken);
            sendToClient(ctx, new LoadGameMessage(gameData.game()));
            String username = getUsername(authToken);
            String color;
            if(username.equals(gameData.whiteUsername())){
                color = "WHITE";
            }
            else if(username.equals(gameData.blackUsername())){
                color = "BLACK";
            }
            else{
                color = "observer";
            }
            broadcastToGameExcept(gameID, ctx,
                    new NotificationMessage(username + " joined the game as " + color + "."));
        }
        catch (Exception exception) {
            sendError(ctx, "Failed to connect to game");
        }
    }

    private void handleMakeMove(MakeMoveCommand cmd, WsContext ctx) {
        try {
            int gameID = cmd.getGameID();
            String authToken = cmd.getAuthToken();
            GameData gameData = data.getGame(gameID);
            if (gameData == null || gameData.game() == null) {
                sendError(ctx, "Game not found");
                return;
            }
            ChessGame game = gameData.game();
            String username = getUsername(authToken);
            if (game.isGameOver()) {
                sendError(ctx, "Game is over");
                return;
            }
            if (!username.equals(gameData.whiteUsername()) &&
                    !username.equals(gameData.blackUsername())) {
                sendError(ctx, "Observers cannot make moves");
                return;
            }
            if (!isPlayersTurn(gameData, game, username)) {
                sendError(ctx, "Not your turn");
                return;
            }
            ChessMove move = cmd.getMove();
            game.makeMove(cmd.getMove());
            boolean isCheckmate = game.isInCheckmate(game.getTeamTurn());
            boolean isStalemate = game.isInStalemate(game.getTeamTurn());
            GameData updatedGame = new GameData(
                    gameData.gameID(),
                    gameData.whiteUsername(),
                    gameData.blackUsername(),
                    gameData.gameName(),
                    game
            );
            data.updateGame(updatedGame);
            broadcastToGame(gameID, new LoadGameMessage(game));
            broadcastNotificationExcept(gameID, ctx, username + " made a move: " +
                    moveParser(move.getStartPosition()) + " " + moveParser(move.getEndPosition()));
            if (isCheckmate) {
                game.setGameOver(true);
                broadcastNotification(gameID, "Checkmate! " + username + " wins.");
            }
            else if (isStalemate) {
                game.setGameOver(true);
                broadcastNotification(gameID, "Stalemate! Draw.");
            }
        }
        catch (Exception exception) {
            String errorMsg = exception.getMessage() != null ? exception.getMessage() : "Invalid move";
            System.err.println("Invalid move attempted by " + getUsername(cmd.getAuthToken()) + ": " + errorMsg);
            sendError(ctx, "Invalid move. You made an illegal or invalid move.");
        }
    }

    private void handleLeave(UserGameCommand cmd, WsContext ctx) {
        try {
            int gameID = cmd.getGameID();
            String authToken = cmd.getAuthToken();
            String username = getUsername(authToken);
            removeSessionFromGame(gameID, ctx);
            try {
                GameData gameData = data.getGame(gameID);
                if (gameData != null) {
                    String white = gameData.whiteUsername();
                    String black = gameData.blackUsername();
                    if (username.equals(white)) {
                        white = null;
                    } else if (username.equals(black)) {
                        black = null;
                    }
                    GameData updatedGame = new GameData(
                            gameID,
                            white,
                            black,
                            gameData.gameName(),
                            gameData.game()
                    );
                    data.updateGame(updatedGame);
                }
            } catch (Exception dbEx) {
                System.err.println("Warning: Could not clear player slot on leave (game may be over): " + dbEx.getMessage());
            }
            broadcastToGameExcept(gameID, ctx,
                    new NotificationMessage(username + " left the game"));
            System.out.println("✅ " + username + " left game " + gameID + " successfully");
        } catch (Exception e) {
            System.err.println("Error in handleLeave: " + e.getMessage());
            sendError(ctx, "Error leaving game");
        }
    }

    private void handleResign(UserGameCommand cmd, WsContext ctx) {
        try {
            int gameID = cmd.getGameID();
            String authToken = cmd.getAuthToken();
            GameData gameData = data.getGame(gameID);
            if (gameData == null || gameData.game() == null) {
                sendError(ctx, "Game not found");
                return;
            }
            ChessGame game = gameData.game();
            String username = getUsername(authToken);
            if (game.isGameOver()) {
                sendError(ctx, "Game already over");
                return;
            }
            if (!username.equals(gameData.whiteUsername()) &&
                    !username.equals(gameData.blackUsername())) {
                sendError(ctx, "Observers cannot resign");
                return;
            }
            game.setGameOver(true);
            GameData updatedGame = new GameData(
                    gameData.gameID(),
                    gameData.whiteUsername(),
                    gameData.blackUsername(),
                    gameData.gameName(),
                    game
            );
            data.updateGame(updatedGame);
            broadcastToGame(gameID,
                    new NotificationMessage(username + " resigned. Game over."));
        }
        catch (Exception exception) {
            sendError(ctx, "Error processing resign");
        }
    }

    private void sendToClient(WsContext ctx, ServerMessage message) {
        try {
            ctx.send(gson.toJson(message));
        } catch (Exception ignored) {}
    }

    private void broadcastToGame(int gameID, ServerMessage message) {
        var sessions = gameSessions.get(gameID);
        if (sessions == null){
            return;
        }

        String json = gson.toJson(message);

        for (WsContext client : sessions.keySet()) {
            if (client.session.isOpen()) {
                try {
                    client.send(json);
                } catch (Exception ignored) {}
            }
        }
    }

    private void broadcastToGameExcept(int gameID, WsContext exclude, ServerMessage message) {
        var sessions = gameSessions.get(gameID);
        if (sessions == null){
            return;
        }
        String json = gson.toJson(message);
        for (WsContext client : sessions.keySet()) {
            if (client != exclude && client.session.isOpen()) {
                try {
                    client.send(json);
                } catch (Exception ignored) {}
            }
        }
    }

    private void sendError(WsContext ctx, String errorText) {
        try {
            ctx.send(gson.toJson(new ErrorMessage(errorText)));
        } catch (Exception ignored) {}
    }

    private void removeSessionFromGame(int gameID, WsContext ctx) {
        var map = gameSessions.get(gameID);
        if (map != null) {
            map.remove(ctx);
            if (map.isEmpty()) {
                gameSessions.remove(gameID);
            }
        }
    }

    private void removeSessionFromAllGames(WsContext ctx) {
        gameSessions.values().forEach(map -> map.remove(ctx));
    }

    private boolean isPlayersTurn(GameData gameData, ChessGame game, String username) {
        if (game.getTeamTurn() == ChessGame.TeamColor.WHITE) {
            return username.equals(gameData.whiteUsername());
        } else {
            return username.equals(gameData.blackUsername());
        }
    }

    private String getUsername(String authToken) {
        try {
            AuthData auth = data.getAuth(authToken);
            if (auth == null){
                return "Unknown";
            }
            return auth.username();
        } catch (Exception e) {
            return "Unknown";
        }
    }

    private void broadcastNotificationExcept(int gameID, WsContext excludeCtx, String text) {
        var sessions = gameSessions.get(gameID);
        if (sessions == null) {
            return;
        }

        NotificationMessage notif = new NotificationMessage(text);
        String json = gson.toJson(notif);

        for (WsContext client : sessions.keySet()) {
            if (client.session == excludeCtx.session){
                continue;
            }
            if (client.session.isOpen()) {
                try {
                    client.send(json);
                } catch (Exception ignored) {}
            }
        }
    }

    private void broadcastNotification(int gameID, String text) {
        var sessions = gameSessions.get(gameID);
        if (sessions == null) {
            return;
        }

        NotificationMessage notif = new NotificationMessage(text);
        String json = gson.toJson(notif);

        for (WsContext client : sessions.keySet()) {
                try {
                    client.send(json);
                }
                catch (Exception ignored) {}
        }
    }

    private String moveParser(ChessPosition position){
        char file = (char) ('a' + position.getColumn() - 1);
        int rank = position.getRow();
        return file + String.valueOf(rank);
    }
}