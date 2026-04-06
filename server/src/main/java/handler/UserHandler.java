package handler;
import model.LoginReq;
import model.LoginResult;
import model.RegisterRequest;
import model.RegisterResult;
import service.*;
import io.javalin.http.Context;
import com.google.gson.Gson;


public class UserHandler {
    private final UserService service;
    private final Gson gson = new Gson();

    public UserHandler(UserService service){
        this.service = service;
    }

    public void register(Context context){
        try{
            RegisterRequest request = gson.fromJson(context.body(), RegisterRequest.class);
            RegisterResult result = service.register(request);
            context.status(200);
            context.result(gson.toJson(result));
        } catch (Exception exception){
            String message = exception.getMessage() != null ? exception.getMessage() : "unknown error";
            int errorStatus = switch (message) {
                case "already taken" -> 403;
                case "bad request" -> 400;
                default -> 500;
            };
            context.status(errorStatus);
            context.result("{\"message\":\"Error: " + message + "\"}");
        }
    }

    public void login(Context context){
        try{
            LoginReq request = gson.fromJson(context.body(), LoginReq.class);
            LoginResult result = service.login(request);
            context.status(200);
            context.result(gson.toJson(result));
        } catch (Exception exception){
            String message = exception.getMessage();
            int errorStatus = switch (message){
                case "bad request" -> 400;
                case "unauthorized" -> 401;
                default -> 500;
            };
            context.status(errorStatus);
            context.result("{\"message\":\"Error: " + message + "\"}");
        }
    }

    public void logout(Context context){
        try{
            String authToken = context.header("authorization");
            service.logout(authToken);
            context.status(200);
            context.result("{}");
        } catch(Exception exception){
            String message = exception.getMessage();
            int errorStatus = switch(message){
                case "unauthorized" -> 401;
                default -> 500;
            };
            context.status(errorStatus);
            context.result("{\"message\":\"Error: " + message + "\"}");
        }
    }
}
