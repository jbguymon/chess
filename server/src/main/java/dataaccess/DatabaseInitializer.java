package dataaccess;


import java.sql.SQLException;

public class DatabaseInitializer {
    public static void initialize() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var con = DatabaseManager.getConnection();
            var state = con.createStatement()) {
                state.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS user (
                        username VARCHAR(50) PRIMARY KEY,
                        password VARCHAR(255) NOT NULL,
                        email VARCHAR(100) NOT NULL
                    )
                """);
                state.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS game (
                        gameID INT AUTO_INCREMENT PRIMARY KEY,
                        whiteUsername VARCHAR(50),
                        blackUsername VARCHAR(50),
                        gameName VARCHAR(100),
                        gameState TEXT
                    )
                """);
                state.executeUpdate("""
                CREATE TABLE IF NOT EXISTS auth (
                    authToken VARCHAR(255) PRIMARY KEY,
                    username VARCHAR(50) NOT NULL,
                    FOREIGN KEY (username) REFERENCES user(username) ON DELETE CASCADE
                )
            """);
            }
        catch (SQLException exception){
            throw new DataAccessException("Failed to create tables", exception);
        }
    }
}
