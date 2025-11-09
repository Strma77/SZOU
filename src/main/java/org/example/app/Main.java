package org.example.app;

import org.example.entities.*;
import org.example.exceptions.*;
import org.example.services.*;
import org.example.utils.InputHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Main application entry point for the online learning system.
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("\n=== Online Learning System Started ===");

        try {
            // Step 1: Create professors and students
            Set<Professor> professors = UserService.createProfessors();
            Set<Student> students = UserService.createStudents();

            // Step 2: Merge users into single list
            List<User> users = UserService.mergeUsers(professors, students);

            // Step 3: Create courses with new enums (CourseLevel, LessonType)
            int cNum = InputHelper.readPositiveInt("How many courses would you like to input?: ");
            List<Course> courses = CourseService.createCourses(cNum, users);

            // Step 4: Enroll students in courses (using Semester enum)
            List<Enrollment> enrollments = EnrollmentService.enrollStudents(
                    students, courses, UserService.getMaxCoursesOvr());

            // DEBUG: Check student enrollment status
            System.out.println("\n🔍 DEBUG: Checking student enrollments...");
            for (Student s : students) {
                System.out.println("Student " + s.getFirstName() + " " + s.getLastName() +
                        " is enrolled in " + s.getCourseCount() + " courses");
                System.out.println("  Courses: " + s.getEnrolledCourses());
            }

            // Step 5: Assign random grades to enrollments (for demonstration)
            System.out.println("\n🎓 Assigning grades to students...");
            enrollments = GradingService.assignRandomGrades(enrollments);

            // DEBUG: Check grades after assignment
            System.out.println("\n🔍 DEBUG: Checking student grades...");
            for (Student s : students) {
                System.out.println("Student " + s.getFirstName() + " " + s.getLastName() +
                        " GPA: " + String.format("%.2f", s.calculateGPA()));
                System.out.println("  Grades: " + s.getCourseGrades());
            }

            // Step 6: Print basic enrollments
            PrintService.printEnrollments(enrollments);

            // Step 7: Run demonstrations
            DemonstrationService.demonstrateAll(students, professors, courses, enrollments);

            // Step 8: Interactive search loop
            SearchService.searchLoop(students, professors, courses);

            // Step 9: Print all users with their courses
            PrintService.printUsers(users, courses);

            // Step 10: Print statistics
            PrintService.printStudentStatistics(students);
            PrintService.printCourseStatistics(courses);

            logger.info("✅ Program finished successfully!");

        } catch (TooManyAttemptsException e) {
            logger.error("❌ Program interrupted: {}", e.getMessage());
            System.err.println("Program terminated: " + e.getMessage());
        } catch (Exception e) {
            logger.error("❌ Unexpected error: {}", e.getMessage(), e);
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}