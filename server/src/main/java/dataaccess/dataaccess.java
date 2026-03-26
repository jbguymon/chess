package dataaccess;

import model.*;
import java.util.List;

public class dataaccess {
    public interface UserDAO {
        void clear() throws DataAccessException;
        void createUser(UserData user) throws DataAccessException;
    }
}
