package lostandfound.model.items;

import java.time.LocalDate;
import lostandfound.model.Item;

/**
 * A lost electronic device, such as a phone or laptop. Kept for 90 days.
 *
 * @author Osayuki
 * @version 1.0
 */
public class Electronics extends Item {

    private String brand;
    private String serialNumber;


    /**
     * Creates an electronic device.
     *
     * @param id           the unique id of the item
     * @param name         a short name for the item
     * @param description  extra identifying details
     * @param foundBy      who handed it in
     * @param dateFound    the date it was found
     * @param brand        the device's brand
     * @param serialNumber the device's serial number
     */
    public Electronics(int id, String name, String description, String foundBy, LocalDate dateFound, String brand, String serialNumber) {
        super(id, name, description, foundBy, dateFound);
        this.brand = brand;
        this.serialNumber = serialNumber;
    }

    /**
     * Returns the device's brand.
     *
     * @return the brand
     */
    public String getBrand() { return brand; }


    /**
     * Returns the device's serial number.
     *
     * @return the serial number
     */
    public String getSerialNumber() { return serialNumber; }

    /**
     * Returns the category name.
     *
     * @return {@code "Electronics"}
     */
    @Override
    public String getCategory() { return "Electronics"; }


    /**
     * Returns how long electronics are kept.
     *
     * @return 90 days
     */
    @Override
    public int getHoldingDays() { return 90; }

    /**
     * Returns the item summary plus the brand and serial number.
     *
     * @return the device as text
     */
    @Override
    public String toString() {
        return super.toString() + " brand=" + brand + ", serial=" + serialNumber;
    }


}
