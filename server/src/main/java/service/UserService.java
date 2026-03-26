package service;
import dataaccess.dataaccess;
import model.UserData;
import model.AuthData;
import java.util.UUID;

public class UserService {
    private final dataaccess data;

    public UserService(dataaccess data){
        this.data = data;
    }

    public RegisterResult register(RegisterRequest request) throws Exception{
        if(request.username() == null || request.password() == null){
            throw new Exception("request is bad, either username or password is null");
        }
        if(data.getUser(request.username()) != null){
            throw new Exception("Username already taken");
        }
        data.createUser(new UserData(request.username(), request.password(), request.email()));
        String token = UUID.randomUUID().toString();
        data.createAuth(new AuthData(token, request.username()));
        return new RegisterResult(request.username(), token);
    }
}
