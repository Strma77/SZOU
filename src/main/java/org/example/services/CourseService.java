package org.example.services;

import org.example.entities.*;
import org.example.exceptions.*;
import org.example.utils.InputHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides service methods for managing courses and lessons.
 * <p>
 * This service handles interactive creation of courses with lessons, professor assignment,
 * and course searching functionality. Includes date and time parsing with validation.
 */
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Creates courses through interactive console input with lessons and professor assignment.
     * <p>
     * Prompts for course name, ECTS credits, professor selection, and lesson details for each
     * course. Automatically updates professor's teaching courses list.
     *
     * @param courseNum number of courses to create
     * @param users array of users for professor selection (not null)
     * @return array of newly created courses
     * @throws TooManyAttemptsException if input validation fails after 3 attempts
     * @throws NullPointerException if users array is null
     */
    public static List<Course> createCourses(int courseNum, List<User> users) throws TooManyAttemptsException {
        List<Course> courses = new ArrayList<>();
        logger.info("\nCourse {} input", courseNum);

        for (int i = 0; i < courseNum; i++) {
            System.out.println("\n--- Course #" + (i + 1) + " ---");
            String courseName = InputHelper.readNonEmptyString("Name: ");
            int ects = InputHelper.readPositiveInt("How many ECTS: ");
            Professor prof = selectProfessor(users);
            List<Lesson> lessons = createLessons();
            Course course = new Course(courseName, prof, lessons.size(), ects);

            for(Lesson l : lessons) course.addLesson(l);
            if(prof != null) prof.addCourse(courseName);

            courses.add(course);

            logger.info("\nCourse created: {} (ECTS={}, Profesor={})", courseName, ects, prof.getFirstName());
        }
        return courses;
    }

    /**
     * Prompts user to select a professor from available users.
     * <p>
     * Displays numbered list of all professors and validates selection.
     *
     * @param users array containing professors (not null)
     * @return selected professor or null if no valid selection
     * @throws TooManyAttemptsException if input validation fails after 3 attempts
     */
    private static Professor selectProfessor(List<User> users) throws TooManyAttemptsException {
        System.out.println("Choose the teacher for this course: ");
        int index = 1;
        for (User u : users) {
            if (u instanceof Professor) {
                System.out.println(index + ": " + u.getFirstName() + " " + u.getLastName());
                index++;
            }
        }

        int chosen;
        while (true) {
            chosen = InputHelper.readPositiveInt("Which teacher (index): ");
            if (chosen > 0 && chosen < index) break;
            System.out.println("Invalid index. Try again.");
            logger.warn("Invalid teacher index: {}", chosen);
        }

        int counter = 0;
        for (User u : users) {
            if (u instanceof Professor) {
                if (counter == chosen - 1) return (Professor) u;
                counter++;
            }
        }
        return null;
    }

    /**
     * Creates lessons through interactive console input with scheduling.
     * <p>
     * Prompts for lesson name, duration, date, and start time for each lesson.
     * Date format: {@code dd-MM-yyyy}, time format: {@code HH:mm}.
     *
     * @return list of created and scheduled lessons
     * @throws TooManyAttemptsException if input validation fails after 3 attempts
     */
    private static List<Lesson> createLessons() throws TooManyAttemptsException {
        int lessonCount = InputHelper.readPositiveInt("How many lessons does this course have?: ");
        List<Lesson> lessons = new ArrayList<>();

        for (int i = 0; i < lessonCount; i++) {
            System.out.println("\n--- Lesson #" + (i + 1) + " ---");
            String name = InputHelper.readNonEmptyString("Name: ");
            int duration = InputHelper.readPositiveInt("Length (min): ");

            LocalDate date = readDate();
            LocalTime startTime = readTime();

            Lesson lesson = new Lesson(name, duration);
            lesson.schedule(date, startTime.getHour(), startTime.getMinute(), duration);
            lessons.add(lesson);

            logger.debug("\nLesson created: {} {} {}:{} length {}min", name, date, startTime.getHour(), startTime.getMinute(), duration);
        }
        return lessons;
    }

    /**
     * Reads and parses a date from console input with validation.
     * <p>
     * Expected format: {@code dd-MM-yyyy}. Retries on invalid format.
     *
     * @return parsed LocalDate
     * @throws TooManyAttemptsException if input validation fails after 3 attempts
     */
    private static LocalDate readDate() throws TooManyAttemptsException {
        while (true) {
            String input = InputHelper.readNonEmptyString("Date of the lesson (dd-MM-yyyy): ");
            try {
                return LocalDate.parse(input, DATE_FMT);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format! Try again.");
                logger.warn("Invalid lesson date: {}", input);
            }
        }
    }

    /**
     * Reads and parses a time from console input with validation.
     * <p>
     * Expected format: {@code HH:mm}. Retries on invalid format.
     *
     * @return parsed LocalTime
     * @throws TooManyAttemptsException if input validation fails after 3 attempts
     */
    private static LocalTime readTime() throws TooManyAttemptsException {
        while (true) {
            String input = InputHelper.readNonEmptyString("Lesson start time (HH:mm): ");
            try {
                return LocalTime.parse(input, TIME_FMT);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format! Try again.");
                logger.warn("Invalid lesson time: {}", input);
            }
        }
    }

    /**
     * Finds and displays courses by name through interactive search.
     * <p>
     * Performs case-insensitive search and displays course name, ECTS, and professor.
     * Null elements are skipped.
     *
     * @param courses array of courses to search (not null)
     * @throws NotFoundException if no course with specified name is found
     * @throws TooManyAttemptsException if input validation fails after 3 attempts
     * @throws NullPointerException if courses array is null
     */
    public static void findCourseByName(List<Course> courses) throws NotFoundException, TooManyAttemptsException {
        String cName = InputHelper.readNonEmptyString("Insert course name: ");
        boolean found = false;

        for (Course course : courses) {
            if (course != null && course.getName().equalsIgnoreCase(cName)) {

                System.out.println("Course found: " + course.getName() +
                        " (ECTS: " + course.getECTS() + ")" +
                        " (Profesor: " + course.getProfessor().getFirstName() +
                        " " + course.getProfessor().getLastName() + ")");

                logger.info("\nCourse found: {}", course.getName());
                found = true;
            }
        }

        if (!found) throw new NotFoundException("Course not found.");
    }
}