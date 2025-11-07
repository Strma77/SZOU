package org.example.app;
import org.example.entities.*;
import org.example.exceptions.*;
import org.example.utils.InputHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.services.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Main application entry point for the course management system.
 * <p>
 * This class orchestrates the creation of professors, students, courses, and enrollments
 * through interactive console input, followed by search and display functionality.
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * Executes the main application workflow.
     * <p>
     * Workflow steps:
     * <ol>
     *   <li>Prompts for and creates professors</li>
     *   <li>Prompts for and creates students</li>
     *   <li>Merges users into single array</li>
     *   <li>Prompts for and creates courses with lessons</li>
     *   <li>Creates student enrollments</li>
     *   <li>Displays enrollments</li>
     *   <li>Runs interactive search loop</li>
     *   <li>Displays all users with their courses</li>
     * </ol>
     *
     * <p>If any input validation fails after 3 attempts, the program terminates
     * and logs the error.
     *
     * @param args command line arguments (not used)
     */
    static void main(String[] args) {
        logger.info("\n=== App begun ===");

        try {
            Set<Professor> professors = UserService.createProfessors();
            Set<Student> students = UserService.createStudents();

            List<User> users = UserService.mergeUsers(professors, students);

            int cNum = InputHelper.readPositiveInt("How many courses would you like to input?: ");
            List<Course> courses = CourseService.createCourses(cNum, users);

            List<Enrollment> enrollments = EnrollmentService.enrollStudents(students, courses, UserService.getMaxCoursesOvr());

            PrintService.printEnrollments(enrollments);

            SearchService.searchLoop(students, professors, courses);

            PrintService.printUsers(users, courses);

            logger.info("Program finished successfully!");
        } catch (TooManyAttemptsException e) {
            logger.error("Program interrupted: {}", e.getMessage());
        }
    }
}