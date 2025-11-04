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
 * Provides service methods for managing student course enrollments.
 * <p>
 * This service handles interactive enrollment creation with validation for student IDs,
 * course IDs, and semester values. Enforces course enrollment limits per student.
 */
public class EnrollmentService {

    public static final Logger logger = LoggerFactory.getLogger(EnrollmentService.class);

    /**
     * Creates enrollments for students through interactive console input.
     * <p>
     * Prompts for student ID, course ID, and semester for each enrollment. Validates
     * all inputs and enforces student course limits. IDs are 1-indexed in user input
     * but converted to 0-indexed for array access.
     *
     * <p><b>Valid input ranges:</b>
     * <ul>
     *   <li>Student ID: 1 to {@code studenti.length}</li>
     *   <li>Course ID: 1 to {@code maxCoursesOvr}</li>
     *   <li>Semester: 1 to 6</li>
     * </ul>
     *
     * <p><b>Edge cases:</b>
     * <ul>
     *   <li>If student exceeds course limit, enrollment record is created but course not added to student</li>
     *   <li>Same student can be enrolled multiple times with different courses</li>
     *   <li>No duplicate enrollment validation (same student + course allowed)</li>
     * </ul>
     *
     * @param students array of students available for enrollment (not null)
     * @param courses array of courses available for enrollment (not null)
     * @param maxCoursesOvr total number of available courses
     * @return list of created enrollments
     * @throws TooManyAttemptsException if input validation fails after 3 attempts
     * @throws NullPointerException if any array is null
     */
    public static List<Enrollment> enrollStudents(List<Student> students, List<Course> courses, int maxCoursesOvr) throws TooManyAttemptsException {
        logger.info("Student enrollment beginning.");
        List<Enrollment> enrollments = new ArrayList<>();

        for(int i = 0; i < students.size(); i++){
            int sid = InputHelper.readPositiveInt("Enrollment #" + (i + 1) + ":\nChoose student ID (1-" + students.size() + "): ") - 1;
            while (sid < 0 || sid >= students.size()) {
                System.out.println("Invalid student ID!");
                sid = InputHelper.readPositiveInt("Choose student ID (1-" + students.size() + "): ") - 1;
                logger.warn("User chose invalid student ID: {}", sid);
            }

            int cid;
            do {
                cid = InputHelper.readPositiveInt("Choose course ID (1-" + maxCoursesOvr + "): ") - 1;

                if (cid >= courses.size()) {
                    System.out.println("⚠️ Only " + courses.size() + " courses available. Try again.");
                    logger.warn("User chose course ID beyond list size: {}", cid);
                    cid = -1;
                } else if (cid < 0) {
                    System.out.println("Invalid choice, must be at least 1!");
                    cid = -1;
                }
            } while (cid < 0);

            int semester;
            do {
                semester = InputHelper.readPositiveInt("Choose semester (1-6): ");
            } while (semester < 1 || semester > 6);

            Student selectedStudent = students.get(sid);
            Course selectedCourse = courses.get(cid);

            Enrollment enrollment = new Enrollment(selectedStudent, selectedCourse, semester);
            enrollments.add(enrollment);

            try {
                selectedStudent.enrollCourses(selectedCourse.getName());
                logger.info("Student {} upisan u tečaj {}", selectedStudent.getFirstName(), selectedCourse.getName());
            } catch (LimitExceededException e) {
                System.out.println("⚠️" + e.getMessage());
                logger.warn("Student {} pokušao previše tečajeva: {}", selectedStudent.getFirstName(), e.getMessage());
            }
        }
        logger.info("Unos upisa studenata završen.");
        return enrollments;
    }
}