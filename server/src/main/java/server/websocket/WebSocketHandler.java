package server.websocket;

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
    private static final ConcurrentHashMap<Integer, ConcurrentHashMap<WsContext, String>> gameSessions = new ConcurrentHashMap<>();
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

        } catch (Exception e) {
            e.printStackTrace();
            sendError(ctx, "Error processing command: " + e.getMessage());
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
            GameData gameData = data.getGame(gameID);
            if (gameData == null || gameData.game() == null) {
                sendError(ctx, "Game not found");
                return;
            }
            gameSessions.computeIfAbsent(gameID, k -> new ConcurrentHashMap<>())
                    .put(ctx, authToken);
            sendToClient(ctx, new LoadGameMessage(gameData.game()));
            String username = getUsername(authToken);
            broadcastToGameExcept(gameID, ctx,
                    new NotificationMessage(username + " joined the game"));

        } catch (Exception e) {
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
            if (!username.equals(gameData.whiteUsername()) &&
                    !username.equals(gameData.blackUsername())) {
                sendError(ctx, "Observers cannot make moves");
                return;
            }
            if (!isPlayersTurn(gameData, game, username)) {
                sendError(ctx, "Not your turn");
                return;
            }
            game.makeMove(cmd.getMove());
            ChessGame.TeamColor opponent = game.getTeamTurn();
            if(game.isInCheckmate(opponent)){
                game.setGameOver(true);
                broadcastToGame(gameID,
                        new NotificationMessage("Checkmate! " + username + " wins."));
            }
            if(game.isInStalemate(opponent)){
                game.setGameOver(true);
                broadcastToGame(gameID,
                        new NotificationMessage("Stalemate! Draw."));
            }
            GameData updatedGame = new GameData(
                    gameData.gameID(),
                    gameData.whiteUsername(),
                    gameData.blackUsername(),
                    gameData.gameName(),
                    game
            );
            data.updateGame(updatedGame);
            broadcastToGame(gameID, new LoadGameMessage(game));
            broadcastToGameExcept(gameID, ctx,
                    new NotificationMessage(username + " made a move"));
        }
        catch (Exception e) {
            sendError(ctx, "Invalid move: " + e.getMessage());
        }
    }

    private void handleLeave(UserGameCommand cmd, WsContext ctx) {
        removeSessionFromGame(cmd.getGameID(), ctx);
        String username = getUsername(cmd.getAuthToken());
        broadcastToGame(cmd.getGameID(),
                new NotificationMessage(username + " left the game"));
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
        if (sessions == null) return;

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
        if (sessions == null) return;
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
            if (auth == null) return "Unknown";
            return auth.username();
        } catch (Exception e) {
            return "Unknown";
        }
    }
}