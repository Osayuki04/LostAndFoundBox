package lostandfound.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * A lost item that has been handed in to the lost and found.
 * <p>
 * This is the base class for every kind of item. Each subclass in
 * {@code lostandfound.model.items} says what category it is and how many days
 * it is kept before it counts as overdue.
 *
 * @author Osayuki
 * @version 1.0
 */
public abstract class Item {

    private int id;
    private String name;
    private String description;
    private String foundBy;
    private LocalDate dateFound;
    private boolean claimed;
    private Location location;


    /**
     * Creates a new, unclaimed item with no location set.
     *
     * @param id          the unique id of the item
     * @param name        a short name, for example "Phone"
     * @param description extra details that help identify the item
     * @param foundBy     the name of the person who handed it in
     * @param datefound   the date the item was found
     * @throws IllegalArgumentException if the name is null or blank
     */
    public Item (int id, String name, String description, String foundBy, LocalDate datefound ){

        if(name == null || name.isBlank()){
            throw new IllegalArgumentException( "Item name cannot be Empty ");
        }
        this.id = id;
        this.name = name;
        this.description = description;
        this.dateFound = datefound;
        this.claimed = false;
        this.foundBy = foundBy;

    }

    /**
     * Returns the category name of this kind of item.
     *
     * @return the category, for example "Electronics"
     */
    public abstract String getCategory();

    /**
     * Returns how many days this kind of item is kept before it is overdue.
     *
     * @return the number of holding days
     */
    public abstract int getHoldingDays();

    /**
     * Checks whether the item has been waiting longer than its holding days.
     * A claimed item is never overdue.
     *
     * @param today the date to check against
     * @return {@code true} if the item is unclaimed and past its holding days
     */
    public boolean isOverdue(LocalDate today) {
        return !claimed && today.isAfter(dateFound.plusDays(getHoldingDays()));
    }

    /**
     * Returns how many days have passed since the item was found.
     *
     * @param today the date to count up to
     * @return the number of days between the found date and {@code today}
     */
    public long getDaysWaiting(LocalDate today) {
        return ChronoUnit.DAYS.between(dateFound, today);
    }


    /**
     * Returns the item's id.
     *
     * @return the id
     */
    public int getId(){
        return id ;
    }

    /**
     * Returns the item's name.
     *
     * @return the name
     */
    public String getName(){
        return name;
    }

    /**
     * Returns the item's description.
     *
     * @return the description
     */
    public String getDescription(){
        return description ;
    }

    /**
     * Returns the name of the person who found the item.
     *
     * @return who found it
     */
    public String getFoundBy(){
        return foundBy;
    }

    /**
     * Returns the date the item was found.
     *
     * @return the found date
     */
    public LocalDate getDateFound(){
        return dateFound;
    }

    /**
     * Returns where the item was found.
     *
     * @return the location, or {@code null} if it has not been set
     */
    public  Location getLocation(){
        return location;
    }

    /**
     * Checks whether the item has been claimed by its owner.
     *
     * @return {@code true} if claimed
     */
    public boolean isClaimed(){
        return claimed;
    }


    /**
     * Changes the item's description.
     *
     * @param description the new description
     */
    public void setDescription(String description){
        this.description = description;
    }

    /**
     * Changes who the item was found by.
     *
     * @param foundBy the new finder's name
     */
    public void setFoundBy(String foundBy){
        this.foundBy = foundBy;
    }

    /**
     * Sets where the item was found. This can only be done once.
     *
     * @param building the building where it was found
     * @param room     the room where it was found
     * @throws IllegalStateException if the location has already been set
     */
    public void setLocation(String building, String room) {

        if (location != null) {
            throw new IllegalStateException("Location for item " + id + " has already been set");
        }
        this.location = new Location(building, room);
    }

    /**
     * Marks the item as claimed by its owner.
     *
     * @throws IllegalStateException if the item has already been claimed
     */
    public void markClaimed ( ) {

        if(claimed){
            throw new IllegalStateException("Item " + id + " has already been claimed");
        }
        this.claimed = true;
    }

    /**
     * Returns a one-line summary of the item for printing.
     *
     * @return the item's id, category, details, location and claim status
     */
    @Override
    public String toString() {
        return "#" + id + " [" + getCategory() + "] " + name + " (" + description + "), found by " + foundBy
                + " on " + dateFound + (location != null ? " at " + location : "")
                + (claimed ? " [CLAIMED]" : " [WAITING]");
    }
}
