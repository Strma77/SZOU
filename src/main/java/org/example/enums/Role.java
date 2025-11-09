package org.example.enums;

/**
 * Represents user roles in the online learning system.
 */
public enum Role {
    STUDENT("Student", "Enrolled in courses"),
    PROFESSOR("Professor", "Teaching courses"),
    ADMIN("Administrator", "System administrator");

    private final String displayName;
    private final String description;

    Role(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    @Override
    public String toString() { return displayName; }
}