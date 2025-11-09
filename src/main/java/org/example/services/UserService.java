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
 * Includes sorting with Comparators and stream operations.
 */
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static int maxCoursesOvr = 0;

    public static Set<Professor> createProfessors() throws TooManyAttemptsException {
        Set<Professor> professors = new LinkedHashSet<>();
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
            logger.info("\nStudent input #{}", i + 1);
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
        maxCoursesOvr = students.stream()
                .mapToInt(Student::getCourseCount)
                .sum();
        return students;
    }

    public static List<User> mergeUsers(Set<Professor> professors, Set<Student> students) {
        List<User> users = Stream.concat(professors.stream(), students.stream())
                .collect(Collectors.toCollection(ArrayList::new));
        logger.info("\n======All users connected in one list: overall {}======", users.size());
        return users;
    }

    /**
     * Sorts students by first name (alphabetically).
     */
    public static List<Student> sortStudentsByName(Collection<Student> students) {
        return students.stream()
                .sorted(Comparator.comparing(Student::getFirstName)
                        .thenComparing(Student::getLastName))
                .toList();
    }

    /**
     * Sorts students by GPA (descending - highest first).
     */
    public static List<Student> sortStudentsByGPA(Collection<Student> students) {
        return students.stream()
                .sorted(Comparator.comparingDouble(Student::calculateGPA).reversed())
                .toList();
    }

    /**
     * Sorts students by number of enrolled courses (descending).
     */
    public static List<Student> sortStudentsByCourseCount(Collection<Student> students) {
        return students.stream()
                .sorted(Comparator.comparingInt(Student::getCourseCount).reversed())
                .toList();
    }

    /**
     * Sorts professors by last name (alphabetically).
     */
    public static List<Professor> sortProfessorsByName(Collection<Professor> professors) {
        return professors.stream()
                .sorted(Comparator.comparing(Professor::getLastName)
                        .thenComparing(Professor::getFirstName))
                .toList();
    }

    /**
     * Groups students by their role (always STUDENT, but demonstrates groupingBy).
     * More useful: groups by course count ranges.
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
     * Partitions students by GPA: passing (>= 2.0) vs failing (< 2.0).
     */
    public static Map<Boolean, List<Student>> partitionStudentsByGPA(
            Collection<Student> students) {
        return students.stream()
                .collect(Collectors.partitioningBy(s -> s.calculateGPA() >= 2.0));
    }

    /**
     * Groups professors by number of courses they teach.
     */
    public static Map<Integer, List<Professor>> groupProfessorsByCourseCount(
            Collection<Professor> professors) {
        return professors.stream()
                .collect(Collectors.groupingBy(Professor::getCourseCount));
    }

    /**
     * Gets the first student (by insertion order).
     */
    public static Student getFirstStudent(Set<Student> students) {
        if (students instanceof SequencedSet<Student> seq) {
            return seq.getFirst();
        }
        return students.stream().findFirst().orElse(null);
    }

    /**
     * Gets the last student (by insertion order).
     */
    public static Student getLastStudent(Set<Student> students) {
        if (students instanceof SequencedSet<Student> seq) {
            return seq.getLast();
        }
        return students.stream().reduce((first, second) -> second).orElse(null);
    }

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

    public static int getMaxCoursesOvr(){
        return maxCoursesOvr;
    }
}