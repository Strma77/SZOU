package org.example.entities;

import org.example.enums.GradeType;
import org.example.enums.Role;
import org.example.exceptions.DuplicateEnrollmentException;
import org.example.exceptions.LimitExceededException;
import org.example.exceptions.NegativeValueException;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Represents a student with course enrollment capabilities and GPA tracking.
 */
public class Student extends User implements Serializable {
    @Serial
    private static final long serialVersionUID = 3L;

    private final Set<String> enrolledCourses;
    private final int maxCourses;
    private final Map<String, GradeType> courseGrades;

    protected Student(StudentBuilder builder){
        super(builder);
        this.enrolledCourses = new LinkedHashSet<>();
        this.maxCourses = builder.maxCourses;
        this.courseGrades = new HashMap<>();
    }

    public void enrollCourses(String courseName){
        if(enrolledCourses.contains(courseName))
            throw new DuplicateEnrollmentException("Student " + getFirstName() + " " +
                    getLastName() + " is already enrolled in the course: " + courseName);

        if(enrolledCourses.size() >= maxCourses)
            throw new LimitExceededException("Student " + getFirstName() + " " +
                    getLastName() + " has reached the maximum number of courses (" + maxCourses + ")!");

        enrolledCourses.add(courseName);
        courseGrades.put(courseName, GradeType.NOT_GRADED);
    }

    public void setGrade(String courseName, GradeType grade) {
        if (!enrolledCourses.contains(courseName)) {
            throw new IllegalArgumentException("Student is not enrolled in course: " + courseName);
        }
        courseGrades.put(courseName, grade);
    }

    public GradeType getGrade(String courseName) {
        return courseGrades.getOrDefault(courseName, GradeType.NOT_GRADED);
    }

    public double calculateGPA() {
        List<GradeType> validGrades = courseGrades.values().stream()
                .filter(g -> g != GradeType.NOT_GRADED && g != GradeType.INCOMPLETE)
                .toList();

        if (validGrades.isEmpty()) return 0.0;

        double sum = validGrades.stream()
                .mapToDouble(GradeType::getGradePoint)
                .sum();

        return sum / validGrades.size();
    }

    public Set<String> getEnrolledCourses(){
        return Collections.unmodifiableSet(enrolledCourses);
    }

    public Map<String, GradeType> getCourseGrades() {
        return Collections.unmodifiableMap(courseGrades);
    }

    public int getCourseCount(){ return enrolledCourses.size(); }

    public int getMaxCourses(){ return maxCourses; }

    public static class StudentBuilder extends User.UserBuilder {
        private int maxCourses = 5;

        public StudentBuilder(String firstName, String lastName, int ID){
            super(firstName, lastName, ID);
            this.role = Role.STUDENT;
        }

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

        public StudentBuilder maxCourses(int maxCourses){
            if (maxCourses <= 0)
                throw new NegativeValueException("maxCourses variable must be a positive number!");
            this.maxCourses = maxCourses;
            return this;
        }

        @Override
        public Student build(){ return new Student(this); }
    }
}