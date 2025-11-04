package org.example.entities;

/**
 * Represents an abstract person with basic identifying information.
 * <p>
 * This base class provides immutable first name, last name, and ID fields
 * for all person entities in the system.
 */
public abstract class Person {

    private final String firstName;
    private final String lastName;
    private final int id;

    /**
     * Constructs a person with the specified identifying information.
     *
     * @param firstName the person's first name (not null)
     * @param lastName the person's last name (not null)
     * @param id the person's unique identifier
     */
    public Person(String firstName, String lastName, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
    }

    /**
     * Returns the person's first name.
     *
     * @return first name
     */
    public String getFirstName() { return firstName; }

    /**
     * Returns the person's last name.
     *
     * @return last name
     */
    public String getLastName() { return lastName; }

    /**
     * Returns string representation with full name and ID.
     *
     * @return formatted string: "[FirstName] [LastName] ID: [id]"
     */
    @Override
    public String toString(){
        return firstName + " " + lastName + " ID: " + id;
    }
}