package service;
import dataaccess.DataAccess;
import model.UserData;
import model.AuthData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class UserService {
    private final DataAccess data;

    public UserService(DataAccess data){
        this.data = data;
    }

    public RegisterResult register(RegisterRequest request) throws Exception{
        if(request.username() == null || request.password() == null){
            throw new Exception("bad request");
        }
        if(data.getUser(request.username()) != null){
            throw new Exception("already taken");
        }
        data.createUser(new UserData(request.username(), request.password(), request.email()));
        String token = UUID.randomUUID().toString();
        data.createAuth(new AuthData(token, request.username()));
        return new RegisterResult(request.username(), token);
    }

    public LoginResult login(LoginReq request) throws Exception {
        if(request.username() == null || request.password() == null){
            throw new Exception("bad request");
        }
        UserData user = data.getUser(request.username());
        if(user == null || !BCrypt.checkpw(request.password(), user.password())) {
            throw new Exception("unauthorized");
        }
        String token = UUID.randomUUID().toString();
        data.createAuth(new AuthData(token, request.username()));
        return new LoginResult(request.username(), token);
    }

    public void logout(String authToken) throws Exception {
        if (authToken == null) {
            throw new Exception("unauthorized");
        }
        var authorToken = data.getAuth(authToken);
        if(authorToken == null){
            throw new Exception("unauthorized");
        }
        data.deleteAuth(authToken);
    }
}
