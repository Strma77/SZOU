package org.example.services;

import org.example.entities.*;
import org.example.exceptions.*;
import org.example.utils.InputHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * @param profNum the number of professors to create
     * @return array of newly created professors
     * @throws TooManyAttemptsException if input validation fails after 3 attempts
     * @throws StringIndexOutOfBoundsException if first name is empty
     */
    public static Professor[] createProfessors(int profNum) throws TooManyAttemptsException {
        Professor[] profesori = new Professor[profNum];

        for (int i = 0; i < profNum; i++) {
            logger.info("\nUnos profesora #{}", i + 1);
            String firstName = InputHelper.readNonEmptyString("Ime: ");
            String lastName = InputHelper.readNonEmptyString("Prezime: ");
            int ID = 10 + i;
            String username = (firstName.charAt(0) + lastName).toLowerCase();
            String email = username + ID + "@profuni.hr";
            String passwd = ID + "123";
            int maxC = InputHelper.readPositiveInt("Koliko tecajeva taj profesor predaje?: ");

            logger.debug("\nProfesor: {} {}, ID={}, username={}, maxCourses={}", firstName, lastName, ID, username, maxC);

            profesori[i] = new Professor.ProfessorBuilder(firstName, lastName, ID)
                    .username(username)
                    .password(passwd)
                    .email(email)
                    .maxCourses(maxC)
                    .build();
        }

        return profesori;
    }

    /**
     * Creates students through interactive console input with auto-generated credentials.
     * <p>
     * IDs start at 100, email format: {@code username[ID]@studuni.hr}, password: {@code [ID]456}.
     * Updates {@link #maxCoursesOvr} with cumulative course totals.
     *
     * @param studNum the number of students to create
     * @return array of newly created students
     * @throws TooManyAttemptsException if input validation fails after 3 attempts
     * @throws StringIndexOutOfBoundsException if first name is empty
     */
    public static Student[] createStudents(int studNum) throws TooManyAttemptsException {
        Student[] studenti = new Student[studNum];

        for (int i = 0; i < studNum; i++) {
            logger.info("\nUnos studenta #{}", i + 1);
            String firstName = InputHelper.readNonEmptyString("Ime: ");
            String lastName = InputHelper.readNonEmptyString("Prezime: ");
            int ID = 100 + i;
            String username = (firstName.charAt(0) + lastName).toLowerCase();
            String email = username + ID + "@studuni.hr";
            String passwd = ID + "456";
            int maxC = InputHelper.readPositiveInt("Koliko tecajeva taj student pohadja?: ");
            maxCoursesOvr += maxC;

            logger.debug("\nStudent: {} {}, ID={}, username={}, maxCourses={}", firstName, lastName, ID, username, maxC);

            studenti[i] = new Student.StudentBuilder(firstName, lastName, ID)
                    .username(username)
                    .password(passwd)
                    .email(email)
                    .maxCourses(maxC)
                    .build();
        }

        return studenti;
    }

    /**
     * Merges professor and student arrays into a single user array.
     * <p>
     * Professors appear first, followed by students, preserving original order.
     *
     * @param profesori the array of professors (not null)
     * @param studenti the array of students (not null)
     * @return merged array containing all users
     * @throws NullPointerException if either array is null
     */
    public static User[] mergeUsers(Professor[] profesori, Student[] studenti) {
        User[] users = new User[profesori.length + studenti.length];
        System.arraycopy(profesori, 0, users, 0, profesori.length);
        System.arraycopy(studenti, 0, users, profesori.length, studenti.length);
        logger.info("\n======Svi korisnici spojeni u jedan niz: ukupno {}======", users.length);
        return users;
    }

    /**
     * Finds and displays students by first name through interactive search.
     * <p>
     * Performs case-insensitive search and displays all matches. Null elements are skipped.
     *
     * @param studenti the array of students to search (not null)
     * @throws NotFoundException if no student with specified name is found
     * @throws TooManyAttemptsException if input validation fails after 3 attempts
     * @throws NullPointerException if array is null
     */
    public static void findStudentByFirstName(Student[] studenti) throws NotFoundException, TooManyAttemptsException {
        String name = InputHelper.readNonEmptyString("Unesi ime studenta: ");
        boolean found = false;

        for (Student s : studenti) {
            if (s != null && s.getFirstName().equalsIgnoreCase(name)) {
                System.out.println("Pronađen: " + s.getFirstName() + " " + s.getLastName());
                logger.info("\nStudent found: {} {}", s.getFirstName(), s.getLastName());
                found = true;
            }
        }

        if (!found) throw new NotFoundException("Student nije pronađen.");
    }

    /**
     * Finds and displays professors by last name through interactive search.
     * <p>
     * Performs case-insensitive search and displays all matches. Null elements are skipped.
     *
     * @param profesori the array of professors to search (not null)
     * @throws NotFoundException if no professor with specified name is found
     * @throws TooManyAttemptsException if input validation fails after 3 attempts
     * @throws NullPointerException if array is null
     */
    public static void findProfesorByLastName(Professor[] profesori) throws NotFoundException, TooManyAttemptsException {
        String lastName = InputHelper.readNonEmptyString("Unesi prezime profesora: ");
        boolean found = false;

        for (Professor p : profesori) {
            if (p != null && p.getLastName().equalsIgnoreCase(lastName)) {
                System.out.println("Pronađen: " + p.getFirstName() + " " + p.getLastName());
                logger.info("\nProfessor found: {} {}", p.getFirstName(), p.getLastName());
                found = true;
            }
        }

        if (!found) throw new NotFoundException("Profesor nije pronađen.");
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