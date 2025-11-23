package org.example.services;

import org.example.entities.*;
import org.example.enums.CourseLevel;
import org.example.enums.LessonType;
import org.example.exceptions.*;
import org.example.utils.InputHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Service for managing courses with sorting and stream operations.
 */
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public static List<Course> createCourses(int courseNum, List<User> users)
            throws TooManyAttemptsException {
        List<Course> courses = new ArrayList<>();
        logger.info("Starting creation of {} courses", courseNum);

        for (int i = 0; i < courseNum; i++) {
            System.out.println("\n--- Course #" + (i + 1) + " ---");

            String courseName = InputHelper.readNonEmptyString("Name: ");
            int ects = InputHelper.readPositiveInt("How many ECTS: ");
            CourseLevel level = InputHelper.readCourseLevel("Select course level: ");

            Professor prof = selectProfessor(users);
            List<Lesson> lessons = createLessons();

            Course course = new Course(courseName, prof, 50, ects, level);

            for (Lesson l : lessons) {
                course.addLesson(l);
            }
            prof.addCourse(courseName);

            courses.add(course);
            logger.info("Course created: {} (ECTS={}, Level={}, Professor={})",
                    courseName, ects, level, prof.getFirstName());
        }

        return courses;
    }

    /**
     * Sorts courses by name (alphabetically).
     */
    public static List<Course> sortCoursesByName(Collection<Course> courses) {
        return courses.stream()
                .sorted(Comparator.comparing(Course::getName))
                .toList();
    }

    /**
     * Sorts courses by ECTS credits (descending - highest first).
     */
    public static List<Course> sortCoursesByECTS(Collection<Course> courses) {
        return courses.stream()
                .sorted(Comparator.comparingInt(Course::getECTS).reversed())
                .toList();
    }

    /**
     * Sorts courses by difficulty level (BEGINNER to EXPERT).
     */
    public static List<Course> sortCoursesByLevel(Collection<Course> courses) {
        return courses.stream()
                .sorted(Comparator.comparing(c -> c.getLevel().getDifficulty()))
                .toList();
    }

    /**
     * Sorts courses by enrollment count (descending).
     */
    public static List<Course> sortCoursesByEnrollment(Collection<Course> courses) {
        return courses.stream()
                .sorted(Comparator.comparingInt(Course::getEnrollmentCount).reversed())
                .toList();
    }

    /**
     * Groups courses by their difficulty level.
     */
    public static Map<CourseLevel, List<Course>> groupCoursesByLevel(
            Collection<Course> courses) {
        return courses.stream()
                .collect(Collectors.groupingBy(Course::getLevel));
    }

    /**
     * Groups courses by professor.
     */
    public static Map<String, List<Course>> groupCoursesByProfessor(
            Collection<Course> courses) {
        return courses.stream()
                .collect(Collectors.groupingBy(c ->
                        c.getProfessor().getFirstName() + " " + c.getProfessor().getLastName()));
    }

    /**
     * Partitions courses by enrollment: popular (>= 3 students) vs unpopular.
     */
    public static Map<Boolean, List<Course>> partitionCoursesByPopularity(
            Collection<Course> courses) {
        return courses.stream()
                .collect(Collectors.partitioningBy(c -> c.getEnrollmentCount() >= 3));
    }

    /**
     * Groups courses by ECTS range.
     */
    public static Map<String, List<Course>> groupCoursesByECTSRange(
            Collection<Course> courses) {
        return courses.stream()
                .collect(Collectors.groupingBy(c -> {
                    int ects = c.getECTS();
                    if (ects <= 3) return "Small (1-3 ECTS)";
                    if (ects <= 6) return "Medium (4-6 ECTS)";
                    return "Large (7+ ECTS)";
                }));
    }

    public static void findCourseByName(Collection<Course> courses)
            throws NotFoundException, TooManyAttemptsException {
        String searchName = InputHelper.readNonEmptyString("Insert course name: ");

        List<Course> matched = courses.stream()
                .filter(c -> c.getName().equalsIgnoreCase(searchName))
                .toList();

        if (matched.isEmpty()) throw new NotFoundException("Course not found.");

        matched.forEach(c -> {
            System.out.println("Course found: " + c.getName() +
                    " (ECTS: " + c.getECTS() + ")" +
                    " (Level: " + c.getLevel() + ")" +
                    " (Professor: " + c.getProfessor().getFirstName() + " " +
                    c.getProfessor().getLastName() + ")");
            logger.info("Course found: {}", c.getName());
        });
    }

    private static Professor selectProfessor(List<User> users) throws TooManyAttemptsException {
        List<Professor> profs = users.stream()
                .filter(u -> u instanceof Professor)
                .map(u -> (Professor) u)
                .toList();

        if (profs.isEmpty()) throw new IllegalStateException("No professors available!");

        for (int i = 0; i < profs.size(); i++) {
            System.out.println((i + 1) + ": " + profs.get(i).getFirstName() + " " +
                    profs.get(i).getLastName());
        }

        int chosen = InputHelper.readChoice("Which teacher (index): ", profs.size());
        return profs.get(chosen);
    }

    private static List<Lesson> createLessons() throws TooManyAttemptsException {
        int lessonCount = InputHelper.readPositiveInt("How many lessons does this course have?: ");
        List<Lesson> lessons = new ArrayList<>();

        for (int i = 0; i < lessonCount; i++) {
            System.out.println("\n--- Lesson #" + (i + 1) + " ---");

            String name = InputHelper.readNonEmptyString("Name: ");
            int duration = InputHelper.readPositiveInt("Length (min): ");
            LessonType type = InputHelper.readLessonType("Select lesson type: ");

            LocalDate date = readDate();
            LocalTime startTime = readTime();

            Lesson lesson = new Lesson(name, duration, type);
            lesson.schedule(date, startTime.getHour(), startTime.getMinute(), duration);
            lessons.add(lesson);

            logger.debug("Lesson created: {} ({}) {} {}:{} length {}min",
                    name, type, date, startTime.getHour(), startTime.getMinute(), duration);
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

    public static <T> List<T> filterCourses(Collection<? extends T> courses,
                                            Predicate<? super T> predicate) {
        return courses.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public static List<Course> filterCoursesByMinECTS(Collection<Course> courses, int minECTS) {
        return filterCourses(courses, c -> c.getECTS() >= minECTS);
    }

    public static List<Course> filterCoursesByLevel(Collection<Course> courses, CourseLevel level) {
        return filterCourses(courses, c -> c.getLevel() == level);
    }

    public static List<Course> filterCoursesByProfessor(Collection<Course> courses, Professor professor) {
        return filterCourses(courses, c -> c.getProfessor().equals(professor));
    }

    public static List<String> getCourseNames(Collection<Course> courses) {
        return courses.stream()
                .map(Course::getName)
                .collect(Collectors.toList());
    }

    public static Optional<Course> findMostPopularCourse(Collection<Course> courses) {
        return courses.stream()
                .max(Comparator.comparingInt(Course::getEnrollmentCount));
    }

    public static int calculateTotalECTS(Collection<Course> courses) {
        return courses.stream()
                .mapToInt(Course::getECTS)
                .reduce(0, Integer::sum);
    }

    public static double calculateAverageEnrollment(Collection<Course> courses) {
        return courses.stream()
                .mapToInt(Course::getEnrollmentCount)
                .average()
                .orElse(0.0);
    }

    public static <T extends Comparable<T>> Optional<T> findMaximum(Collection<T> elements) {
        return elements.stream()
                .max(Comparator.naturalOrder());
    }

    public static boolean anyCourseMatches(Collection<Course> courses, Predicate<Course> predicate) {
        return courses.stream().anyMatch(predicate);
    }

    public static boolean allCoursesMatch(Collection<Course> courses, Predicate<Course> predicate) {
        return courses.stream().allMatch(predicate);
    }
}