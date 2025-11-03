package org.example.entities;

/**
 * Represents a student's enrollment in a specific course for a given semester.
 * <p>
 * This immutable record links a {@link Student} to a {@link Course} with semester information.
 *
 * @param student the enrolled student (not null)
 * @param course the course being taken (not null)
 * @param semester the semester number (typically 1-6)
 */
public record Enrollment(Student student, Course course, Integer semester) { }