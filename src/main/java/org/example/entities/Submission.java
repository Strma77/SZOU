package org.example.entities;

import org.example.enums.GradeType;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a student's submission for an assignment.
 */
public class Submission {

    private final Student student;
    private final Assignment assignment;
    private final String content;
    private final LocalDateTime submittedDate;
    private Integer score;
    private String feedback;

    public Submission(Student student, Assignment assignment, String content) {
        this.student = Objects.requireNonNull(student, "Student cannot be null");
        this.assignment = Objects.requireNonNull(assignment, "Assignment cannot be null");
        this.content = content;
        this.submittedDate = LocalDateTime.now();
        this.score = null;
        this.feedback = "";
    }

    public Student getStudent() { return student; }
    public Assignment getAssignment() { return assignment; }
    public String getContent() { return content; }
    public LocalDateTime getSubmittedDate() { return submittedDate; }
    public Integer getScore() { return score; }
    public String getFeedback() { return feedback; }

    public void grade(int score, String feedback) {
        if (score < 0 || score > assignment.getMaxPoints()) {
            throw new IllegalArgumentException("Score must be between 0 and " +
                    assignment.getMaxPoints());
        }
        this.score = score;
        this.feedback = feedback != null ? feedback : "";
    }

    public boolean isGraded() { return score != null; }
    public boolean isLate() { return submittedDate.isAfter(assignment.getDueDate()); }
    public double getPercentage() { if (score == null) return -1; return (score * 100.0) / assignment.getMaxPoints(); }
    public GradeType getLetterGrade() { if (score == null) return GradeType.NOT_GRADED; return GradeType.fromScore((int) getPercentage()); }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Submission that)) return false;
        return student.equals(that.student) && assignment.equals(that.assignment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, assignment);
    }

    @Override
    public String toString() {
        String gradeInfo = isGraded()
                ? String.format("Score: %d/%d (%.1f%%) - %s", score, assignment.getMaxPoints(),
                getPercentage(), getLetterGrade())
                : "Not graded yet";

        return String.format("Submission by %s %s | %s | %s %s",
                student.getFirstName(), student.getLastName(),
                assignment.getTitle(),
                gradeInfo,
                isLate() ? "[LATE]" : ""
        );
    }
}