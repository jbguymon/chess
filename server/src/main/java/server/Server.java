package server;

import io.javalin.*;
import dataaccess.MemoryDataAccess;
import service.ClearService;
import handler.ClearHandler;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        var data = new MemoryDataAccess();
        var clearService = new ClearService(data);
        var clearHandler = new ClearHandler(clearService);

        javalin.delete("/db", clearHandler::handle);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
