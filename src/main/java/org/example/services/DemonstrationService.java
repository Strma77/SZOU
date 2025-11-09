package org.example.services;

import org.example.entities.*;
import org.example.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Service for demonstrating modern Java features (sorting, streams, sequenced collections).
 */
public class DemonstrationService {

    private static final Logger logger = LoggerFactory.getLogger(DemonstrationService.class);

    /**
     * Runs all demonstrations.
     */
    public static void demonstrateAll(Set<Student> students, Set<Professor> professors,
                                      List<Course> courses, List<Enrollment> enrollments) {
        demonstrateSorting(students, professors, courses, enrollments);
        demonstrateStreamOperations(students, courses, enrollments);
        demonstrateSequencedCollections(students, professors);
    }

    /**
     * Demonstrates sorting with Comparators and lambda expressions.
     */
    private static void demonstrateSorting(Set<Student> students, Set<Professor> professors,
                                           List<Course> courses, List<Enrollment> enrollments) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("📊 DEMONSTRATION: SORTING WITH COMPARATORS");
        System.out.println("=".repeat(50));

        // Sort students by name
        System.out.println("\n1️⃣  Students sorted by name:");
        UserService.sortStudentsByName(students).forEach(s ->
                System.out.println("   - " + s.getFirstName() + " " + s.getLastName()));

        // Sort students by GPA (descending)
        System.out.println("\n2️⃣  Students sorted by GPA (highest first):");
        UserService.sortStudentsByGPA(students).forEach(s ->
                System.out.println("   - " + s.getFirstName() + " " + s.getLastName() +
                        " (GPA: " + String.format("%.2f", s.calculateGPA()) + ")"));

        // Sort courses by ECTS
        System.out.println("\n3️⃣  Courses sorted by ECTS (highest first):");
        CourseService.sortCoursesByECTS(courses).forEach(c ->
                System.out.println("   - " + c.getName() + " (" + c.getECTS() + " ECTS)"));

        // Sort courses by level
        System.out.println("\n4️⃣  Courses sorted by difficulty level:");
        CourseService.sortCoursesByLevel(courses).forEach(c ->
                System.out.println("   - " + c.getName() + " [" + c.getLevel() + "]"));

        // Sort enrollments by grade
        System.out.println("\n5️⃣  Enrollments sorted by grade (best first):");
        EnrollmentService.sortEnrollmentsByGrade(enrollments).stream()
                .limit(5)
                .forEach(e -> System.out.println("   - " + e.student().getFirstName() + " " +
                        e.student().getLastName() + " in " + e.course().getName() +
                        " (Grade: " + e.grade().name().replace("_", "+") + ")"));

        logger.info("Sorting demonstration completed");
    }

    /**
     * Demonstrates stream operations with groupingBy and partitioningBy.
     */
    private static void demonstrateStreamOperations(Set<Student> students,
                                                    List<Course> courses,
                                                    List<Enrollment> enrollments) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🔄 DEMONSTRATION: STREAM OPERATIONS");
        System.out.println("=".repeat(50));

        // groupingBy: Group students by course load
        System.out.println("\n1️⃣  Students grouped by course load:");
        Map<String, List<Student>> studentsByCourseLoad =
                UserService.groupStudentsByCourseLoad(students);
        studentsByCourseLoad.forEach((load, list) ->
                System.out.println("   " + load + ": " + list.size() + " students"));

        // partitioningBy: Partition students by GPA
        System.out.println("\n2️⃣  Students partitioned by passing GPA (>= 2.0):");
        Map<Boolean, List<Student>> studentsByGPA =
                UserService.partitionStudentsByGPA(students);
        System.out.println("   ✅ Passing: " + studentsByGPA.get(true).size() + " students");
        System.out.println("   ❌ At Risk: " + studentsByGPA.get(false).size() + " students");

        // groupingBy: Group courses by level
        System.out.println("\n3️⃣  Courses grouped by difficulty level:");
        Map<CourseLevel, List<Course>> coursesByLevel =
                CourseService.groupCoursesByLevel(courses);
        coursesByLevel.forEach((level, list) ->
                System.out.println("   " + level + ": " + list.size() + " courses"));

        // groupingBy: Group courses by professor
        System.out.println("\n4️⃣  Courses grouped by professor:");
        Map<String, List<Course>> coursesByProfessor =
                CourseService.groupCoursesByProfessor(courses);
        coursesByProfessor.forEach((prof, list) ->
                System.out.println("   " + prof + ": " + list.size() + " course(s)"));

        // partitioningBy: Partition courses by popularity
        System.out.println("\n5️⃣  Courses partitioned by popularity (>= 3 students):");
        Map<Boolean, List<Course>> coursesByPopularity =
                CourseService.partitionCoursesByPopularity(courses);
        System.out.println("   ⭐ Popular: " + coursesByPopularity.get(true).size() + " courses");
        System.out.println("   📉 Unpopular: " + coursesByPopularity.get(false).size() + " courses");

        // groupingBy: Group enrollments by semester
        System.out.println("\n6️⃣  Enrollments grouped by semester:");
        Map<Semester, List<Enrollment>> enrollmentsBySemester =
                EnrollmentService.groupEnrollmentsBySemester(enrollments);
        enrollmentsBySemester.forEach((semester, list) ->
                System.out.println("   " + semester + ": " + list.size() + " enrollments"));

        // partitioningBy: Partition enrollments by passing
        System.out.println("\n7️⃣  Enrollments partitioned by passing grade:");
        Map<Boolean, List<Enrollment>> enrollmentsByPassing =
                EnrollmentService.partitionEnrollmentsByPassing(enrollments);
        System.out.println("   ✅ Passed: " + enrollmentsByPassing.get(true).size() + " enrollments");
        System.out.println("   ❌ Failed: " + enrollmentsByPassing.get(false).size() + " enrollments");

        // groupingBy: Group students by GPA range
        System.out.println("\n8️⃣  Students grouped by GPA range:");
        Map<String, List<Student>> studentsByGPARange =
                GradingService.groupStudentsByGPARange(students);
        studentsByGPARange.forEach((range, list) ->
                System.out.println("   " + range + ": " + list.size() + " students"));

        logger.info("Stream operations demonstration completed");
    }

    /**
     * Demonstrates Sequenced Collections (getFirst, getLast, reversed).
     */
    private static void demonstrateSequencedCollections(Set<Student> students,
                                                        Set<Professor> professors) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🔢 DEMONSTRATION: SEQUENCED COLLECTIONS");
        System.out.println("=".repeat(50));

        // Get first and last student
        Student firstStudent = UserService.getFirstStudent(students);
        Student lastStudent = UserService.getLastStudent(students);

        System.out.println("\n1️⃣  First student enrolled: " +
                (firstStudent != null ? firstStudent.getFirstName() + " " +
                        firstStudent.getLastName() : "None"));

        System.out.println("2️⃣  Last student enrolled: " +
                (lastStudent != null ? lastStudent.getFirstName() + " " +
                        lastStudent.getLastName() : "None"));

        // Demonstrate reversed() on LinkedHashSet
        System.out.println("\n3️⃣  Students in reverse order:");
        if (students instanceof SequencedSet<Student> seq) {
            seq.reversed().stream()
                    .limit(3)
                    .forEach(s -> System.out.println("   - " + s.getFirstName() + " " +
                            s.getLastName()));
        }

        // Demonstrate with professors
        System.out.println("\n4️⃣  Professors in insertion order:");
        professors.stream()
                .limit(3)
                .forEach(p -> System.out.println("   - " + p.getFirstName() + " " +
                        p.getLastName()));

        logger.info("Sequenced collections demonstration completed");
    }
}