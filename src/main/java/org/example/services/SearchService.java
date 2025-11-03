package org.example.services;

import org.example.entities.Course;
import org.example.entities.Professor;
import org.example.entities.Student;
import org.example.exceptions.NotFoundException;
import org.example.exceptions.TooManyAttemptsException;
import org.example.utils.InputHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides interactive search functionality for students, professors, and courses.
 * <p>
 * This service implements a menu-driven search loop that delegates to appropriate
 * service methods based on user selection.
 */
public class SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    /**
     * Runs an interactive search loop with menu options for different entity types.
     * <p>
     * Displays a menu allowing users to search for students by first name, professors
     * by last name, or courses by name. Continues until user selects exit option ('Q').
     * Handles search errors gracefully and logs all operations.
     *
     * <p><b>Menu options:</b>
     * <ul>
     *   <li>A - Search student by first name</li>
     *   <li>B - Search professor by last name</li>
     *   <li>C - Search course by name</li>
     *   <li>Q - Exit search loop</li>
     * </ul>
     *
     * @param studenti array of students to search (not null)
     * @param profesori array of professors to search (not null)
     * @param courses array of courses to search (not null)
     * @throws TooManyAttemptsException if input validation fails after 3 attempts
     * @throws NullPointerException if any array is null
     */
    public static void searchLoop(Student[] studenti, Professor[] profesori, Course[] courses) throws TooManyAttemptsException {

        logger.info("Search loop started");
        char choice;

        do {
            System.out.println("""
                    
                    === Pretraživanje ===
                    A - Student po imenu
                    B - Profesor po prezimenu
                    C - Tečaj po nazivu
                    Q - Izlaz
                    """);

            choice = InputHelper.readNonEmptyString("Izbor: ").toUpperCase().charAt(0);
            try {
                switch (choice) {
                    case 'A' -> UserService.findStudentByFirstName(studenti);
                    case 'B' -> UserService.findProfesorByLastName(profesori);
                    case 'C' -> CourseService.findCourseByName(courses);

                    case 'Q' -> {
                        logger.info("\nExiting search loop...");
                        System.out.println("Izlaz...");
                    }

                    default -> {
                        System.out.println("Neispravan izbor!");
                        logger.warn("Invalid search choice: {}", choice);
                    }
                }
            } catch (NotFoundException e) {
                System.out.println("⚠️ " + e.getMessage());
                logger.error("Search error: {}", e.getMessage());
            }

        } while (choice != 'Q');
    }
}