package lostandfound.repository.mysql;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Opens connections to the MySQL database on the university server.
 * <p>
 * The database address and login are read from {@code db.properties} in the
 * project folder, so they are never written in the code or uploaded to GitHub
 * ({@code db.properties} is listed in {@code .gitignore}). To set it up, copy
 * {@code db.properties.example} to {@code db.properties} and fill in your details.
 * <p>
 * The MySQL Connector/J driver must be on the classpath (it is in {@code lib/}).
 *
 * @author Osayuki
 * @version 1.0
 */
public class DatabaseConnection {
    private static final String SETTINGS_FILE = "db.properties";


    /** Not used: this class only has static methods. */
    private DatabaseConnection() {
    }

    /**
     * Opens a new connection. The caller must close it, ideally with try-with-resources.
     *
     * @return an open connection to the database
     * @throws SQLException if {@code db.properties} is missing, the server cannot
     *                      be reached or the login is wrong
     */
    public static Connection getConnection() throws SQLException {
        Properties settings = loadSettings();
        return DriverManager.getConnection(settings.getProperty("db.url"),
                settings.getProperty("db.user"), settings.getProperty("db.password"));
    }

    /**
     * Reads the database settings from {@code db.properties}.
     *
     * @return the settings
     * @throws SQLException if the file cannot be read
     */
    private static Properties loadSettings() throws SQLException {
        Properties settings = new Properties();

        try (InputStream file = new FileInputStream(SETTINGS_FILE)) {
            settings.load(file);
        } catch (IOException e) {
            throw new SQLException("Could not read " + SETTINGS_FILE + " in the project folder. "
                    + "Copy db.properties.example to db.properties and fill in your login.", e);
        }
        return settings;
    }
}
