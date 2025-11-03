package org.example.entities;

import org.example.exceptions.DuplicateEnrollmentException;
import org.example.exceptions.NegativeValueException;

/**
 * Represents a student with course enrollment capabilities.
 * <p>
 * Extends {@link User} with enrolled courses tracking and enrollment limit enforcement.
 * Uses the Builder pattern inherited from {@link User.UserBuilder}.
 */
public class Student extends User{

    private String[] enrolledCourses;
    private int courseCount = 0;

    /**
     * Constructs a student from the provided builder.
     *
     * @param builder the builder containing student data (not null)
     */
    protected Student(StudentBuilder builder){
        super(builder);
        this.enrolledCourses = new String[builder.maxCourses];
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
        if (courseCount < enrolledCourses.length) enrolledCourses[courseCount++] = courseName;
        else { throw new DuplicateEnrollmentException("Student " + getFirstName() + " " + getLastName() + " je dostigao limit tečajeva!") ; }
    }

    /**
     * Returns the array of enrolled course names.
     *
     * @return array of course names (may contain null elements)
     */
    public String[] getEnrolledCourses(){ return enrolledCourses; }

    /**
     * Returns the number of courses currently enrolled.
     *
     * @return count of enrolled courses
     */
    public int getCourseCount(){ return courseCount; }

    /**
     * Builder class for constructing {@link Student} instances.
     * <p>
     * Extends {@link User.UserBuilder} and adds maxCourses configuration.
     */
    public static class StudentBuilder extends User.UserBuilder{
        private int maxCourses;

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
         * Sets the maximum number of courses the student can enroll in.
         *
         * @param maxCourses the maximum course limit (must be positive)
         * @return this builder for method chaining
         * @throws NegativeValueException if maxCourses is zero or negative
         */
        public StudentBuilder maxCourses(int maxCourses){
            if (maxCourses <= 0 ) throw new NegativeValueException("Broj tečajeva mora biti pozitivan!");
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