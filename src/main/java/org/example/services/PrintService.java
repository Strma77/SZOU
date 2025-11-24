package org.example.services;

import org.example.entities.*;
import org.example.enums.GradeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for printing and displaying entity information with modern formatting.
 */
public class PrintService {

    private static final Logger logger = LoggerFactory.getLogger(PrintService.class);


    /**
     * Prints all users (professors and students) with their courses and details.
     */
    public static void printUsers(List<User> users, List<Course> courses) {
        if (users == null || courses == null) {
            System.out.println("No users or courses to display.");
            return;
        }

        Map<String, Course> courseMap = courses.stream()
                .collect(Collectors.toMap(
                        c -> c.getName().toLowerCase(),
                        c -> c,
                        (c1, c2) -> c1
                ));

        logger.info("Printing all users and their courses.");
        System.out.println("\n========== ALL USERS ==========");

        for (User u : users) {
            switch (u) {
                case Professor p -> printProfessor(p, courseMap);
                case Student s -> printStudent(s, courseMap);
                case null, default -> {}
            }
            System.out.println();
        }
        System.out.println("===============================\n");
    }

    /**
     * Prints detailed student statistics.
     */
    public static void printStudentStatistics(Collection<Student> students) {
        System.out.println("\n========== STUDENT STATISTICS ==========");

        double avgGPA = students.stream()
                .mapToDouble(Student::calculateGPA)
                .average()
                .orElse(0.0);

        System.out.println("Total Students: " + students.size());
        System.out.println("Average GPA: " + String.format("%.2f", avgGPA));

        System.out.println("\nTop 5 Students by GPA:");
        UserService.sortStudentsByGPA(students).stream()
                .limit(5)
                .forEach(s -> System.out.println("  - " + s.getFirstName() + " " +
                        s.getLastName() + " (GPA: " + String.format("%.2f", s.calculateGPA()) + ")"));

        System.out.println("========================================\n");
    }

    /**
     * Prints detailed course statistics.
     */
    public static void printCourseStatistics(Collection<Course> courses) {
        System.out.println("\n========== COURSE STATISTICS ==========");

        System.out.println("Total Courses: " + courses.size());

        double avgEnrollment = courses.stream()
                .mapToInt(Course::getEnrollmentCount)
                .average()
                .orElse(0.0);
        System.out.println("Average Enrollment per Course: " + String.format("%.1f", avgEnrollment));

        System.out.println("\nMost Popular Courses:");
        CourseService.sortCoursesByEnrollment(courses).stream()
                .limit(3)
                .forEach(c -> System.out.println("  - " + c.getName() +
                        " (" + c.getEnrollmentCount() + " students)"));

        System.out.println("=======================================\n");
    }

    public static void printEnrollmentsBySemester(Collection<Enrollment> enrollments) {
        Map<org.example.enums.Semester, List<Enrollment>> grouped =
                EnrollmentService.groupEnrollmentsBySemester(enrollments);

        System.out.println("\n========== ENROLLMENTS BY SEMESTER ==========");
        grouped.forEach((semester, list) -> {
            System.out.println("\n" + semester + ": " + list.size() + " enrollments");
            list.forEach(e -> System.out.println("  - " + e.student().getFirstName() +
                    " " + e.student().getLastName() + " -> " + e.course().getName()));
        });
        System.out.println("============================================\n");
    }

    private static void printProfessor(Professor p, Map<String, Course> courseMap) {
        System.out.println("👨‍🏫 PROFESSOR: " + p.getFirstName() + " " + p.getLastName());
        System.out.println("   Email: " + p.getEmail());
        System.out.println("   Teaching " + p.getTeachingCourses().size() + " course(s):");

        for (String courseName : p.getTeachingCourses()) {
            System.out.println("    • " + courseName);

            Course c = courseMap.get(courseName.toLowerCase());
            if (c != null) {
                System.out.println("      Level: " + c.getLevel() +
                        ", Students: " + c.getEnrollmentCount());
                c.getLessons().forEach(l ->
                        System.out.println("      - " + l.getName() + " (" + l.getType() + ")")
                );
            }
        }
    }

    private static void printStudent(Student s, Map<String, Course> courseMap) {
        System.out.println("👨‍🎓 STUDENT: " + s.getFirstName() + " " + s.getLastName());
        System.out.println("   Email: " + s.getEmail());
        System.out.println("   GPA: " + String.format("%.2f", s.calculateGPA()));
        System.out.println("   Taking " + s.getCourseCount() + " course(s):");

        for (String courseName : s.getEnrolledCourses()) {
            Course c = courseMap.get(courseName.toLowerCase());
            GradeType grade = s.getGrade(courseName);

            String gradeTernary = grade == GradeType.NOT_GRADED ?
                    "Not graded" : grade.name().replace("_", "+");
            if (c != null) {
                System.out.println("    • " + c.getName() + " - " + c.getECTS() + " ECTS" +
                        " (Grade: " + gradeTernary + ")");

                c.getLessons().forEach(l ->
                        System.out.println("      - " + l.getName() + " (" + l.getType() + ")")
                );
            } else {
                System.out.println("    • " + courseName + " - ECTS unknown" +
                        " (Grade: " + gradeTernary + ")");
            }
        }
    }
}