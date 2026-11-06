package lostandfound.model.items;

import java.time.LocalDate;
import lostandfound.model.Item;

/**
 * A lost piece of clothing, such as a coat or hoodie. Kept for 30 days.
 *
 * @author Osayuki
 * @version 1.0
 */
public class Clothing extends Item {
    private String size;
    private String colour;


    /**
     * Creates a piece of clothing.
     *
     * @param id          the unique id of the item
     * @param name        a short name for the item
     * @param description extra identifying details
     * @param foundBy     who handed it in
     * @param dateFound   the date it was found
     * @param size        the clothing size, for example "M"
     * @param colour      its main colour
     */
    public Clothing(int id, String name, String description, String foundBy, LocalDate dateFound,
                    String size, String colour) {
        super(id, name, description, foundBy, dateFound);
        this.size = size;
        this.colour = colour;
    }

    /**
     * Returns the clothing size.
     *
     * @return the size
     */
    public String getSize() {
         return size;
    }

    /**
     * Returns the clothing colour.
     *
     * @return the colour
     */
    public String getColour() {
         return colour;
    }

    /**
     * Returns the category name.
     *
     * @return {@code "Clothing"}
     */
    @Override
    public String getCategory() {
         return "Clothing";
    }


    /**
     * Returns how long clothing is kept.
     *
     * @return 30 days
     */
    @Override
    public int getHoldingDays() {
        return 30;
    }
}
