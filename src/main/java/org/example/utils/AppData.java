package org.example.utils;

import org.example.entities.*;
import java.util.*;

/**
 * Container for all application data.
 */
public class AppData {
    public final Set<Student> students;
    public final Set<Professor> professors;
    public final List<Course> courses;
    public final List<Enrollment> enrollments;
    public final List<User> users;

    public AppData(Set<Student> students, Set<Professor> professors,
                   List<Course> courses, List<Enrollment> enrollments) {
        this.students = students;
        this.professors = professors;
        this.courses = courses;
        this.enrollments = enrollments;
        this.users = new ArrayList<>();
        this.users.addAll(professors);
        this.users.addAll(students);
    }

    public void refreshUsers() {
        users.clear();
        users.addAll(professors);
        users.addAll(students);
    }
}