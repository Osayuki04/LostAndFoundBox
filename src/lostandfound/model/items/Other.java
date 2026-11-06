package lostandfound.model.items;

import java.time.LocalDate;
import lostandfound.model.Item;

/**
 * Any lost item that does not fit one of the other categories. Kept for 30 days.
 *
 * @author Osayuki
 * @version 1.0
 */
public class Other extends Item {
    private String category;



    /**
     * Creates an item of another kind.
     *
     * @param id          the unique id of the item
     * @param name        a short name for the item
     * @param description extra identifying details
     * @param foundBy     who handed it in
     * @param dateFound   the date it was found
     * @param category    what kind of thing it is, in the finder's own words
     */
    public Other(int id, String name, String description, String foundBy, LocalDate dateFound,
                    String category) {
        super(id, name, description, foundBy, dateFound);
        this.category = category;

    }

    /**
     * Returns what kind of thing this is, as typed in when it was added.
     *
     * @return the finder's own description of the kind of item
     */
    public String getACategory() {
         return category;
    }



    /**
     * Returns the category name.
     *
     * @return {@code "Other"}
     */
    @Override
    public String getCategory() {
         return "Other";
    }


    /**
     * Returns how long other items are kept.
     *
     * @return 30 days
     */
    @Override
    public int getHoldingDays() {
        return 30;
    }

    /**
     * Returns the item summary plus what kind of thing it is.
     *
     * @return the item as text
     */
    @Override
    public String toString() {
        return super.toString() + " type=" + category;
    }

}
