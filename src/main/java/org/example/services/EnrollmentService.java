package org.example.services;

import org.example.entities.*;
import org.example.enums.EnrollmentStatus;
import org.example.enums.GradeType;
import org.example.entities.Enrollment;
import org.example.enums.Semester;
import org.example.exceptions.LimitExceededException;
import org.example.exceptions.TooManyAttemptsException;
import org.example.utils.InputHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
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

    public static List<Enrollment> enrollStudents(Set<Student> students, List<Course> courses) throws TooManyAttemptsException {
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
                } catch (LimitExceededException e) {
                    System.out.println("⚠️  " + e.getMessage());
                    logger.warn("Student {} tried to enroll in too many courses: {}",
                            student.getFirstName(), e.getMessage());
                    break;
                }
            }
        }

        logger.info("Enrollment creation finished. Total enrollments: {}", enrollments.size());
        return enrollments;
    }


    /**
     * Sorts enrollments by student name.
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
                .sorted(Comparator.comparingDouble((Enrollment e) -> e.grade().getGradePoint()).reversed())
                .toList();
    }

    /**
     * Groups enrollments by semester.
     */
    public static Map<Semester, List<Enrollment>> groupEnrollmentsBySemester(
            Collection<Enrollment> enrollments) {
        return enrollments.stream()
                .collect(Collectors.groupingBy(Enrollment::semester));
    }

    /**
     * Groups enrollments by status (ACTIVE, COMPLETED, DROPPED, etc.).
     */
    public static Map<EnrollmentStatus, List<Enrollment>> groupEnrollmentsByStatus(
            Collection<Enrollment> enrollments) {
        return enrollments.stream()
                .collect(Collectors.groupingBy(Enrollment::status));
    }

    /**
     * Partitions enrollments by completion status: completed vs active/pending.
     */
    public static Map<Boolean, List<Enrollment>> partitionEnrollmentsByCompletion(
            Collection<Enrollment> enrollments) {
        return enrollments.stream()
                .collect(Collectors.partitioningBy(e ->
                        e.status() == EnrollmentStatus.COMPLETED));
    }

    /**
     * Groups enrollments by course.
     */
    public static Map<String, List<Enrollment>> groupEnrollmentsByCourse(
            Collection<Enrollment> enrollments) {
        return enrollments.stream()
                .collect(Collectors.groupingBy(e -> e.course().getName()));
    }

    /**
     * Partitions enrollments by passing grade.
     */
    public static Map<Boolean, List<Enrollment>> partitionEnrollmentsByPassing(
            Collection<Enrollment> enrollments) {
        return enrollments.stream()
                .collect(Collectors.partitioningBy(Enrollment::isPassed));
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

    public static <T> List<T> filterEnrollments(Collection<? extends T> enrollments,
                                                Predicate<? super T> predicate) {
        return enrollments.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public static List<Enrollment> filterEnrollmentsByStudent(
            Collection<Enrollment> enrollments,
            Student student) {
        return filterEnrollments(enrollments, e -> e.student().equals(student));
    }

    public static List<Enrollment> filterEnrollmentsByCourse(
            Collection<Enrollment> enrollments,
            Course course) {
        return filterEnrollments(enrollments, e -> e.course().equals(course));
    }

    public static List<Enrollment> filterEnrollmentsBySemester(
            Collection<Enrollment> enrollments,
            Semester semester) {
        return filterEnrollments(enrollments, e -> e.semester() == semester);
    }

    public static List<Enrollment> filterActiveEnrollments(Collection<Enrollment> enrollments) {
        return filterEnrollments(enrollments, Enrollment::isActive);
    }

    public static List<Enrollment> filterPassedEnrollments(Collection<Enrollment> enrollments) {
        return filterEnrollments(enrollments, Enrollment::isPassed);
    }

    public static List<String> getEnrolledCourseNames(Collection<Enrollment> enrollments) {
        return enrollments.stream()
                .map(e -> e.course().getName())
                .collect(Collectors.toList());
    }

    public static Optional<Enrollment> findTopEnrollment(Collection<Enrollment> enrollments) {
        return enrollments.stream()
                .filter(Enrollment::isPassed)
                .max(Comparator.comparingDouble(e -> e.grade().getGradePoint()));
    }

    public static long countByStatus(Collection<Enrollment> enrollments, EnrollmentStatus status) {
        return enrollments.stream()
                .filter(e -> e.status() == status)
                .count();
    }

    public static double calculateCompletionRate(Collection<Enrollment> enrollments) {
        long total = enrollments.size();
        if (total == 0) return 0.0;

        long completed = enrollments.stream()
                .filter(e -> e.status() == EnrollmentStatus.COMPLETED)
                .count();

        return (completed * 100.0) / total;
    }

    public static <T, R> List<R> transformEnrollments(
            Collection<? extends T> enrollments,
            Function<? super T, ? extends R> transformer) {
        return enrollments.stream()
                .map(transformer)
                .collect(Collectors.toList());
    }

    public static List<String> getStudentNamesFromEnrollments(Collection<Enrollment> enrollments) {
        return transformEnrollments(enrollments,
                e -> e.student().getFirstName() + " " + e.student().getLastName());
    }
}