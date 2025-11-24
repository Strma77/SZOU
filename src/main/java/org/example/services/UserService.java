package org.example.services;

import org.example.entities.*;
import org.example.exceptions.*;
import org.example.utils.InputHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service for managing users (professors and students).
 */
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public static Set<Professor> createProfessors() throws TooManyAttemptsException {
        Set<Professor> professors = new LinkedHashSet<>();
        int profNum = InputHelper.readPositiveInt("How many professors would you like to input?: ");

        for (int i = 0; i < profNum; i++) {
            logger.info("Professor input #{}", i + 1);
            String firstName = InputHelper.readNonEmptyString("Name: ");
            String lastName = InputHelper.readNonEmptyString("Surname: ");
            int ID = 10 + i;
            String username = (firstName.charAt(0) + lastName).toLowerCase();
            String email = username + ID + "@profuni.hr";
            String passwd = ID + "123";
            int maxC = InputHelper.readPositiveInt("How many courses is this teacher teaching?: ");

            professors.add(new Professor.ProfessorBuilder(firstName, lastName, ID)
                    .username(username)
                    .password(passwd)
                    .email(email)
                    .maxCourses(maxC)
                    .build());
        }
        return professors;
    }

    public static Set<Student> createStudents() throws TooManyAttemptsException {
        Set<Student> students = new LinkedHashSet<>();
        int studNum = InputHelper.readPositiveInt("How many students would you like to input?: ");

        for (int i = 0; i < studNum; i++) {
            logger.info("Student input #{}", i + 1);
            String firstName = InputHelper.readNonEmptyString("Name: ");
            String lastName = InputHelper.readNonEmptyString("Surname: ");
            int ID = 100 + i;
            String username = (firstName.charAt(0) + lastName).toLowerCase();
            String email = username + ID + "@studuni.hr";
            String passwd = ID + "456";
            int maxC = InputHelper.readPositiveInt("How many courses is this student taking?: ");

            students.add(new Student.StudentBuilder(firstName, lastName, ID)
                    .username(username)
                    .password(passwd)
                    .email(email)
                    .maxCourses(maxC)
                    .build());
        }
        return students;
    }

    /**
     * Merges professors and students into one list.
     * Demonstrates PECS wildcards with Stream.concat().
     */
    public static List<User> mergeUsers(Set<Professor> professors, Set<Student> students) {
        List<User> users = Stream.concat(professors.stream(), students.stream())
                .collect(Collectors.toCollection(ArrayList::new));
        logger.info("All users merged: {} total", users.size());
        return users;
    }

    /**
     * Sorts students by GPA (descending - highest first).
     * Demonstrates Comparator and lambda expressions.
     */
    public static List<Student> sortStudentsByGPA(Collection<Student> students) {
        return students.stream()
                .sorted(Comparator.comparingDouble(Student::calculateGPA).reversed())
                .toList();
    }

    /**
     * Sorts students by name.
     */
    public static List<Student> sortStudentsByName(Collection<Student> students) {
        return students.stream()
                .sorted(Comparator.comparing(Student::getFirstName)
                        .thenComparing(Student::getLastName))
                .toList();
    }

    /**
     * Groups students by course load.
     */
    public static Map<String, List<Student>> groupStudentsByCourseLoad(
            Collection<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(s -> {
                    int count = s.getCourseCount();
                    if (count <= 2) return "Light (1-2 courses)";
                    if (count <= 4) return "Medium (3-4 courses)";
                    return "Heavy (5+ courses)";
                }));
    }

    /**
     * Finds top student by GPA.
     * Demonstrates Optional return type.
     */
    public static Optional<Student> findTopStudent(Collection<Student> students) {
        return students.stream()
                .max(Comparator.comparingDouble(Student::calculateGPA));
    }

    /**
     * Searches for student by first name.
     */
    public static void findStudentByFirstName(Collection<Student> students)
            throws NotFoundException, TooManyAttemptsException {
        String name = InputHelper.readNonEmptyString("Name of the student: ");

        List<Student> found = students.stream()
                .filter(s -> s.getFirstName().equalsIgnoreCase(name))
                .toList();

        if (found.isEmpty()) {
            throw new NotFoundException("Student not found.");
        }

        found.forEach(s -> {
            System.out.println("Found: " + s.getFirstName() + " " + s.getLastName() +
                    " (GPA: " + String.format("%.2f", s.calculateGPA()) + ")");
            logger.info("Student found: {} {}", s.getFirstName(), s.getLastName());
        });
    }

    /**
     * Searches for professor by last name.
     */
    public static void findProfessorByLastName(Collection<Professor> professors)
            throws NotFoundException, TooManyAttemptsException {
        String lastName = InputHelper.readNonEmptyString("Professor surname: ");

        List<Professor> found = professors.stream()
                .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
                .toList();

        if (found.isEmpty()) {
            throw new NotFoundException("Professor not found.");
        }

        found.forEach(p -> {
            System.out.println("Found: " + p.getFirstName() + " " + p.getLastName() +
                    " (Teaching " + p.getCourseCount() + " courses)");
            logger.info("Professor found: {} {}", p.getFirstName(), p.getLastName());
        });
    }

    /**
     * Gets first and last students from sequenced set.
     * Demonstrates SequencedSet operations.
     */
    public static Optional<Student> getFirstStudent(Set<Student> students) {
        if (students instanceof SequencedSet<Student> seq) {
            try {
                return Optional.of(seq.getFirst());
            } catch (NoSuchElementException e) {
                return Optional.empty();
            }
        }
        return students.stream().findFirst();
    }

    public static Optional<Student> getLastStudent(Set<Student> students) {
        if (students instanceof SequencedSet<Student> seq) {
            try {
                return Optional.of(seq.getLast());
            } catch (NoSuchElementException e) {
                return Optional.empty();
            }
        }
        return students.stream().reduce((first, second) -> second);
    }
}