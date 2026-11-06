package lostandfound.repository;

import java.util.ArrayList;
import java.util.List;
import lostandfound.model.Claim;

/**
 * A {@link ClaimRepository} that keeps claims in a list.
 * <p>
 * Nothing is saved when the program stops, so this is mainly used by the tests.
 *
 * @author Osayuki
 * @version 1.0
 */
public class InMemoryClaimRepository implements ClaimRepository {
    private List<Claim> claims;


    /**
     * Creates an empty store.
     */
    public InMemoryClaimRepository() {
        this.claims = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Claim claim) {
        claims.add(claim);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Claim> findAll() {
        return new ArrayList<>(claims);
    }

    /**
     * {@inheritDoc}
     * Claim ids start at 1 and go up by one for each claim.
     */
    @Override
    public int nextClaimId() {
        return claims.size() + 1;
    }
}
