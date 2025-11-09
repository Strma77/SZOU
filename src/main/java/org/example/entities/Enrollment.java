package org.example.entities;

import org.example.enums.EnrollmentStatus;
import org.example.enums.GradeType;
import org.example.enums.Semester;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a student's enrollment in a specific course for a given semester.
 */
public record Enrollment(
        Student student,
        Course course,
        Semester semester,
        EnrollmentStatus status,
        GradeType grade,
        LocalDateTime enrollmentDate,
        LocalDateTime completionDate
) {
    public Enrollment {
        Objects.requireNonNull(student, "Student cannot be null");
        Objects.requireNonNull(course, "Course cannot be null");
        Objects.requireNonNull(semester, "Semester cannot be null");
        Objects.requireNonNull(status, "Status cannot be null");
        Objects.requireNonNull(grade, "Grade cannot be null");
        Objects.requireNonNull(enrollmentDate, "Enrollment date cannot be null");
    }

    public Enrollment(Student student, Course course, Semester semester) {
        this(student, course, semester, EnrollmentStatus.ACTIVE,
                GradeType.NOT_GRADED, LocalDateTime.now(), null);
    }

    public Enrollment withStatus(EnrollmentStatus newStatus) {
        LocalDateTime completion = (newStatus == EnrollmentStatus.COMPLETED ||
                newStatus == EnrollmentStatus.FAILED)
                ? LocalDateTime.now()
                : this.completionDate;
        return new Enrollment(student, course, semester, newStatus, grade,
                enrollmentDate, completion);
    }

    public Enrollment withGrade(GradeType newGrade) {
        EnrollmentStatus newStatus = switch (newGrade) {
            case F -> EnrollmentStatus.FAILED;
            case INCOMPLETE, NOT_GRADED -> this.status;
            default -> newGrade.isPassing() ? EnrollmentStatus.COMPLETED : EnrollmentStatus.FAILED;
        };

        LocalDateTime completion = (newStatus == EnrollmentStatus.COMPLETED ||
                newStatus == EnrollmentStatus.FAILED)
                ? LocalDateTime.now()
                : this.completionDate;

        return new Enrollment(student, course, semester, newStatus, newGrade,
                enrollmentDate, completion);
    }

    public boolean isActive() { return status.isActive(); }
    public boolean isPassed() { return status == EnrollmentStatus.COMPLETED && grade.isPassing(); }
    @Override
    public String toString() {
        return String.format("%s %s | %s | %s | %s | Grade: %s",
                student.getFirstName(),
                student.getLastName(),
                course.getName(),
                semester,
                status,
                grade == GradeType.NOT_GRADED ? "Not yet graded" : grade.name().replace("_", "+")
        );
    }
}