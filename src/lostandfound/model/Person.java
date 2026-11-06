package lostandfound.model;

/**
 * A person who claims an item from the lost and found.
 *
 * @author Osayuki
 * @version 1.0
 */
public class Person {

    private int id;
    private String name;
    private String email;
    private String phone;


    /**
     * Creates a person.
     *
     * @param id    the unique id of the person
     * @param name  the person's full name
     * @param email the person's email address
     * @param phone the person's phone number
     * @throws IllegalArgumentException if the name is blank or the email has no {@code @}
     */
    public Person(int id, String name, String email, String phone) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Person name cannot be Empty ");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email is not valid ");
        }
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    /**
     * Returns the person's id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the person's name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the person's email address.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the person's phone number.
     *
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Changes the person's phone number.
     *
     * @param phone the new phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Returns the person as "name &lt;email&gt;".
     *
     * @return the person as text
     */
    @Override
    public String toString() {
        return name + " <" + email + ">";
    }

}
