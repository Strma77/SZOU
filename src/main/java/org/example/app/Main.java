package org.example.app;

import org.example.entities.*;
import org.example.exceptions.*;
import org.example.services.*;
import org.example.utils.CollectionUtils;
import org.example.utils.InputHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    static void main(String[] args) {
        logger.info("\n=== Online Learning System Started ===");

        try {
            Set<Professor> professors = UserService.createProfessors();
            Set<Student> students = UserService.createStudents();
            List<User> users = UserService.mergeUsers(professors, students);

            int cNum = InputHelper.readPositiveInt("How many courses would you like to input?: ");
            List<Course> courses = CourseService.createCourses(cNum, users);

            List<Enrollment> enrollments = EnrollmentService.enrollStudents(students, courses);
            enrollments = GradingService.assignRandomGrades(enrollments);

            demonstrateFeatures(students, courses, enrollments);

            SearchService.searchLoop(students, professors, courses);

            PrintService.printUsers(users, courses);
            PrintService.printStudentStatistics(students);
            PrintService.printCourseStatistics(courses);

            logger.info("✅ Program finished successfully!");

        } catch (TooManyAttemptsException e) {
            logger.error("❌ Program interrupted: {}", e.getMessage());
            System.err.println("Program terminated: " + e.getMessage());
        } catch (Exception e) {
            logger.error("❌ Unexpected error: {}", e.getMessage(), e);
            System.err.println("An unexpected error occurred: " + e.getMessage());
        } finally {
            InputHelper.closeScanner();
        }
    }

    private static void demonstrateFeatures(Set<Student> students,
                                            List<Course> courses,
                                            List<Enrollment> enrollments) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("📊 SYSTEM OVERVIEW & FEATURES");
        System.out.println("=".repeat(50));

        System.out.println("\n1️⃣  Top 3 Students by GPA (Lambda + Comparator):");
        UserService.sortStudentsByGPA(students).stream()
                .limit(3)
                .forEach(s -> System.out.println("   - " + s.getFirstName() + " " +
                        s.getLastName() + ": " + String.format("%.2f", s.calculateGPA())));


        System.out.println("\n2️⃣  Courses by Level (groupingBy):");
        CourseService.groupCoursesByLevel(courses)
                .forEach((level, list) ->
                        System.out.println("   " + level + ": " + list.size() + " course(s)"));


        System.out.println("\n3️⃣  Enrollments by Passing Status (partitioningBy):");
        Map<Boolean, List<Enrollment>> partitioned =
                EnrollmentService.partitionEnrollmentsByPassing(enrollments);
        System.out.println("   ✅ Passed: " + partitioned.get(true).size());
        System.out.println("   ❌ Failed: " + partitioned.get(false).size());

        System.out.println("\n4️⃣  Top Student (Optional):");
        GradingService.findTopStudentByGPA(students)
                .ifPresentOrElse(
                        s -> System.out.println("   " + s.getFirstName() + " " +
                                s.getLastName() + " (GPA: " +
                                String.format("%.2f", s.calculateGPA()) + ")"),
                        () -> System.out.println("   No students found")
                );

        System.out.println("\n5️⃣  High Performers (Generic Filter with PECS):");
        List<Student> highPerformers = CollectionUtils.filter(
                students, s -> s.calculateGPA() >= 4.0);
        System.out.println("   Found: " + highPerformers.size() + " student(s)");
        highPerformers.forEach(s -> System.out.println("   - " + s.getFirstName() +
                " " + s.getLastName()));

        System.out.println("\n6️⃣  Student Names (Generic Map):");
        List<String> names = CollectionUtils.map(
                students, s -> s.getFirstName() + " " + s.getLastName());
        names.stream().limit(3).forEach(name -> System.out.println("   - " + name));

        System.out.println("\n7️⃣  Most Popular Course (Bounded Generic):");
        CourseService.findMostPopularCourse(courses)
                .ifPresent(c -> System.out.println("   " + c.getName() +
                        " (" + c.getEnrollmentCount() + " students)"));

        System.out.println("\n8️⃣  First & Last Students (SequencedSet):");
        UserService.getFirstStudent(students)
                .ifPresent(s -> System.out.println("   First: " + s.getFirstName() +
                        " " + s.getLastName()));
        UserService.getLastStudent(students)
                .ifPresent(s -> System.out.println("   Last: " + s.getFirstName() +
                        " " + s.getLastName()));

        System.out.println("\n9️⃣  Total ECTS Available (Stream Reduction):");
        int totalECTS = CourseService.calculateTotalECTS(courses);
        System.out.println("   " + totalECTS + " ECTS");

        System.out.println("\n🔟 Copy Passing Students (Consumer Super):");
        List<Student> passingStudents = new ArrayList<>();
        CollectionUtils.copyFiltered(students, passingStudents,
                s -> s.calculateGPA() >= 2.0);
        System.out.println("   Copied: " + passingStudents.size() + " student(s)");

        System.out.println("\n" + "=".repeat(50) + "\n");
    }
}