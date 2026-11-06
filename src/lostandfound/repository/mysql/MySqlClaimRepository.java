package lostandfound.repository.mysql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lostandfound.exception.DatabaseException;
import lostandfound.model.Claim;
import lostandfound.model.Item;
import lostandfound.model.Person;
import lostandfound.repository.ClaimRepository;
import lostandfound.repository.ItemRepository;

/**
 * A {@link ClaimRepository} that saves claims in the MySQL {@code claims} table,
 * and the people who made them in the {@code persons} table.
 *
 * @author Osayuki
 * @version 1.0
 */
public class MySqlClaimRepository implements ClaimRepository {
    private static final String FIND_PERSON_SQL = "SELECT id FROM persons WHERE id = ?";
    private static final String INSERT_PERSON_SQL = "INSERT INTO persons (id, name, email, phone) VALUES (?, ?, ?, ?)";
    private static final String INSERT_CLAIM_SQL = "INSERT INTO claims (claim_id, item_id, person_id, claim_date) "
            + "VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT claims.claim_id, claims.item_id, claims.claim_date, "
            + "persons.id, persons.name, persons.email, persons.phone "
            + "FROM claims JOIN persons ON claims.person_id = persons.id ORDER BY claims.claim_id";
    private static final String NEXT_ID_SQL = "SELECT COALESCE(MAX(claim_id), 0) + 1 AS next_id FROM claims";

    private ItemRepository itemRepository;


    /**
     * Creates the repository.
     *
     * @param itemRepository used to load the item each claim belongs to
     */
    public MySqlClaimRepository(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * {@inheritDoc}
     * The claimant is saved to the {@code persons} table first if they are not already there.
     *
     * @throws IllegalArgumentException if the email is already used by a different person id
     * @throws DatabaseException        if the database cannot be reached
     */
    @Override
    public void add(Claim claim) {

        try (Connection connection = DatabaseConnection.getConnection()) {
            savePersonIfNew(connection, claim.getClaimant());

            try (PreparedStatement statement = connection.prepareStatement(INSERT_CLAIM_SQL)) {
                statement.setInt(1, claim.getClaimId());
                statement.setInt(2, claim.getItem().getId());
                statement.setInt(3, claim.getClaimant().getId());
                statement.setDate(4, Date.valueOf(claim.getClaimDate()));
                statement.executeUpdate();
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalArgumentException("That email is already used by a different person id ");
        } catch (SQLException e) {
            throw new DatabaseException("Could not save claim " + claim.getClaimId(), e);
        }
    }

    /**
     * Saves a person unless a person with the same id is already stored.
     *
     * @param connection the open connection to use
     * @param person     the person to save
     * @throws SQLException if a query fails
     */
    private void savePersonIfNew(Connection connection, Person person) throws SQLException {

        try (PreparedStatement find = connection.prepareStatement(FIND_PERSON_SQL)) {
            find.setInt(1, person.getId());

            try (ResultSet row = find.executeQuery()) {
                if (row.next()) {
                    return;
                }
            }
        }

        try (PreparedStatement insert = connection.prepareStatement(INSERT_PERSON_SQL)) {
            insert.setInt(1, person.getId());
            insert.setString(2, person.getName());
            insert.setString(3, person.getEmail());
            insert.setString(4, person.getPhone());
            insert.executeUpdate();
        }
    }

    /**
     * {@inheritDoc}
     * The claims are sorted by claim id.
     *
     * @throws DatabaseException if the database cannot be reached
     */
    @Override
    public List<Claim> findAll() {
        List<Claim> claims = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet row = statement.executeQuery()) {

            while (row.next()) {
                Person claimant = new Person(row.getInt("id"), row.getString("name"),
                        row.getString("email"), row.getString("phone"));
                Item item = itemRepository.findById(row.getInt("item_id"));
                LocalDate claimDate = row.getDate("claim_date").toLocalDate();
                claims.add(new Claim(row.getInt("claim_id"), item, claimant, claimDate));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not load claims", e);
        }
        return claims;
    }

    /**
     * {@inheritDoc}
     * This is one more than the highest claim id in the table, or 1 if it is empty.
     *
     * @throws DatabaseException if the database cannot be reached
     */
    @Override
    public int nextClaimId() {

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(NEXT_ID_SQL);
             ResultSet row = statement.executeQuery()) {

            row.next();
            return row.getInt("next_id");
        } catch (SQLException e) {
            throw new DatabaseException("Could not work out the next claim id", e);
        }
    }
}
