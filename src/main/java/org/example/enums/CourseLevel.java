package org.example.enums;

/**
 * Represents the difficulty level of a course.
 */
public enum CourseLevel {
    BEGINNER("Beginner", 1),
    INTERMEDIATE("Intermediate", 2),
    ADVANCED("Advanced", 3),
    EXPERT("Expert", 4);

    private final String displayName;
    private final int difficulty;

    CourseLevel(String displayName, int difficulty) {
        this.displayName = displayName;
        this.difficulty = difficulty;
    }

    public String getDisplayName() { return displayName; }
    public int getDifficulty() { return difficulty; }
    @Override
    public String toString() { return displayName; }
}