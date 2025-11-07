package org.example.entities;

import org.example.exceptions.DuplicateEnrollmentException;
import org.example.exceptions.LimitExceededException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents an academic course with lessons, professor assignment, and ECTS credits.
 * <p>
 * A course contains a fixed-size array of lessons and tracks the assigned professor.
 * Lessons can be added up to the maximum capacity specified during construction.
 */
public class Course {

    private final String name;
    private final Professor professor;
    private final List<Lesson> lessons;
    private final int ECTS;
    private final int maxLessons;

    /**
     * Constructs a course with the specified properties.
     *
     * @param name the course name (not null)
     * @param professor the assigned professor (not null)
     * @param maxLessons the maximum number of lessons allowed
     * @param ECTS the ECTS credit value
     */
    public Course(String name, Professor professor, int maxLessons, int ECTS) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Course name cannot be empty.");

        if (professor == null)
            throw new IllegalArgumentException("Course must have an assigned professor.");

        if (maxLessons <= 0)
            throw new IllegalArgumentException("maxLessons must be positive.");

        if (ECTS <= 0)
            throw new IllegalArgumentException("ECTS must be positive.");

        this.name = name;
        this.professor = professor;
        this.maxLessons = maxLessons;
        this.lessons = new ArrayList<>();
        this.ECTS = ECTS;
    }

    /**
     * Adds a lesson to this course.
     * <p>
     * If maximum lesson capacity is reached, prints a warning but does not throw exception.
     *
     * @param lesson the lesson to add (not null)
     */
    public void addLesson(Lesson lesson){
        Objects.requireNonNull(lesson, "Lesson cannot be null.");

        if (lessons.stream().anyMatch(l -> l.getName().equalsIgnoreCase(lesson.getName())))
            throw new DuplicateEnrollmentException("Lesson " + lesson.getName() + " already exists in course " + name + ".");

        if (lessons.size() >= maxLessons)
            throw new LimitExceededException("Course " + name + " has reached the max number of lessons (" + maxLessons + ").");

        lessons.add(lesson);
    }

    /**
     * Returns the course name.
     *
     * @return course name
     */
    public String getName() { return name; }

    /**
     * Returns the assigned professor.
     *
     * @return professor teaching this course
     */
    public Professor getProfessor() { return professor; }

    /**
     * Returns the array of lessons.
     *
     * @return lesson array (may contain null elements)
     */
    public List<Lesson> getLessons() { return lessons; }

    /**
     * Returns the ECTS credit value.
     *
     * @return ECTS credits
     */
    public int getECTS() { return ECTS; }

    /**
     * Returns formatted string with course details including lessons.
     * <p>
     * Prints decorative separators to console as side effect.
     *
     * @return formatted multi-line string with course information
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        System.out.println("====================");
        sb.append("Course: ").append(name).append("\n");
        sb.append("Profesor: ").append(professor.getFirstName()).append(" ").append(professor.getLastName()).append("\n");
        sb.append("Lekcije:\n");
        for (Lesson l : lessons) {
            sb.append("-").append(l.getName()).append("\n");
        }
        System.out.println("====================\n");
        return sb.toString();
    }
}