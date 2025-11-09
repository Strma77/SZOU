package org.example.enums;

/**
 * Represents letter grades with corresponding grade points for GPA calculation.
 */
public enum GradeType {
    A_PLUS(5.0, "Excellent", 90, 100),
    A(4.5, "Very Good", 80, 89),
    B(3.5, "Good", 70, 79),
    C(2.5, "Satisfactory", 60, 69),
    D(1.5, "Sufficient", 50, 59),
    F(0.0, "Fail", 0, 49),
    INCOMPLETE(-1.0, "Incomplete", -1, -1),
    NOT_GRADED(-1.0, "Not Yet Graded", -1, -1);

    private final double gradePoint;
    private final String description;
    private final int minScore;
    private final int maxScore;

    GradeType(double gradePoint, String description, int minScore, int maxScore) {
        this.gradePoint = gradePoint;
        this.description = description;
        this.minScore = minScore;
        this.maxScore = maxScore;
    }

    public double getGradePoint() { return gradePoint; }
    public String getDescription() { return description; }
    public boolean isPassing() { return gradePoint >= 1.5 && gradePoint <= 5.0; }
    /**
     * Converts a numeric score (0-100) to a letter grade.
     */
    public static GradeType fromScore(int score) {
        if (score < 0 || score > 100) throw new IllegalArgumentException("Score must be between 0 and 100");
        for (GradeType grade : values()) if (grade.minScore != -1 && score >= grade.minScore && score <= grade.maxScore) return grade;
        return NOT_GRADED;
    }
    @Override
    public String toString() { return name().replace("_", "+") + " (" + description + ")"; }
}