package handler;
import service.ClearService;
import io.javalin.http.Context;


public class ClearHandler {
    private final ClearService clear;

    public ClearHandler(ClearService clear){
        this.clear = clear;
    }

    public void handle(Context context){
        try{
            clear.clear();
            context.status(200);
            context.result("{}");
        } catch (Exception exception){
            context.status(500);
            context.result("{\"message\":\"Error: " + exception.getMessage() + "\"}");
        }
    }
}
