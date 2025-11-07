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

public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public static List<Course> createCourses(int courseNum, List<User> users) throws TooManyAttemptsException {
        List<Course> courses = new ArrayList<>();
        logger.info("Starting creation of {} courses", courseNum);

        for (int i = 0; i < courseNum; i++) {
            System.out.println("\n--- Course #" + (i + 1) + " ---");

            String courseName = InputHelper.readNonEmptyString("Name: ");
            int ects = InputHelper.readPositiveInt("How many ECTS: ");

            Professor prof = selectProfessor(users);
            List<Lesson> lessons = createLessons();

            Course course = new Course(courseName, prof, 50, ects);

            for (Lesson l : lessons) addLessonToCourse(course, l);
            prof.addCourse(courseName);

            courses.add(course);
            logger.info("Course created: {} (ECTS={}, Professor={})", courseName, ects, prof.getFirstName());
        }

        return courses;
    }

    public static void findCourseByName(List<Course> courses) throws NotFoundException, TooManyAttemptsException {
        String searchName = InputHelper.readNonEmptyString("Insert course name: ");

        List<Course> matched = courses.stream()
                .filter(c -> c != null && c.getName().equalsIgnoreCase(searchName))
                .toList();

        if (matched.isEmpty()) throw new NotFoundException("Course not found.");

        matched.forEach(c -> System.out.println(
                "Course found: " + c.getName() +
                        " (ECTS: " + c.getECTS() + ")" +
                        " (Professor: " + c.getProfessor().getFirstName() + " " + c.getProfessor().getLastName() + ")"
        ));
        matched.forEach(c -> logger.info("Course found: {}", c.getName()));
    }

    // -------------------- Helpers --------------------

    private static void addLessonToCourse(Course course, Lesson lesson) {
        boolean duplicate = course.getLessons().stream()
                .anyMatch(l -> l.getName().equalsIgnoreCase(lesson.getName()));

        if (duplicate) throw new DuplicateEnrollmentException(
                "Lesson " + lesson.getName() + " already exists in course " + course.getName());

        course.addLesson(lesson);
    }

    private static Professor selectProfessor(List<User> users) throws TooManyAttemptsException {
        List<Professor> profs = users.stream()
                .filter(u -> u instanceof Professor)
                .map(u -> (Professor) u)
                .toList();

        if (profs.isEmpty()) throw new IllegalStateException("No professors available!");

        for (int i = 0; i < profs.size(); i++)
            System.out.println((i + 1) + ": " + profs.get(i).getFirstName() + " " + profs.get(i).getLastName());

        int chosen;
        for (int attempt = 0; attempt < 3; attempt++) {
            chosen = InputHelper.readPositiveInt("Which teacher (index): ");
            if (chosen >= 1 && chosen <= profs.size()) return profs.get(chosen - 1);
            System.out.println("Invalid index. Try again.");
            logger.warn("Invalid teacher index: {}", chosen);
        }

        throw new TooManyAttemptsException("Failed to select a valid professor after 3 attempts.");
    }

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

            logger.debug("Lesson created: {} {} {}:{} length {}min", name, date, startTime.getHour(), startTime.getMinute(), duration);
        }

        return lessons;
    }

    private static LocalDate readDate() throws TooManyAttemptsException {
        for (int attempt = 0; attempt < 3; attempt++) {
            String input = InputHelper.readNonEmptyString("Date of the lesson (dd-MM-yyyy): ");
            try {
                return LocalDate.parse(input, DATE_FMT);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format! Try again.");
                logger.warn("Invalid lesson date: {}", input);
            }
        }
        throw new TooManyAttemptsException("Invalid date entered 3 times.");
    }

    private static LocalTime readTime() throws TooManyAttemptsException {
        for (int attempt = 0; attempt < 3; attempt++) {
            String input = InputHelper.readNonEmptyString("Lesson start time (HH:mm): ");
            try {
                return LocalTime.parse(input, TIME_FMT);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format! Try again.");
                logger.warn("Invalid lesson time: {}", input);
            }
        }
        throw new TooManyAttemptsException("Invalid time entered 3 times.");
    }
}