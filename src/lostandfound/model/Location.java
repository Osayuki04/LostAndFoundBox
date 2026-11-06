package lostandfound.model;

/**
 * The place where an item was found: a building and a room.
 *
 * @author Osayuki
 * @version 1.0
 */
public class Location {
    private String building;
    private String room;

    /**
     * Creates a location.
     *
     * @param building the building name
     * @param room     the room name or number
     * @throws IllegalArgumentException if the building is null or blank
     */
    public Location(String building, String room){

        if(building == null || building.isBlank()){
            throw new IllegalArgumentException("Building cannot be null");
        }
        this.building = building;
        this.room =  room;
    }

    /**
     * Returns the building name.
     *
     * @return the building
     */
    public String getBuilding(){
        return building;
    }

    /**
     * Returns the room.
     *
     * @return the room
     */
    public String getRoom(){
        return room;
    }


    /**
     * Returns the location as "building, room".
     *
     * @return the location as text
     */
    @Override
    public String toString() {
        return building + ", "  + room ;
    }
}
