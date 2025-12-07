package org.example.persistence;

import java.util.List;
import java.util.Map;

public class StudentData {
    public int id;
    public String firstName;
    public String lastName;
    public String username;
    public String password;
    public String email;
    public int maxCourses;
    public List<String> enrolledCourses;
    public Map<String, String> courseGrades;

    public StudentData(int id, String firstName, String lastName, String username,
                       String password, String email, int maxCourses,
                       List<String> enrolledCourses, Map<String, String> courseGrades) {
        this.id = id;
        this.firstName =firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.maxCourses = maxCourses;
        this.enrolledCourses = enrolledCourses;
        this.courseGrades = courseGrades;
    }

    public StudentData(){}
}
