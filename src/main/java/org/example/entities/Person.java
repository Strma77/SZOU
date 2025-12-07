package org.example.entities;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents an abstract person with basic identifying information.
 * <p>
 * This base class provides immutable first name, last name, and ID fields
 * for all person entities in the system.
 */
public abstract class Person implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public int getID() { return id; }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Person person)) return false;
        return id == person.id;
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

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