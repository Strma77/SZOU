package org.example.services;

import org.example.entities.Course;
import org.example.entities.Professor;
import org.example.entities.Student;
import org.example.exceptions.NotFoundException;
import org.example.exceptions.TooManyAttemptsException;
import org.example.utils.InputHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Service for interactive search functionality.
 */
public class SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    /**
     * Runs an interactive search loop with menu options for different entity types.
     */
    public static void searchLoop(Collection<Student> students, Collection<Professor> professors,
                                  Collection<Course> courses) throws TooManyAttemptsException {

        logger.info("Search loop started");
        char choice;

        do {
            System.out.println("""
                    
                    === SEARCH MENU ===
                    A - Search student by first name
                    B - Search professor by last name
                    C - Search course by name
                    Q - Exit search
                    """);

            choice = InputHelper.readNonEmptyString("Choice: ").toUpperCase().charAt(0);
            try {
                switch (choice) {
                    case 'A' -> UserService.findStudentByFirstName(students);
                    case 'B' -> UserService.findProfessorByLastName(professors);
                    case 'C' -> CourseService.findCourseByName(courses);

                    case 'Q' -> {
                        logger.info("Exiting search loop...");
                        System.out.println("Exiting search...");
                    }

                    default -> {
                        System.out.println("Invalid choice!");
                        logger.warn("Invalid search choice: {}", choice);
                    }
                }
            } catch (NotFoundException e) {
                System.out.println("⚠️  " + e.getMessage());
                logger.error("Search error: {}", e.getMessage());
            }

        } while (choice != 'Q');
    }
}