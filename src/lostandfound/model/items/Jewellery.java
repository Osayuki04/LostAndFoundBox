package lostandfound.model.items;

import java.time.LocalDate;
import lostandfound.model.Item;

/**
 * A lost piece of jewellery, such as a ring or necklace. Kept for 90 days.
 *
 * @author Osayuki
 * @version 1.0
 */
public class Jewellery extends Item {
    private String material;
    private int estimatedValue;


    /**
     * Creates a piece of jewellery.
     *
     * @param id             the unique id of the item
     * @param name           a short name for the item
     * @param description    extra identifying details
     * @param foundBy        who handed it in
     * @param dateFound      the date it was found
     * @param material       what it is made of, for example "Silver"
     * @param estimatedValue its estimated value in whole pounds (GBP)
     * @throws IllegalArgumentException if the estimated value is less than 1
     */
    public Jewellery(int id, String name, String description, String foundBy, LocalDate dateFound,
                    String material, int estimatedValue) {
        super(id, name, description, foundBy, dateFound);

        if (estimatedValue < 1) {
            throw new IllegalArgumentException("Estimated Value must not be less than One ");
        }
        this.material = material;
        this.estimatedValue = estimatedValue;
    }

    /**
     * Returns what the jewellery is made of.
     *
     * @return the material
     */
    public String getMaterial() {
         return material;
    }

    /**
     * Returns the estimated value in pounds.
     *
     * @return the value in GBP
     */
    public int getEstimatedValue() {
         return estimatedValue;
    }

    /**
     * Returns the category name.
     *
     * @return {@code "Jewellery"}
     */
    @Override
    public String getCategory() {
         return "Jewellery";
    }


    /**
     * Returns how long jewellery is kept.
     *
     * @return 90 days
     */
    @Override
    public int getHoldingDays() {
        return 90;
    }

    /**
     * Returns the item summary plus the material and value.
     *
     * @return the jewellery as text
     */
    @Override
    public String toString() {
        return super.toString() + " material=" + material + ", value=GBP " + estimatedValue;
    }

}
