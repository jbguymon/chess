package server;

import io.javalin.*;
import service.ClearService;
import handler.ClearHandler;
import service.UserService;
import handler.UserHandler;
import service.GameService;
import handler.GameHandler;
import dataaccess.MySqlDataAccess;
import dataaccess.DatabaseInitializer;
import dataaccess.DataAccessException;
import server.websocket.WebSocketHandler;

public class Server {

    private final Javalin javalin;;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        var data = new MySqlDataAccess();
        var clearService = new ClearService(data);
        var clearHandler = new ClearHandler(clearService);

        javalin.delete("/db", clearHandler::handle);

        var userService = new UserService(data);
        var userHandler = new UserHandler(userService);

        javalin.post("/user", userHandler::register);
        javalin.post("/session", userHandler::login);
        javalin.delete("/session", userHandler::logout);

        var gameService = new GameService(data);
        var gameHandler = new GameHandler(gameService);

        javalin.get("/game", gameHandler::listGames);
        javalin.post("/game", gameHandler::createGame);
        javalin.put("/game", gameHandler::joinGame);

        WebSocketHandler handler = new WebSocketHandler(data);
        javalin.ws("/ws", ws -> {
            ws.onConnect(handler::onConnect);
            ws.onMessage(handler::onMessage);
            ws.onClose(handler::onClose);
            ws.onError(handler::onError);
        });
    }

    public int run(int desiredPort) {
        try {
            DatabaseInitializer.initialize();
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
