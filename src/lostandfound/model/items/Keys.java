package lostandfound.model.items;

import java.time.LocalDate;
import lostandfound.model.Item;

/**
 * A lost set of keys. Kept for 30 days.
 *
 * @author Osayuki
 * @version 1.0
 */
public class Keys extends Item {
    private int numberOfKeys;
    private boolean hasKeyring;


    /**
     * Creates a set of keys.
     *
     * @param id           the unique id of the item
     * @param name         a short name for the item
     * @param description  extra identifying details
     * @param foundBy      who handed it in
     * @param dateFound    the date it was found
     * @param numberOfKeys how many keys there are
     * @param hasKeyring   whether the keys are on a keyring
     * @throws IllegalArgumentException if there are fewer than 1 keys
     */
    public Keys(int id, String name, String description, String foundBy, LocalDate dateFound,
                    int numberOfKeys, boolean hasKeyring) {
        super(id, name, description, foundBy, dateFound);

        if(numberOfKeys < 1){
            throw new IllegalArgumentException("Key must not be less than One ");
        }
        this.numberOfKeys = numberOfKeys;
        this.hasKeyring = hasKeyring;
    }

    /**
     * Returns how many keys there are.
     *
     * @return the number of keys
     */
    public int getNumberOfKeys() {
         return numberOfKeys;
    }

    /**
     * Checks whether the keys are on a keyring.
     *
     * @return {@code true} if on a keyring
     */
    public boolean hasKeyring() {
         return hasKeyring;
    }

    /**
     * Returns the category name.
     *
     * @return {@code "Keys"}
     */
    @Override
    public String getCategory() {
         return "Keys";
    }


    /**
     * Returns how long keys are kept.
     *
     * @return 30 days
     */
    @Override
    public int getHoldingDays() {
        return 30;
    }

    /**
     * Returns the item summary plus the number of keys and whether they are on a keyring.
     *
     * @return the keys as text
     */
    @Override
    public String toString() {
        return super.toString() + " keys=" + numberOfKeys + (hasKeyring ? ", on keyring" : ", loose");
    }

}
