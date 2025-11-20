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

            List<Enrollment> enrollments = EnrollmentService.enrollStudents(
                    students, courses, UserService.getMaxCoursesOvr());

            DemonstrationService.demonstrateAll(students, professors, courses, enrollments);

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
        }
    }
}