package lostandfound.exception;

/**
 * Thrown when an item is looked up by an id that does not exist.
 * <p>
 * It is unchecked (it extends {@link RuntimeException}) because a wrong id is
 * a mistake by the caller, not something every caller must be forced to handle.
 *
 * @author Osayuki
 * @version 1.0
 */
public class ItemNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /** The id that could not be found. */
    private int itemId;


    /**
     * Creates the exception with a message naming the missing id.
     *
     * @param itemId the id that could not be found
     */
    public ItemNotFoundException(int itemId) {
        super("No item with id " + itemId);
        this.itemId = itemId;
    }

    /**
     * Returns the id that could not be found.
     *
     * @return the missing item's id
     */
    public int getItemId() {
        return itemId;
    }
}
