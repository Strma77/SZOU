package org.example.enums;

/**
 * Represents academic semesters in a standard 3-year program.
 */
public enum Semester {
    FIRST(1, "First Semester"),
    SECOND(2, "Second Semester"),
    THIRD(3, "Third Semester"),
    FOURTH(4, "Fourth Semester"),
    FIFTH(5, "Fifth Semester"),
    SIXTH(6, "Sixth Semester");

    private final int number;
    private final String displayName;

    Semester(int number, String displayName) {
        this.number = number;
        this.displayName = displayName;
    }

    public int getNumber() { return number;}
    public String getDisplayName() { return displayName; }

    /**
     * Converts an integer (1-6) to the corresponding Semester enum.
     */
    public static Semester fromNumber(int num) {
        for (Semester s : values()) {
            if (s.number == num) return s;
        }
        throw new IllegalArgumentException("Invalid semester number: " + num + ". Must be 1-6.");
    }

    @Override
    public String toString() { return displayName; }
}