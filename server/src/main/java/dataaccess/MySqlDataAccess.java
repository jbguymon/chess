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

    }

    public UserData getUser(String username) throws DataAccessException {

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
