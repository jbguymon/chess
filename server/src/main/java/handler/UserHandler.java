package handler;
import org.eclipse.jetty.server.Authentication;
import service.UserService;
import service.RegisterRequest;
import service.RegisterResult;
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
            context.json(result);
        } catch (Exception exception){
            String message = exception.getMessage() != null ? exception.getMessage() : "unknown error";
            int errorStatus = switch (message) {
                case "Username already taken" -> 403;
                case "Bad request" -> 400;
                default -> 500;
            };
            context.json("{\"message\":\"Error: " + message + "\"}");
        }
    }
}
