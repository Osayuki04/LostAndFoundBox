package lostandfound.model.items;

import java.time.LocalDate;
import lostandfound.model.Item;

/**
 * A lost bag, such as a backpack or handbag. Kept for 60 days.
 *
 * @author Osayuki
 * @version 1.0
 */
public class Bag extends Item {
    private String brand;
    private String colour;
    private String contentsDescription;


    /**
     * Creates a bag.
     *
     * @param id                  the unique id of the item
     * @param name                a short name for the item
     * @param description         extra identifying details
     * @param foundBy             who handed it in
     * @param dateFound           the date it was found
     * @param brand               the bag's brand
     * @param colour              its main colour
     * @param contentsDescription what was inside the bag
     */
    public Bag(int id, String name, String description, String foundBy, LocalDate dateFound,
                    String brand, String colour, String contentsDescription) {
        super(id, name, description, foundBy, dateFound);
        this.brand = brand;
        this.colour = colour;
        this.contentsDescription = contentsDescription;
    }

    /**
     * Returns the bag's brand.
     *
     * @return the brand
     */
    public String getBrand() {
         return brand;
    }

    /**
     * Returns the bag's colour.
     *
     * @return the colour
     */
    public String getColour() {
         return colour;
    }

    /**
     * Returns a description of what was inside the bag.
     *
     * @return the contents
     */
    public String getContentsDescription() {
         return contentsDescription;
    }

    /**
     * Returns the category name.
     *
     * @return {@code "Bag"}
     */
    @Override
    public String getCategory() {
         return "Bag";
    }


    /**
     * Returns how long bags are kept.
     *
     * @return 60 days
     */
    @Override
    public int getHoldingDays() {
        return 60;
    }

}
