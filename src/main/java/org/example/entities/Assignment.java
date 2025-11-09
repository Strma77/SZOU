package org.example.entities;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents an assignment within a course with submission and grading capabilities.
 */
public class Assignment {

    private final String title;
    private final String description;
    private final Course course;
    private final int maxPoints;
    private final LocalDateTime dueDate;
    private final LocalDateTime createdDate;

    public Assignment(String title, String description, Course course, int maxPoints, LocalDateTime dueDate) {
        if (title == null || title.isBlank())
            throw new IllegalArgumentException("Assignment title cannot be empty");

        if (maxPoints <= 0)
            throw new IllegalArgumentException("Max points must be positive");

        this.title = title;
        this.description = description;
        this.course = Objects.requireNonNull(course, "Course cannot be null");
        this.maxPoints = maxPoints;
        this.dueDate = Objects.requireNonNull(dueDate, "Due date cannot be null");
        this.createdDate = LocalDateTime.now();
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Course getCourse() { return course; }
    public int getMaxPoints() { return maxPoints; }
    public LocalDateTime getDueDate() { return dueDate; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public boolean isOverdue() { return LocalDateTime.now().isAfter(dueDate); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Assignment that)) return false;
        return title.equals(that.title) && course.equals(that.course);
    }

    @Override
    public int hashCode() { return Objects.hash(title, course); }

    @Override
    public String toString() {
        return String.format("Assignment: %s [%s] - Max Points: %d, Due: %s %s",
                title, course.getName(), maxPoints, dueDate.toLocalDate(),
                isOverdue() ? "(OVERDUE)" : ""
        );
    }
}