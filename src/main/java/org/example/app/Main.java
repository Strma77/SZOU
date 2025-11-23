package org.example.app;

import org.example.entities.*;
import org.example.exceptions.*;
import org.example.services.*;
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

            DemonstrationService.demonstrateAll(students, professors, courses, enrollments);
            demonstrateLambdaUtils(students, courses);

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

    private static void demonstrateLambdaUtils(Set<Student> students, List<Course> courses) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🧰 DEMONSTRATION: LAMBDA UTILS");
        System.out.println("=".repeat(50));

        System.out.println("\n1️⃣  Transform - Extract student names:");
        List<String> names = org.example.utils.LambdaUtils.transform(students,
                s -> s.getFirstName() + " " + s.getLastName());
        names.forEach(n -> System.out.println("   - " + n));

        System.out.println("\n2️⃣  Filter - Students with GPA >= 3.0:");
        List<Student> highGPA = org.example.utils.LambdaUtils.filter(students,
                s -> s.calculateGPA() >= 3.0);
        System.out.println("   Found: " + highGPA.size() + " students");

        System.out.println("\n3️⃣  FindFirst - Student with GPA > 4.0 (Optional):");
        org.example.utils.LambdaUtils.findFirst(students, s -> s.calculateGPA() > 4.0)
                .ifPresentOrElse(
                        s -> System.out.println("   Found: " + s.getFirstName() + " " +
                                s.getLastName() + " (GPA: " + String.format("%.2f", s.calculateGPA()) + ")"),
                        () -> System.out.println("   No student found")
                );

        System.out.println("\n4️⃣  Partition - Passing vs At-Risk:");
        Map<Boolean, List<Student>> partitioned = org.example.utils.LambdaUtils.partition(
                new ArrayList<>(students), s -> s.calculateGPA() >= 2.0);
        System.out.println("   ✅ Passing: " + partitioned.get(true).size());
        System.out.println("   ❌ At-Risk: " + partitioned.get(false).size());

        System.out.println("\n5️⃣  Count - Students taking >= 2 courses:");
        long count = org.example.utils.LambdaUtils.count(students, s -> s.getCourseCount() >= 2);
        System.out.println("   Count: " + count);

        System.out.println("\n6️⃣  AnyMatch - Any student with GPA = 5.0?");
        boolean perfect = org.example.utils.LambdaUtils.anyMatch(students, s -> s.calculateGPA() == 5.0);
        System.out.println("   " + (perfect ? "Yes ✅" : "No ❌"));

        System.out.println("\n7️⃣  PECS CopyIf - Copy high GPA students:");
        List<Student> highPerformers = new ArrayList<>();
        org.example.utils.LambdaUtils.copyIf(students, highPerformers, s -> s.calculateGPA() >= 4.0);
        System.out.println("   Copied: " + highPerformers.size() + " students");

        System.out.println("\n8️⃣  GroupBy - Courses by ECTS:");
        Map<Integer, List<Course>> grouped = org.example.utils.LambdaUtils.groupByWithPECS(
                courses, Course::getECTS);
        grouped.forEach((ects, list) ->
                System.out.println("   " + ects + " ECTS: " + list.size() + " course(s)"));

        System.out.println("\n✅ LambdaUtils demonstration completed!");
    }
}