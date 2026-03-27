package handler;
import io.javalin.http.Context;
import com.google.gson.Gson;
import service.*;

public class GameHandler {
    private final GameService service;
    private final Gson gson = new Gson();

    public GameHandler(GameService service){
        this.service = service;
    }

    public void createGame(Context context){
        try{
            String authToken = context.header("authorization");
            CreateGameRequest request = gson.fromJson(context.body(), CreateGameRequest.class);
            CreateGameResponse response = service.createGame(authToken, request);
            context.status(200);
            context.result(gson.toJson(response));
        } catch (Exception exception){
            exceptionHandler(context, exception);
        }
    }

    public void listGames(Context context){
        try{
            String authToken = context.header("authorization");
            ListGamesResponse response = service.listGames(authToken);
            context.status(200);
            context.result(gson.toJson(response));
        } catch (Exception exception){
            exceptionHandler(context, exception);
        }
    }

    public void joinGame(Context context){
        try {
            String authToken = context.header("authorization");
            JoinGameRequest request = gson.fromJson(context.body(), JoinGameRequest.class);
            service.joinGame(authToken, request);
            context.status(200);
            context.result("{}");
        } catch (Exception exception){
            exceptionHandler(context, exception);
        }
    }

    private void exceptionHandler(Context context, Exception exception){
        String message = exception.getMessage();
        int errorStatus = switch (message){
            case "bad request" -> 400;
            case "unauthorized" -> 401;
            case "already taken" -> 403;
            default -> 500;
        };
        context.status(errorStatus);
        context.result("{\"message\":\"Error: " + message + "\"}");
    }
}
