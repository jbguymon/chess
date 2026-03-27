package server;

import io.javalin.*;
import dataaccess.MemoryDataAccess;
import service.ClearService;
import handler.ClearHandler;
import service.UserService;
import handler.UserHandler;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        var data = new MemoryDataAccess();
        var clearService = new ClearService(data);
        var clearHandler = new ClearHandler(clearService);

        javalin.delete("/db", clearHandler::handle);

        var userService = new UserService(data);
        var userHandler = new UserHandler(userService);

        javalin.post("/user", userHandler::register);
        javalin.post("/session", userHandler::login);
        javalin.delete("/session", userHandler::logout);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
