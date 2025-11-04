package org.example.services;

import org.example.entities.Course;
import org.example.entities.Enrollment;
import org.example.entities.Student;
import org.example.exceptions.LimitExceededException;
import org.example.exceptions.TooManyAttemptsException;
import org.example.utils.InputHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * @param studenti array of students available for enrollment (not null)
     * @param courses array of courses available for enrollment (not null)
     * @param maxCoursesOvr total number of available courses
     * @return array of created enrollments
     * @throws TooManyAttemptsException if input validation fails after 3 attempts
     * @throws NullPointerException if any array is null
     */
    public static Enrollment[] enrollStudents(Student[] studenti, Course[] courses, int maxCoursesOvr) throws TooManyAttemptsException {
        logger.info("Unos upisa studenata počeo");
        Enrollment[] enrollments = new Enrollment[studenti.length];

        for(int i = 0; i < studenti.length; i++){
            int sid = InputHelper.readPositiveInt("Upis #" + (i + 1) + ":\nOdaberi ID studenta (1-" + studenti.length + "): ") - 1;
            while (sid < 0 || sid >= studenti.length) {
                System.out.println("Neispravan ID studenta!");
                sid = InputHelper.readPositiveInt("Odaberi ID studenta (1-" + studenti.length + "): ") - 1;
                logger.warn("Korisnik odabrao neispravan ID studenta: {}", sid);
            }

            int cid = InputHelper.readPositiveInt("Odaberi tecaj (1-" + maxCoursesOvr + "): ") - 1;
            while (cid < 0 || cid >= maxCoursesOvr) {
                System.out.println("Neispravan ID tecaja!");
                cid = InputHelper.readPositiveInt("Odaberi tecaj (1-" + maxCoursesOvr + "): ") - 1;
                logger.warn("Korisnik odabrao neispravan ID tečaja: {}", cid);
            }

            int semestar;
            do {
                semestar = InputHelper.readPositiveInt("Unesi semestar (1-6): ");
            } while (semestar < 1 || semestar > 6);

            enrollments[i] = new Enrollment(studenti[sid], courses[cid], semestar);
            try {
                studenti[sid].enrollCourses(courses[cid].getName());
                logger.info("Student {} upisan u tečaj {}", studenti[sid].getFirstName(), courses[cid].getName());
            } catch (LimitExceededException e) {
                System.out.println("⚠️" + e.getMessage());
                logger.warn("Student {} pokušao previše tečajeva: {}", studenti[sid].getFirstName(), e.getMessage());
            }
        }
        logger.info("Unos upisa studenata završen.");
        return enrollments;
    }
}