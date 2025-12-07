package org.example.utils;

import org.example.entities.*;
import org.example.persistence.ActivityLogger;
import org.example.services.*;

import java.util.*;

/**
 * Demonstrates various system features and statistics.
 */
public class FeatureDemo {

    public static void demonstrate(Set<Student> students, List<Course> courses,
                                   List<Enrollment> enrollments) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("📊 SYSTEM OVERVIEW & FEATURES");
        System.out.println("=".repeat(50));

        showTopStudents(students);
        showCoursesByLevel(courses);
        showEnrollmentStats(enrollments);
        showTopStudent(students);

        System.out.println("\n" + "=".repeat(50) + "\n");
        ActivityLogger.log("Viewed statistics and features demo");
    }

    private static void showTopStudents(Set<Student> students) {
        System.out.println("\n1️⃣  Top 3 Students by GPA:");
        UserService.sortStudentsByGPA(students).stream()
                .limit(3)
                .forEach(s -> System.out.println("   - " + s.getFirstName() + " " +
                        s.getLastName() + ": " + String.format("%.2f", s.calculateGPA())));
    }

    private static void showCoursesByLevel(List<Course> courses) {
        System.out.println("\n2️⃣  Courses by Level:");
        CourseService.groupCoursesByLevel(courses)
                .forEach((level, list) ->
                        System.out.println("   " + level + ": " + list.size() + " course(s)"));
    }

    private static void showEnrollmentStats(List<Enrollment> enrollments) {
        System.out.println("\n3️⃣  Enrollments by Passing Status:");
        Map<Boolean, List<Enrollment>> partitioned =
                EnrollmentService.partitionEnrollmentsByPassing(enrollments);
        System.out.println("   ✅ Passed: " + partitioned.get(true).size());
        System.out.println("   ❌ Failed: " + partitioned.get(false).size());
    }

    private static void showTopStudent(Set<Student> students) {
        System.out.println("\n4️⃣  Top Student:");
        GradingService.findTopStudentByGPA(students)
                .ifPresentOrElse(
                        s -> System.out.println("   " + s.getFirstName() + " " +
                                s.getLastName() + " (GPA: " +
                                String.format("%.2f", s.calculateGPA()) + ")"),
                        () -> System.out.println("   No students found")
                );
    }
}