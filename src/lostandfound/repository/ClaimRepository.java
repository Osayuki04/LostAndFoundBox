package lostandfound.repository;

import java.util.List;
import lostandfound.model.Claim;

/**
 * Stores and loads {@link Claim} records.
 * <p>
 * Like {@link ItemRepository}, this only says what a claim store must do;
 * {@link InMemoryClaimRepository} and
 * {@link lostandfound.repository.mysql.MySqlClaimRepository} decide how.
 *
 * @author Osayuki
 * @version 1.0
 */
public interface ClaimRepository {

    /**
     * Saves a new claim.
     *
     * @param claim the claim to save
     */
    void add(Claim claim);

    /**
     * Returns every saved claim.
     *
     * @return a new list containing all claims
     */
    List<Claim> findAll();

    /**
     * Works out the id to give the next new claim.
     *
     * @return an id that no saved claim is using yet
     */
    int nextClaimId();
}
