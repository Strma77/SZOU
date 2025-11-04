package org.example.services;

import org.example.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Provides formatting and printing utilities for displaying entity information.
 * <p>
 * This service handles console output for enrollments, users, courses, and lessons
 * with hierarchical formatting and detailed information display.
 */
public class PrintService {

    private static final Logger logger = LoggerFactory.getLogger(PrintService.class);

    /**
     * Prints all enrollment records with student, course, and semester information.
     * <p>
     * Displays each enrollment on a separate line with format:
     * {@code Student: [FirstName] [LastName] | Tečaj: [CourseName] | Semestar: [Semester]}
     *
     * @param enrollments array of enrollments to print (not null)
     * @throws NullPointerException if enrollments array or any enrollment is null
     */
    public static void printEnrollments(Enrollment[] enrollments){
        for(Enrollment e : enrollments){
            System.out.println("Student: " + e.student().getFirstName() + " " + e.student().getLastName() +
                    " | Tečaj: " + e.course().getName() +
                    " | Semestar: " + e.semester());
        }
    }

    /**
     * Prints all users with their associated courses and lessons in hierarchical format.
     * <p>
     * Differentiates between professors and students, displaying teaching courses for
     * professors and enrolled courses with ECTS for students. Includes lesson details
     * for all courses.
     *
     * @param users array of users to print (not null)
     * @param courses array of courses for lookup (not null)
     * @throws NullPointerException if any array is null
     */
    public static void printUsers(List<User> users, Course[] courses) {
        logger.info("\n=== Ispis svih profesora i studenata ===");

        for (User u : users) {
            if (u instanceof Professor p) {
                printProfessor(p, courses);
            } else if (u instanceof Student s) {
                printStudent(s, courses);
            }
            System.out.println();
        }
    }

    /**
     * Prints professor information with teaching courses and associated lessons.
     *
     * @param p the professor to print (not null)
     * @param courses array of courses for lesson lookup (not null)
     */
    private static void printProfessor(Professor p, Course[] courses) {
        System.out.println(p.getFirstName() + " " + p.getLastName());
        System.out.println("  Predaje " + p.getCourseCount() + " tečaja:");

        for (int i = 0; i < p.getCourseCount(); i++) {
            String courseName = p.getTeachingCourses()[i];
            System.out.println("    • " + courseName);
            printLessonsForCourseName(courseName, courses);
        }
    }

    /**
     * Prints student information with enrolled courses, ECTS, and associated lessons.
     *
     * @param s the student to print (not null)
     * @param courses array of courses for ECTS and lesson lookup (not null)
     */
    private static void printStudent(Student s, Course[] courses) {
        System.out.println(s.getFirstName() + " " + s.getLastName());
        System.out.println("  Polazi " + s.getCourseCount() + " tečajeva:");

        for (int i = 0; i < s.getCourseCount(); i++) {
            String courseName = s.getEnrolledCourses()[i];
            System.out.print("    • " + courseName);
            printECTSForCourse(courseName, courses);
            printLessonsForCourseName(courseName, courses);
        }
    }

    /**
     * Prints all lessons for a specified course name.
     *
     * @param courseName the name of the course (case-insensitive)
     * @param courses array of courses to search (not null)
     */
    private static void printLessonsForCourseName(String courseName, Course[] courses) {
        Course course = findCourse(courseName, courses);
        if (course == null) return;

        Lesson[] lessons = course.getLessons();
        for (Lesson lesson : lessons) {
            if (lesson != null) {
                System.out.println("      - " + lesson.getName());
            }
        }
    }

    /**
     * Prints ECTS credits for a specified course name.
     *
     * @param courseName the name of the course (case-insensitive)
     * @param courses array of courses to search (not null)
     */
    private static void printECTSForCourse(String courseName, Course[] courses) {
        Course course = findCourse(courseName, courses);
        if (course != null) {
            System.out.println(" - " + course.getECTS() + " ECTS");
        }
    }

    /**
     * Finds a course by name using case-insensitive search.
     *
     * @param courseName the name to search for (case-insensitive)
     * @param courses array of courses to search (not null)
     * @return matching course or null if not found
     */
    private static Course findCourse(String courseName, Course[] courses) {
        for (Course c : courses) {
            if (c.getName().equalsIgnoreCase(courseName)) {
                return c;
            }
        }
        return null;
    }
}