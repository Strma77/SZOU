package org.example.persistence;

public class EnrollmentData {
    public int studentId;
    public String courseName;
    public String semester;
    public String status;
    public String grade;
    public String enrollmentDate;
    public String completionDate;

    public EnrollmentData(int studentId, String courseName, String semester,
                          String status, String grade, String enrollmentDate,
                          String completionDate) {
        this.studentId = studentId;
        this.courseName = courseName;
        this.semester = semester;
        this.status = status;
        this.grade = grade;
        this.enrollmentDate = enrollmentDate;
        this.completionDate = completionDate;
    }

    public EnrollmentData() {}
}
