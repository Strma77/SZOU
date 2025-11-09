package org.example.enums;

/**
 * Represents different types of lessons within a course.
 */
public enum LessonType {
    LECTURE("Lecture", "Traditional classroom lecture"),
    LAB("Laboratory", "Hands-on practical session"),
    SEMINAR("Seminar", "Discussion-based session"),
    WORKSHOP("Workshop", "Interactive skill-building session"),
    EXAM("Exam", "Assessment session"),
    QUIZ("Quiz", "Short assessment"),
    PROJECT_REVIEW("Project Review", "Project presentation and feedback");

    private final String displayName;
    private final String description;

    LessonType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    @Override
    public String toString() { return displayName; }
}