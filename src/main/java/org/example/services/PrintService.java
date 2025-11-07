package org.example.services;

import org.example.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Provides formatting and printing utilities for displaying entity information.
 * <p>
 * Handles console output for enrollments, users, courses, and lessons
 * with hierarchical formatting and detailed information display.
 */
public class PrintService {

    private static final Logger logger = LoggerFactory.getLogger(PrintService.class);

    /**
     * Prints all enrollment records with student, course, and semester information.
     *
     * @param enrollments list of enrollments (not null)
     */
    public static void printEnrollments(List<Enrollment> enrollments) {
        if (enrollments == null || enrollments.isEmpty()) {
            System.out.println("No enrollments to display.");
            return;
        }

        for (Enrollment e : enrollments) {
            if (e == null || e.student() == null || e.course() == null) continue;

            System.out.println("Student: " + e.student().getFirstName() + " " + e.student().getLastName() +
                    " | Course: " + e.course().getName() +
                    " | Semester: " + e.semester());
        }
    }

    /**
     * Prints all users (professors and students) with their courses and lessons.
     *
     * @param users   list of users (not null)
     * @param courses list of courses (not null)
     */
    public static void printUsers(List<User> users, List<Course> courses) {
        if (users == null || courses == null) {
            System.out.println("No users or courses to display.");
            return;
        }

        Map<String, Course> courseMap = new HashMap<>();
        for (Course c : courses) {
            if (c != null && c.getName() != null) {
                courseMap.put(c.getName().toLowerCase(), c);
            }
        }

        logger.info("Printing all users and their courses.");

        for (User u : users) {
            switch (u) {
                case null -> {
                    continue;
                }
                case Professor p -> printProfessor(p, courseMap);
                case Student s -> printStudent(s, courseMap);
                default -> {
                }
            }

            System.out.println();
        }
    }

    private static void printProfessor(Professor p, Map<String, Course> courseMap) {
        if (p == null) return;

        System.out.println(p.getFirstName() + " " + p.getLastName());
        System.out.println("  Teaching " + p.getTeachingCourses().size() + " course(s):");

        for (String courseName : p.getTeachingCourses()) {
            if (courseName == null) continue;

            System.out.println("    • " + courseName);

            Course c = courseMap.get(courseName.toLowerCase());
            if (c != null) {
                for (Lesson l : c.getLessons()) {
                    if (l != null) System.out.println("      - " + l.getName());
                }
            }
        }
    }

    private static void printStudent(Student s, Map<String, Course> courseMap) {
        if (s == null) return;

        System.out.println(s.getFirstName() + " " + s.getLastName());
        System.out.println("  Taking " + s.getCourseCount() + " course(s):");

        for (String courseName : s.getEnrolledCourses()) {
            if (courseName == null) continue;

            Course c = courseMap.get(courseName.toLowerCase());
            if (c != null) {
                System.out.println("    • " + c.getName() + " - " + c.getECTS() + " ECTS");

                for (Lesson l : c.getLessons()) {
                    if (l != null) System.out.println("      - " + l.getName());
                }
            } else {
                System.out.println("    • " + courseName + " - ECTS unknown");
            }
        }
    }
}
