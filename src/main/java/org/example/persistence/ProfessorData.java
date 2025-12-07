package org.example.persistence;
import java.util.List;
public class ProfessorData {
    public int id;
    public String firstName;
    public String lastName;
    public String username;
    public String password;
    public String email;
    public int maxCourses;
    public List<String> teachingCourses;

    public ProfessorData(int id, String firstName, String lastName, String username,
                         String password, String email, int maxCourses,
                         List<String> teachingCourses) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.maxCourses = maxCourses;
        this.teachingCourses = teachingCourses;
    }

    public ProfessorData() {}
}
