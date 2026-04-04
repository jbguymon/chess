package dataaccess;
import java.sql.*;
import java.util.*;
import model.*;
import com.google.gson.Gson;
import org.mindrot.jbcrypt.BCrypt;

import javax.xml.crypto.Data;

public class MySqlDataAccess implements DataAccess {
    private final Gson gson = new Gson();

    //User functions

    public void createUser(UserData user) throws DataAccessException {
        String sql = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection();
            var state = conn.prepareStatement(sql)){
            String hash = BCrypt.hashpw(user.password(), BCrypt.gensalt());
            state.setString(1, user.username());
            state.setString(2, hash);
            state.setString(3, user.email());
        }
        catch (Exception exception){
            throw new DataAccessException("Error creating user", exception);
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        String sql = "SELECT username, password, email FROM user WHERE username=?";
        try(var conn = DatabaseManager.getConnection();
        var state = conn.prepareStatement(sql)) {
            state.setString(1, username);
            var resultSet = state.executeQuery();
            if(resultSet.next()){
                return new UserData(
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("email"));
            }
            return null;
        }
        catch (Exception exception){
            throw new DataAccessException("Error getting user", exception);
        }
    }

    //Auth token functions

    public void createAuth(AuthData auth) throws DataAccessException {

    }

    public AuthData getAuth(String authToken) throws DataAccessException {

    }

    public void deleteAuth(String token) throws DataAccessException {

    }

    //Game functions

    public int createGame(String name) throws DataAccessException {

    }

    public GameData getGame(int gameID) throws DataAccessException {

    }

    public List<GameData> listGames() throws DataAccessException {

    }

    public void updateGame(GameData game) throws DataAccessException {

    }

    public void clear() throws DataAccessException{

    }
}
