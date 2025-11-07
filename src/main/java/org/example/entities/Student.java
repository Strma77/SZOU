package org.example.entities;

import org.example.exceptions.DuplicateEnrollmentException;
import org.example.exceptions.LimitExceededException;
import org.example.exceptions.NegativeValueException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a student with course enrollment capabilities.
 * <p>
 * Extends {@link User} with enrolled courses tracking and enrollment limit enforcement.
 * Uses the Builder pattern inherited from {@link User.UserBuilder}.
 */
public class Student extends User{

    private final Set<String> enrolledCourses;
    private final int maxCourses;

    /**
     * Constructs a student from the provided builder.
     *
     * @param builder the builder containing student data (not null)
     */
    protected Student(StudentBuilder builder){
        super(builder);
        this.enrolledCourses = new HashSet<>();
        this.maxCourses = builder.maxCourses;
    }

    /**
     * Enrolls the student in a course by adding the course name to enrolled courses.
     * <p>
     * No duplicate checking is performed - only enforces maximum course limit.
     *
     * @param courseName the name of the course to enroll in (not null)
     * @throws DuplicateEnrollmentException if student has reached maximum course limit
     */
    public void enrollCourses(String courseName){
        if(enrolledCourses.contains(courseName)) throw new DuplicateEnrollmentException("Student " + getFirstName() + " " + getLastName() + " is already enrolled in the course: " + courseName);
        if(enrolledCourses.size() >= maxCourses) throw new LimitExceededException("Student " + getFirstName() + " " + getLastName() + " has reached the maximum number of courses (" + maxCourses + ")!");
        enrolledCourses.add(courseName);
    }

    /**
     * Returns the list of enrolled course names.
     *
     * @return read-only list of course names
     */
    public Set<String> getEnrolledCourses(){ return Collections.unmodifiableSet(enrolledCourses); }

    /**
     * Returns the current number of enrolled courses.
     *
     * @return count of enrolled courses
     */
    public int getCourseCount(){ return enrolledCourses.size();}

    /**
     * Builder class for constructing {@link Student} instances.
     * <p>
     * Extends {@link User.UserBuilder} and adds enrollment limit configuration.
     */
    public static class StudentBuilder extends User.UserBuilder{
        private int maxCourses = 5; // safe default

        /**
         * Constructs a builder with required student fields.
         *
         * @param firstName the student's first name (not null)
         * @param lastName the student's last name (not null)
         * @param ID the student's unique identifier
         */
        public StudentBuilder(String firstName, String lastName, int ID){ super(firstName, lastName, ID); }

        @Override
        public StudentBuilder username(String username) {
            super.username(username);
            return this;
        }

        @Override
        public StudentBuilder password(String passwd) {
            super.password(passwd);
            return this;
        }

        @Override
        public StudentBuilder email(String email) {
            super.email(email);
            return this;
        }

        /**
         * Sets the maximum allowed course enrollments per Student.
         *
         * @param maxCourses the maximum course limit (must be positive)
         * @return this builder for method chaining
         * @throws NegativeValueException if maxCourses is zero or negative
         */
        public StudentBuilder maxCourses(int maxCourses){
            if (maxCourses <= 0 ) throw new NegativeValueException("maxCourses variable must be a positive number!");
            this.maxCourses = maxCourses;
            return this;
        }

        /**
         * Builds and returns a new {@link Student} instance.
         *
         * @return newly constructed student
         */
        @Override
        public Student build(){ return new Student(this); }
    }
}