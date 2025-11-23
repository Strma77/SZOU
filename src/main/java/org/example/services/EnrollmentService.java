package org.example.services;

import org.example.entities.*;
import org.example.enums.EnrollmentStatus;
import org.example.enums.Semester;
import org.example.exceptions.DuplicateEnrollmentException;
import org.example.exceptions.LimitExceededException;
import org.example.exceptions.TooManyAttemptsException;
import org.example.utils.InputHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for managing enrollments with stream operations.
 */
public class EnrollmentService {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentService.class);

    public static Enrollment enrollStudent(Student student, Course course, Semester semester)
            throws LimitExceededException {
        student.enrollCourses(course.getName());
        course.enrollStudent(student);
        logger.info("Student {} {} enrolled in course {}",
                student.getFirstName(), student.getLastName(), course.getName());
        return new Enrollment(student, course, semester);
    }

    public static List<Enrollment> enrollStudents(Set<Student> students, List<Course> courses)
            throws TooManyAttemptsException {
        List<Enrollment> enrollments = new ArrayList<>();

        for (Student student : students) {
            System.out.println("\n--- Enroll courses for: " + student.getFirstName() + " " +
                    student.getLastName() + " ---");

            for (int i = 0; i < student.getMaxCourses(); i++) {
                System.out.println("\nCourse #" + (i + 1) + " for " + student.getFirstName());

                Course selectedCourse = selectCourse(courses);
                Semester semester = InputHelper.readSemester("Choose semester (1-6): ");

                try {
                    Enrollment enrollment = enrollStudent(student, selectedCourse, semester);
                    enrollments.add(enrollment);
                } catch (LimitExceededException | DuplicateEnrollmentException e) {
                    System.out.println("⚠️  " + e.getMessage());
                    logger.warn("Enrollment issue for {}: {}",
                            student.getFirstName(), e.getMessage());
                    if (e instanceof  LimitExceededException) break;

                    i--;
                }
            }
        }

        logger.info("Enrollment creation finished. Total enrollments: {}", enrollments.size());
        return enrollments;
    }

    /**
     * Sorts enrollments by student name.
     * Demonstrates Comparator and lambda.
     */
    public static List<Enrollment> sortEnrollmentsByStudentName(
            Collection<Enrollment> enrollments) {
        return enrollments.stream()
                .sorted(Comparator.comparing(e -> e.student().getFirstName()))
                .toList();
    }

    /**
     * Sorts enrollments by grade (descending - best grades first).
     */
    public static List<Enrollment> sortEnrollmentsByGrade(Collection<Enrollment> enrollments) {
        return enrollments.stream()
                .sorted(Comparator.comparingDouble((Enrollment e) ->
                        e.grade().getGradePoint()).reversed())
                .toList();
    }

    /**
     * Groups enrollments by semester.
     * Demonstrates groupingBy collector.
     */
    public static Map<Semester, List<Enrollment>> groupEnrollmentsBySemester(
            Collection<Enrollment> enrollments) {
        return enrollments.stream()
                .collect(Collectors.groupingBy(Enrollment::semester));
    }

    /**
     * Groups enrollments by status (ACTIVE, COMPLETED, etc.).
     */
    public static Map<EnrollmentStatus, List<Enrollment>> groupEnrollmentsByStatus(
            Collection<Enrollment> enrollments) {
        return enrollments.stream()
                .collect(Collectors.groupingBy(Enrollment::status));
    }

    /**
     * Partitions enrollments by passing grade.
     * Demonstrates partitioningBy collector.
     */
    public static Map<Boolean, List<Enrollment>> partitionEnrollmentsByPassing(
            Collection<Enrollment> enrollments) {
        return enrollments.stream()
                .collect(Collectors.partitioningBy(Enrollment::isPassed));
    }

    /**
     * Finds top enrollment by grade.
     * Demonstrates Optional return type.
     */
    public static Optional<Enrollment> findTopEnrollment(Collection<Enrollment> enrollments) {
        return enrollments.stream()
                .filter(Enrollment::isPassed)
                .max(Comparator.comparingDouble(e -> e.grade().getGradePoint()));
    }

    /**
     * Calculates completion rate percentage.
     * Demonstrates stream operations with calculations.
     */
    public static double calculateCompletionRate(Collection<Enrollment> enrollments) {
        long total = enrollments.size();
        if (total == 0) return 0.0;

        long completed = enrollments.stream()
                .filter(e -> e.status() == EnrollmentStatus.COMPLETED)
                .count();

        return (completed * 100.0) / total;
    }

    private static Course selectCourse(List<Course> courses) throws TooManyAttemptsException {
        System.out.println("Available courses:");
        for (int i = 0; i < courses.size(); i++) {
            Course c = courses.get(i);
            System.out.println((i + 1) + ": " + c.getName() + " (ECTS: " + c.getECTS() +
                    ", Level: " + c.getLevel() + ")");
        }

        int choice = InputHelper.readChoice("Choose course ID: ", courses.size());
        return courses.get(choice);
    }
}