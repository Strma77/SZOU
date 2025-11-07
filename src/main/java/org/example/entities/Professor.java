package org.example.entities;

import org.example.exceptions.DuplicateEnrollmentException;
import org.example.exceptions.LimitExceededException;
import org.example.exceptions.NegativeValueException;

import java.util.*;

/**
 * Represents a professor with course teaching capabilities.
 * <p>
 * Extends {@link User} with teaching courses tracking and course limit enforcement.
 * Uses the Builder pattern inherited from {@link User.UserBuilder}.
 */
public class Professor extends User {

    private final Set<String> teachingCourses;
    private final int maxCourses;

    /**
     * Constructs a professor from the provided builder.
     *
     * @param builder the builder containing professor data (not null)
     */
    protected Professor(ProfessorBuilder builder){
        super(builder);
        this.teachingCourses = new HashSet<>();
        this.maxCourses = builder.maxCourses;
    }

    /**
     * Adds a course to the professor's teaching list.
     * <p>
     * If maximum course limit is reached, prints a warning message but does not throw exception.
     *
     * @param courseName the name of the course to add (not null)
     */
    public void addCourse(String courseName) {
        if(teachingCourses.contains(courseName)) throw new DuplicateEnrollmentException("Professor " + getFirstName() + " " + getLastName() + " is already teaching in the course: " + courseName);
        if(teachingCourses.size() >= maxCourses) throw new LimitExceededException("Professor " + getFirstName() + " " + getLastName() + " has reached the maximum number of courses (" + maxCourses + ")!");
        teachingCourses.add(courseName);
    }

    /**
     * Returns the list of teaching course names.
     *
     * @return read-only list of course names
     */
    public Set<String> getTeachingCourses() { return Collections.unmodifiableSet(teachingCourses); }

    /**
     * Builder class for constructing {@link Professor} instances.
     * <p>
     * Extends {@link User.UserBuilder} and adds maxCourses configuration.
     */
    public static class ProfessorBuilder extends User.UserBuilder {
        private int maxCourses;

        /**
         * Constructs a builder with required professor fields.
         *
         * @param firstName the professor's first name (not null)
         * @param lastName the professor's last name (not null)
         * @param ID the professor's unique identifier
         */
        public ProfessorBuilder(String firstName, String lastName, int ID) { super(firstName, lastName, ID); }

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

        /**
         * Sets the maximum number of courses the professor can teach.
         *
         * @param maxCourses the maximum course limit
         * @return this builder for method chaining
         */
        public ProfessorBuilder maxCourses(int maxCourses) {
            if (maxCourses <= 0 ) throw new NegativeValueException("maxCourses variable must be a positive number!");
            this.maxCourses = maxCourses;
            return this;
        }

        /**
         * Builds and returns a new {@link Professor} instance.
         *
         * @return newly constructed professor
         */
        @Override
        public Professor build() { return new Professor(this); }
    }
}