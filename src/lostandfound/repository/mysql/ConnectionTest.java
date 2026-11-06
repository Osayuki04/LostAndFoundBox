package lostandfound.repository.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A small program for checking that the database connection works.
 * <p>
 * Run it on its own: it connects and prints the id and name of every item.
 *
 * @author Osayuki
 * @version 1.0
 */
public class ConnectionTest {

    /** Not used: run the check with {@link #main(String[])}. */
    private ConnectionTest() {
    }

    /**
     * Connects to the database and prints every item's id and name.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        String sql = "SELECT id, name FROM items";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {

            System.out.println("Connected to MySQL!");
            while (results.next()) {
                int id = results.getInt("id");
                String name = results.getString("name");
                System.out.println(id + " " + name);
            }
        } catch (SQLException e) {
            System.out.println("Could not connect: " + e.getMessage());
        }
    }
}
