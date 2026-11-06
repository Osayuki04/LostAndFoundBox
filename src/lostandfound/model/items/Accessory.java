package lostandfound.model.items;

import java.time.LocalDate;
import lostandfound.model.Item;

/**
 * A lost accessory, such as a scarf, umbrella or pair of glasses.
 * Kept for 30 days.
 *
 * @author Osayuki
 * @version 1.0
 */
public class Accessory extends Item {
    private String accessoryType;
    private String colour;


    /**
     * Creates an accessory.
     *
     * @param id            the unique id of the item
     * @param name          a short name for the item
     * @param description   extra identifying details
     * @param foundBy       who handed it in
     * @param dateFound     the date it was found
     * @param accessoryType what kind of accessory it is, for example "Umbrella"
     * @param colour        its main colour
     */
    public Accessory(int id, String name, String description, String foundBy, LocalDate dateFound,
                    String accessoryType, String colour) {
        super(id, name, description, foundBy, dateFound);
        this.accessoryType = accessoryType;
        this.colour = colour;
    }

    /**
     * Returns what kind of accessory this is.
     *
     * @return the accessory type
     */
    public String getAccessoryType() {
         return accessoryType;
    }

    /**
     * Returns the accessory's colour.
     *
     * @return the colour
     */
    public String getColour() {
         return colour;
    }

    /**
     * Returns the category name.
     *
     * @return {@code "Accessory"}
     */
    @Override
    public String getCategory() {
         return "Accessory";
    }


    /**
     * Returns how long accessories are kept.
     *
     * @return 30 days
     */
    @Override
    public int getHoldingDays() {
        return 30;
    }

    /**
     * Returns the item summary plus the accessory type and colour.
     *
     * @return the accessory as text
     */
    @Override
    public String toString() {
        return super.toString() + " type=" + accessoryType + ", colour=" + colour;
    }

}
