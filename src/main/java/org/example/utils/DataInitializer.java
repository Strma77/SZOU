package org.example.utils;

import org.example.entities.*;
import org.example.exceptions.TooManyAttemptsException;
import org.example.persistence.*;
import org.example.services.*;

import java.util.*;

/**
 * Handles initial data loading and creation.
 */
public class DataInitializer {

    public static AppData loadOrCreateData() throws TooManyAttemptsException {
        Set<Professor> professors = JsonLoader.loadProfessors();
        Set<Student> students = JsonLoader.loadStudents();
        List<Course> courses = JsonLoader.loadCourse(professors);
        JsonLoader.loadLessons(courses);
        List<Enrollment> enrollments = JsonLoader.loadEnrollments(students, courses);

        if (students.isEmpty() && professors.isEmpty() && courses.isEmpty()) {
            System.out.println("\n🆕 No existing data found. Let's create some!\n");
            ActivityLogger.log("First run - creating initial data");
            createInitialData(students, professors, courses, enrollments);
        } else {
            printLoadSummary(students, professors, courses, enrollments);
            ActivityLogger.log("Data loaded from JSON files");
        }

        return new AppData(students, professors, courses, enrollments);
    }

    private static void createInitialData(Set<Student> students, Set<Professor> professors,
                                          List<Course> courses, List<Enrollment> enrollments)
            throws TooManyAttemptsException {

        createProfessors(professors);
        createStudents(students);

        List<User> users = new ArrayList<>();
        users.addAll(professors);
        users.addAll(students);

        int cNum = InputHelper.readPositiveInt("How many courses?: ");
        courses.addAll(CourseService.createCourses(cNum, users));

        enrollments.addAll(EnrollmentService.enrollStudents(students, courses));
        List<Enrollment> graded = GradingService.assignRandomGrades(enrollments);
        enrollments.clear();
        enrollments.addAll(graded);

        JsonSaver.saveAll(students, professors, courses, enrollments);
        System.out.println("✅ Data saved successfully!\n");
    }

    private static void createProfessors(Set<Professor> professors)
            throws TooManyAttemptsException {
        int profNum = InputHelper.readPositiveInt("How many professors?: ");
        for (int i = 0; i < profNum; i++) {
            String firstName = InputHelper.readNonEmptyString("Professor name: ");
            String lastName = InputHelper.readNonEmptyString("Professor surname: ");
            int maxC = InputHelper.readPositiveInt("Max courses: ");

            int id = 10 + i;
            String username = (firstName.charAt(0) + lastName).toLowerCase();

            professors.add(new Professor.ProfessorBuilder(firstName, lastName, id)
                    .username(username).password(id + "123")
                    .email(username + id + "@profuni.hr").maxCourses(maxC).build());
        }
    }

    private static void createStudents(Set<Student> students)
            throws TooManyAttemptsException {
        int studNum = InputHelper.readPositiveInt("How many students?: ");
        for (int i = 0; i < studNum; i++) {
            String firstName = InputHelper.readNonEmptyString("Student name: ");
            String lastName = InputHelper.readNonEmptyString("Student surname: ");
            int maxC = InputHelper.readPositiveInt("Max courses: ");

            int id = 100 + i;
            String username = (firstName.charAt(0) + lastName).toLowerCase();

            students.add(new Student.StudentBuilder(firstName, lastName, id)
                    .username(username).password(id + "456")
                    .email(username + id + "@studuni.hr").maxCourses(maxC).build());
        }
    }

    private static void printLoadSummary(Set<Student> students, Set<Professor> professors,
                                         List<Course> courses, List<Enrollment> enrollments) {
        System.out.println("\n✅ Loaded existing data:");
        System.out.println("   📚 " + students.size() + " students");
        System.out.println("   👨‍🏫 " + professors.size() + " professors");
        System.out.println("   📖 " + courses.size() + " courses");
        System.out.println("   📝 " + enrollments.size() + " enrollments\n");
    }
}