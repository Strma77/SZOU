package org.example.entities;

import org.example.enums.Role;
import org.example.exceptions.DuplicateEnrollmentException;
import org.example.exceptions.LimitExceededException;
import org.example.exceptions.NegativeValueException;

import java.util.*;

/**
 * Represents a professor with course teaching capabilities.
 */
public class Professor extends User {

    private final Set<String> teachingCourses;
    private final int maxCourses;

    protected Professor(ProfessorBuilder builder){
        super(builder);
        this.teachingCourses = new LinkedHashSet<>();
        this.maxCourses = builder.maxCourses;
    }

    public void addCourse(String courseName) {
        if(teachingCourses.contains(courseName))
            throw new DuplicateEnrollmentException("Professor " + getFirstName() + " " +
                    getLastName() + " is already teaching in the course: " + courseName);

        if(teachingCourses.size() >= maxCourses)
            throw new LimitExceededException("Professor " + getFirstName() + " " +
                    getLastName() + " has reached the maximum number of courses (" + maxCourses + ")!");

        teachingCourses.add(courseName);
    }

    public Set<String> getTeachingCourses() {
        return Collections.unmodifiableSet(teachingCourses);
    }

    public int getCourseCount() {
        return teachingCourses.size();
    }

    public static class ProfessorBuilder extends User.UserBuilder {
        private int maxCourses;

        public ProfessorBuilder(String firstName, String lastName, int ID) {
            super(firstName, lastName, ID);
            this.role = Role.PROFESSOR;
        }

        @Override
        public ProfessorBuilder username(String username) {
            super.username(username);
            return this;
        }

        @Override
        public ProfessorBuilder password(String passwd) {
            super.password(passwd);
            return this;
        }

        @Override
        public ProfessorBuilder email(String email) {
            super.email(email);
            return this;
        }

        public ProfessorBuilder maxCourses(int maxCourses) {
            if (maxCourses <= 0)
                throw new NegativeValueException("maxCourses variable must be a positive number!");
            this.maxCourses = maxCourses;
            return this;
        }

        @Override
        public Professor build() { return new Professor(this); }
    }
}