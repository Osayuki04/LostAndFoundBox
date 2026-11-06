package lostandfound.model;

import java.time.LocalDate;

/**
 * A record that a person collected an item, and when.
 *
 * @author Osayuki
 * @version 1.0
 */
public class Claim {
    private int claimId;
    private Item item;
    private Person claimant;
    private LocalDate claimDate;


    /**
     * Creates a claim.
     *
     * @param claimId   the unique id of the claim
     * @param item      the item that was collected
     * @param claimant  the person who collected it
     * @param claimDate the date it was collected
     * @throws IllegalArgumentException if the item or the claimant is null
     */
    public Claim(int claimId, Item item, Person claimant, LocalDate claimDate) {

        if (item == null || claimant == null) {
            throw new IllegalArgumentException("A claim needs an item and a person ");
        }
        this.claimId = claimId;
        this.item = item;
        this.claimant = claimant;
        this.claimDate = claimDate;
    }

    /**
     * Returns the claim's id.
     *
     * @return the claim id
     */
    public int getClaimId() {
        return claimId;
    }

    /**
     * Returns the item that was claimed.
     *
     * @return the item
     */
    public Item getItem() {
        return item;
    }

    /**
     * Returns the person who claimed the item.
     *
     * @return the claimant
     */
    public Person getClaimant() {
        return claimant;
    }

    /**
     * Returns the date the item was claimed.
     *
     * @return the claim date
     */
    public LocalDate getClaimDate() {
        return claimDate;
    }

    /**
     * Returns a one-line summary of who collected what and when.
     *
     * @return the claim as text
     */
    @Override
    public String toString() {
        return "Claim #" + claimId + ": " + claimant.getName() + " collected " + item.getName() + " on " + claimDate;
    }
}
