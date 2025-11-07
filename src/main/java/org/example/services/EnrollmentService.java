package org.example.services;

import org.example.entities.Course;
import org.example.entities.Enrollment;
import org.example.entities.Student;
import org.example.exceptions.LimitExceededException;
import org.example.exceptions.TooManyAttemptsException;
import org.example.utils.InputHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles student course enrollments.
 * <p>
 * Provides both core enrollment logic and interactive console input.
 */
public class EnrollmentService {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentService.class);

    /**
     * Core method: enrolls a student in a course for a given semester.
     *
     * @param student the student to enroll
     * @param course the course to enroll in
     * @param semester semester number (1-6)
     * @return Enrollment object
     * @throws LimitExceededException if the student has reached max courses
     */
    public static Enrollment enrollStudent(Student student, Course course, int semester) throws LimitExceededException {
        student.enrollCourses(course.getName());
        logger.info("Student {} {} enrolled in course {}", student.getFirstName(), student.getLastName(), course.getName());
        return new Enrollment(student, course, semester);
    }

    /**
     * Interactive method: lets user create multiple enrollments via console.
     *
     * @param students list of students
     * @param courses list of courses
     * @return list of created enrollments
     * @throws TooManyAttemptsException if input fails too many times
     */
    public static List<Enrollment> enrollStudents(List<Student> students, List<Course> courses, int maxCourses) throws TooManyAttemptsException {
        List<Enrollment> enrollments = new ArrayList<>();

        for (Student student : students) {
            System.out.println("\n--- Enroll courses for: " + student.getFirstName() + " " + student.getLastName() + " ---");

            for (int i = 0; i < maxCourses; i++) {
                System.out.println("\nCourse #" + (i + 1) + " for " + student.getFirstName());

                Course selectedCourse = selectCourse(courses);
                int semester = selectSemester();

                try {
                    Enrollment enrollment = enrollStudent(student, selectedCourse, semester);
                    enrollments.add(enrollment);
                } catch (LimitExceededException e) {
                    System.out.println("⚠️ " + e.getMessage());
                    logger.warn("Student {} tried to enroll in too many courses: {}", student.getFirstName(), e.getMessage());
                }
            }
        }

        logger.info("Enrollment creation finished. Total enrollments: {}", enrollments.size());
        return enrollments;
    }

    private static Course selectCourse(List<Course> courses) throws TooManyAttemptsException {
        System.out.println("Available courses:");
        for (int i = 0; i < courses.size(); i++) {
            Course c = courses.get(i);
            System.out.println((i + 1) + ": " + c.getName() + " (ECTS: " + c.getECTS() + ")");
        }

        int choice = 0;
        while (true) {
            choice = InputHelper.readPositiveInt("Choose course ID (1-" + courses.size() + "): ");
            if (choice >= 1 && choice <= courses.size()) break;
            System.out.println("Invalid ID. Try again.");
            logger.warn("Invalid course ID chosen: {}", choice);
        }
        return courses.get(choice - 1);
    }

    private static int selectSemester() throws TooManyAttemptsException {
        int semester;
        while (true) {
            semester = InputHelper.readPositiveInt("Choose semester (1-6): ");
            if (semester >= 1 && semester <= 6) break;
            System.out.println("Semester must be between 1 and 6. Try again.");
            logger.warn("Invalid semester chosen: {}", semester);
        }
        return semester;
    }
}
