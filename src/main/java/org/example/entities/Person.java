package org.example.entities;

public abstract class Person {
    private final String firstName;
    private final String lastName;
    private final int id;

    public Person(String firstName, String lastName, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
    }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getId() { return id; }

    @Override
    public String toString(){
        return firstName + " " + lastName + " ID: " + id;
    }
}
