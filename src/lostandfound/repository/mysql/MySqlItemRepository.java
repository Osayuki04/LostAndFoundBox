package lostandfound.repository.mysql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lostandfound.exception.DatabaseException;
import lostandfound.exception.ItemNotFoundException;
import lostandfound.model.Item;
import lostandfound.model.Location;
import lostandfound.model.items.Accessory;
import lostandfound.model.items.Bag;
import lostandfound.model.items.Clothing;
import lostandfound.model.items.Document;
import lostandfound.model.items.Electronics;
import lostandfound.model.items.Jewellery;
import lostandfound.model.items.Keys;
import lostandfound.model.items.Other;
import lostandfound.repository.ItemRepository;

/**
 * An {@link ItemRepository} that saves items in the MySQL {@code items} table.
 * <p>
 * Every kind of item is stored in the same table. The {@code category} column
 * says which subclass a row is, and the columns that only one kind of item
 * uses (such as {@code serial_number}) are left empty for the others.
 *
 * @author Osayuki
 * @version 1.0
 */
public class MySqlItemRepository implements ItemRepository {
    private static final String INSERT_SQL = "INSERT INTO items (id, category, name, description, found_by, date_found, "
            + "claimed, building, room, brand, serial_number, size, colour, document_type, owner_name, material, "
            + "estimated_value, number_of_keys, has_keyring, contents_description, accessory_type, other_type) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ONE_SQL = "SELECT * FROM items WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM items ORDER BY id";
    private static final String UPDATE_SQL = "UPDATE items SET description = ?, claimed = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM items WHERE id = ?";


    /**
     * Creates the repository. No connection is opened until a method is called.
     */
    public MySqlItemRepository() {
    }

    /**
     * {@inheritDoc}
     *
     * @throws DatabaseException if the database cannot be reached
     */
    @Override
    public void add(Item item) {

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {

            statement.setInt(1, item.getId());
            statement.setString(2, item.getCategory());
            statement.setString(3, item.getName());
            statement.setString(4, item.getDescription());
            statement.setString(5, item.getFoundBy());
            statement.setDate(6, Date.valueOf(item.getDateFound()));
            statement.setBoolean(7, item.isClaimed());

            Location location = item.getLocation();
            statement.setString(8, location != null ? location.getBuilding() : null);
            statement.setString(9, location != null ? location.getRoom() : null);

            for (int column = 10; column <= 22; column++) {
                statement.setNull(column, Types.NULL);
            }

            if (item instanceof Electronics electronics) {
                statement.setString(10, electronics.getBrand());
                statement.setString(11, electronics.getSerialNumber());
            } else if (item instanceof Clothing clothing) {
                statement.setString(12, clothing.getSize());
                statement.setString(13, clothing.getColour());
            } else if (item instanceof Document document) {
                statement.setString(14, document.getDocumentType());
                statement.setString(15, document.getOwnerName());
            } else if (item instanceof Jewellery jewellery) {
                statement.setString(16, jewellery.getMaterial());
                statement.setInt(17, jewellery.getEstimatedValue());
            } else if (item instanceof Keys keys) {
                statement.setInt(18, keys.getNumberOfKeys());
                statement.setBoolean(19, keys.hasKeyring());
            } else if (item instanceof Bag bag) {
                statement.setString(10, bag.getBrand());
                statement.setString(13, bag.getColour());
                statement.setString(20, bag.getContentsDescription());
            } else if (item instanceof Accessory accessory) {
                statement.setString(13, accessory.getColour());
                statement.setString(21, accessory.getAccessoryType());
            } else if (item instanceof Other other) {
                statement.setString(22, other.getACategory());
            }

            statement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalArgumentException("An item with id " + item.getId() + " already exists ");
        } catch (SQLException e) {
            throw new DatabaseException("Could not add item " + item.getId(), e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws DatabaseException if the database cannot be reached
     */
    @Override
    public Item findById(int id) {

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ONE_SQL)) {

            statement.setInt(1, id);

            try (ResultSet row = statement.executeQuery()) {
                if (row.next()) {
                    return rowToItem(row);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not find item " + id, e);
        }
    }

    /**
     * {@inheritDoc}
     * The items are sorted by id.
     *
     * @throws DatabaseException if the database cannot be reached
     */
    @Override
    public List<Item> findAll() {
        List<Item> items = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet row = statement.executeQuery()) {

            while (row.next()) {
                items.add(rowToItem(row));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not load items", e);
        }
        return items;
    }

    /**
     * {@inheritDoc}
     * Only the description and the claimed flag are saved, because those are
     * the only parts of an item that can change.
     *
     * @throws DatabaseException if the database cannot be reached
     */
    @Override
    public void update(Item item) {

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setString(1, item.getDescription());
            statement.setBoolean(2, item.isClaimed());
            statement.setInt(3, item.getId());

            int rowsChanged = statement.executeUpdate();
            if (rowsChanged == 0) {
                throw new ItemNotFoundException(item.getId());
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not update item " + item.getId(), e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws DatabaseException if the database cannot be reached
     */
    @Override
    public boolean delete(int id) {

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {

            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Could not delete item " + id, e);
        }
    }

    /**
     * Turns one row of the {@code items} table back into the right {@link Item} subclass.
     *
     * @param row the result set, already moved to the row to read
     * @return the item that row describes
     * @throws SQLException if a column cannot be read
     */
    private Item rowToItem(ResultSet row) throws SQLException {
        int id = row.getInt("id");
        String category = row.getString("category");
        String name = row.getString("name");
        String description = row.getString("description");
        String foundBy = row.getString("found_by");
        LocalDate dateFound = row.getDate("date_found").toLocalDate();

        Item item = switch (category) {
            case "Electronics" -> new Electronics(id, name, description, foundBy, dateFound,
                    row.getString("brand"), row.getString("serial_number"));
            case "Clothing" -> new Clothing(id, name, description, foundBy, dateFound,
                    row.getString("size"), row.getString("colour"));
            case "Document" -> new Document(id, name, description, foundBy, dateFound,
                    row.getString("document_type"), row.getString("owner_name"));
            case "Jewellery" -> new Jewellery(id, name, description, foundBy, dateFound,
                    row.getString("material"), row.getInt("estimated_value"));
            case "Keys" -> new Keys(id, name, description, foundBy, dateFound,
                    row.getInt("number_of_keys"), row.getBoolean("has_keyring"));
            case "Bag" -> new Bag(id, name, description, foundBy, dateFound,
                    row.getString("brand"), row.getString("colour"), row.getString("contents_description"));
            case "Accessory" -> new Accessory(id, name, description, foundBy, dateFound,
                    row.getString("accessory_type"), row.getString("colour"));
            default -> new Other(id, name, description, foundBy, dateFound, row.getString("other_type"));
        };

        String building = row.getString("building");
        if (building != null) {
            item.setLocation(building, row.getString("room"));
        }
        if (row.getBoolean("claimed")) {
            item.markClaimed();
        }
        return item;
    }
}
