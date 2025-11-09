package org.example.enums;

/**
 * Represents the current status of a student's enrollment in a course.
 */
public enum EnrollmentStatus {
    ACTIVE("Active", "Student is currently enrolled and attending"),
    COMPLETED("Completed", "Student has successfully completed the course"),
    DROPPED("Dropped", "Student has withdrawn from the course"),
    FAILED("Failed", "Student did not pass the course"),
    PENDING("Pending", "Enrollment is awaiting approval");

    private final String displayName;
    private final String description;

    EnrollmentStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public boolean isActive() { return this == ACTIVE || this == PENDING; }
    @Override
    public String toString() { return displayName; }
}