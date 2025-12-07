package org.example.entities;

import org.example.enums.CourseLevel;
import org.example.exceptions.DuplicateEnrollmentException;
import org.example.exceptions.LimitExceededException;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Represents an academic course with lessons, professor, ECTS credits, and difficulty level.
 */
public class Course implements Serializable {
    @Serial
    private static final long serialVersionUID = 5L;

    private final String name;
    private final Professor professor;
    private final List<Lesson> lessons;
    private final int ECTS;
    private final int maxLessons;
    private final CourseLevel level;
    private final List<Student> enrolledStudents;

    public Course(String name, Professor professor, int maxLessons, int ECTS, CourseLevel level) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Course name cannot be empty.");
        if (professor == null) throw new IllegalArgumentException("Course must have an assigned professor.");
        if (maxLessons <= 0) throw new IllegalArgumentException("maxLessons must be positive.");
        if (ECTS <= 0) throw new IllegalArgumentException("ECTS must be positive.");

        this.name = name;
        this.professor = professor;
        this.maxLessons = maxLessons;
        this.lessons = new ArrayList<>();
        this.ECTS = ECTS;
        this.level = Objects.requireNonNull(level, "Course level cannot be null");
        this.enrolledStudents = new ArrayList<>();
    }

    public void addLesson(Lesson lesson){
        Objects.requireNonNull(lesson, "Lesson cannot be null.");

        if (lessons.stream().anyMatch(l -> l.getName().equalsIgnoreCase(lesson.getName())))
            throw new DuplicateEnrollmentException("Lesson " + lesson.getName() +
                    " already exists in course " + name + ".");

        if (lessons.size() >= maxLessons)
            throw new LimitExceededException("Course " + name +
                    " has reached the max number of lessons (" + maxLessons + ").");

        lessons.add(lesson);
    }

    public void enrollStudent(Student student) {
        Objects.requireNonNull(student, "Student cannot be null");
        if (!enrolledStudents.contains(student)) {
            enrolledStudents.add(student);
        }
    }

    public String getName() { return name; }
    public Professor getProfessor() { return professor; }
    public List<Lesson> getLessons() { return Collections.unmodifiableList(lessons); }
    public int getECTS() { return ECTS; }
    public CourseLevel getLevel() { return level; }
    public List<Student> getEnrolledStudents() {
        return Collections.unmodifiableList(enrolledStudents);
    }
    public int getEnrollmentCount() { return enrolledStudents.size(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course course)) return false;
        return name.equals(course.name) && professor.equals(course.professor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, professor);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        System.out.println("====================");
        sb.append("Course: ").append(name).append(" [").append(level).append("]\n");
        sb.append("Professor: ").append(professor.getFirstName()).append(" ")
                .append(professor.getLastName()).append("\n");
        sb.append("ECTS: ").append(ECTS).append("\n");
        sb.append("Enrolled Students: ").append(enrolledStudents.size()).append("\n");
        sb.append("Lessons:\n");
        for (Lesson l : lessons) {
            sb.append("  - ").append(l.getName()).append(" (").append(l.getType()).append(")\n");
        }
        System.out.println("====================\n");
        return sb.toString();
    }
}