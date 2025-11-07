package org.example.services;

import org.example.entities.*;
import org.example.exceptions.*;
import org.example.utils.InputHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Provides service methods for managing user entities including professors and students.
 * <p>
 * This service handles creation, searching, and credential generation for {@link Professor}
 * and {@link Student} objects. Usernames are generated as first char of first name + last name.
 *
 * <p><b>Thread Safety:</b> Not thread-safe due to shared {@code maxCoursesOvr} state.
 */
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    /**
     * Tracks cumulative total of maximum courses across all students created.
     */
    private static int maxCoursesOvr = 0;

    /**
     * Creates professors through interactive console input with auto-generated credentials.
     * <p>
     * IDs start at 10, email format: {@code username[ID]@profuni.hr}, password: {@code [ID]123}.
     *
     * @return list of newly created professors
     * @throws TooManyAttemptsException if input validation fails after 3 attempts
     * @throws StringIndexOutOfBoundsException if first name is empty
     */
    public static List<Professor> createProfessors() throws TooManyAttemptsException {
        List<Professor> professors = new ArrayList<>();

        int profNum = InputHelper.readPositiveInt("How many professors would you like to input?: ");

        for (int i = 0; i < profNum; i++) {
            logger.info("\nProfessor input #{}", i + 1);
            String firstName = InputHelper.readNonEmptyString("Name: ");
            String lastName = InputHelper.readNonEmptyString("Surname: ");
            int ID = 10 + i;
            String username = (firstName.charAt(0) + lastName).toLowerCase();
            String email = username + ID + "@profuni.hr";
            String passwd = ID + "123";
            int maxC = InputHelper.readPositiveInt("How many courses is this teacher teaching?: ");

            logger.debug("\nProfesor: {} {}, ID={}, username={}, maxCourses={}", firstName, lastName, ID, username, maxC);

            professors.add(new Professor.ProfessorBuilder(firstName, lastName, ID)
                    .username(username)
                    .password(passwd)
                    .email(email)
                    .maxCourses(maxC)
                    .build());
        }

        return professors;
    }

    /**
     * Creates students through interactive console input with auto-generated credentials.
     * <p>
     * IDs start at 100, email format: {@code username[ID]@studuni.hr}, password: {@code [ID]456}.
     * Updates {@link #maxCoursesOvr} with cumulative course totals.
     *
     * @return list of newly created students
     * @throws TooManyAttemptsException if input validation fails after 3 attempts
     * @throws StringIndexOutOfBoundsException if first name is empty
     */
    public static List<Student> createStudents() throws TooManyAttemptsException {
        List<Student> students = new ArrayList<>();

        int studNum = InputHelper.readPositiveInt("How many students would you like to input?: ");

        for (int i = 0; i < studNum; i++) {
            logger.info("\nStudent input #{}", i + 1);
            String firstName = InputHelper.readNonEmptyString("Name: ");
            String lastName = InputHelper.readNonEmptyString("Surname: ");
            int ID = 100 + i;
            String username = (firstName.charAt(0) + lastName).toLowerCase();
            String email = username + ID + "@studuni.hr";
            String passwd = ID + "456";
            int maxC = InputHelper.readPositiveInt("How many courses is this student taking?: ");

            logger.debug("\nStudent: {} {}, ID={}, username={}, maxCourses={}", firstName, lastName, ID, username, maxC);

            students.add(new Student.StudentBuilder(firstName, lastName, ID)
                    .username(username)
                    .password(passwd)
                    .email(email)
                    .maxCourses(maxC)
                    .build());
        }

        for(Student s : students){
            maxCoursesOvr += s.getCourseCount();
        }

        return students;
    }

    /**
     * Merges professor and student lists into a single user list.
     * <p>
     * Professors appear first, followed by students, preserving original order.
     *
     * @param professors the list of professors (not null)
     * @param students the list of students (not null)
     * @return merged list containing all users
     * @throws NullPointerException if either list is null
     */
    public static List<User> mergeUsers(List<Professor> professors, List<Student> students) {
        List<User> users = Stream.concat(professors.stream(), students.stream()).toList();
        logger.info("\n======All users connected in one list: overall {}======", users.size());
        return users;
    }

    /**
     * Finds and displays students by first name through interactive search.
     * <p>
     * Performs case-insensitive search and displays all matches. Null elements are skipped.
     *
     * @param students the list of students to search (not null)
     * @throws NotFoundException if no student with specified name is found
     * @throws TooManyAttemptsException if input validation fails after 3 attempts
     * @throws NullPointerException if list is null
     */
    public static void findStudentByFirstName(List<Student> students) throws NotFoundException, TooManyAttemptsException {
        String name = InputHelper.readNonEmptyString("Name of the student: ");
        boolean found = false;

        for (Student s : students) {
            if (s != null && s.getFirstName().equalsIgnoreCase(name)) {
                System.out.println("Found: " + s.getFirstName() + " " + s.getLastName());
                logger.info("\nStudent found: {} {}", s.getFirstName(), s.getLastName());
                found = true;
            }
        }

        if (!found) throw new NotFoundException("Student not found.");
    }

    /**
     * Finds and displays professors by last name through interactive search.
     * <p>
     * Performs case-insensitive search and displays all matches. Null elements are skipped.
     *
     * @param professors the array of professors to search (not null)
     * @throws NotFoundException if no professor with specified name is found
     * @throws TooManyAttemptsException if input validation fails after 3 attempts
     * @throws NullPointerException if array is null
     */
    public static void findProfesorByLastName(List<Professor> professors) throws NotFoundException, TooManyAttemptsException {
        String lastName = InputHelper.readNonEmptyString("Professor name: ");
        boolean found = false;

        for (Professor p : professors) {
            if (p != null && p.getLastName().equalsIgnoreCase(lastName)) {
                System.out.println("Found: " + p.getFirstName() + " " + p.getLastName());
                logger.info("\nProfessor found: {} {}", p.getFirstName(), p.getLastName());
                found = true;
            }
        }

        if (!found) throw new NotFoundException("Professor not found.");
    }

    /**
     * Retrieves cumulative total of maximum courses across all created students.
     *
     * @return sum of all students' maxCourses values
     */
    public static int getMaxCoursesOvr(){
        return maxCoursesOvr;
    }
}