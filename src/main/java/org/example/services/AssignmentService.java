package org.example.services;

import org.example.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Service for managing assignments and submissions.
 */
public class AssignmentService {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentService.class);

    /**
     * Creates a new assignment for a course.
     */
    public static Assignment createAssignment(String title, String description,
                                              Course course, int maxPoints, LocalDateTime dueDate) {
        Assignment assignment = new Assignment(title, description, course, maxPoints, dueDate);
        logger.info("Assignment created: {} for course {}", title, course.getName());
        return assignment;
    }

    /**
     * Submits an assignment for a student.
     */
    public static Submission submitAssignment(Student student, Assignment assignment,
                                              String content) {
        Submission submission = new Submission(student, assignment, content);
        logger.info("Student {} {} submitted assignment {}",
                student.getFirstName(), student.getLastName(), assignment.getTitle());
        return submission;
    }

    /**
     * Grades a submission.
     */
    public static void gradeSubmission(Submission submission, int score, String feedback) {
        submission.grade(score, feedback);
        logger.info("Submission graded: {} {} for {} - Score: {}/{}",
                submission.getStudent().getFirstName(),
                submission.getStudent().getLastName(),
                submission.getAssignment().getTitle(),
                score,
                submission.getAssignment().getMaxPoints());
    }

    /**
     * Groups submissions by assignment.
     */
    public static Map<Assignment, List<Submission>> groupSubmissionsByAssignment(
            Collection<Submission> submissions) {
        return submissions.stream()
                .collect(Collectors.groupingBy(Submission::getAssignment));
    }

    /**
     * Partitions submissions by grading status.
     */
    public static Map<Boolean, List<Submission>> partitionSubmissionsByGraded(
            Collection<Submission> submissions) {
        return submissions.stream()
                .collect(Collectors.partitioningBy(Submission::isGraded));
    }

    /**
     * Partitions submissions by late status.
     */
    public static Map<Boolean, List<Submission>> partitionSubmissionsByLate(
            Collection<Submission> submissions) {
        return submissions.stream()
                .collect(Collectors.partitioningBy(Submission::isLate));
    }

    /**
     * Finds overdue assignments.
     */
    public static List<Assignment> findOverdueAssignments(Collection<Assignment> assignments) {
        return assignments.stream()
                .filter(Assignment::isOverdue)
                .sorted(Comparator.comparing(Assignment::getDueDate))
                .collect(Collectors.toList());
    }

    /**
     * Calculates average score for an assignment.
     */
    public static double calculateAverageScore(Assignment assignment,
                                               Collection<Submission> submissions) {
        return submissions.stream()
                .filter(s -> s.getAssignment().equals(assignment))
                .filter(Submission::isGraded)
                .mapToDouble(Submission::getPercentage)
                .average()
                .orElse(0.0);
    }

    /**
     * Finds top performers on an assignment.
     */
    public static List<Submission> findTopSubmissions(Assignment assignment,
                                                      Collection<Submission> submissions, int limit) {
        return submissions.stream()
                .filter(s -> s.getAssignment().equals(assignment))
                .filter(Submission::isGraded)
                .sorted(Comparator.comparingDouble(Submission::getPercentage).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public static Optional<Submission> findTopSubmission(Collection<Submission> submissions) {
        return submissions.stream()
                .filter(Submission::isGraded)
                .max(Comparator.comparingDouble(Submission::getPercentage));
    }

    public static <T extends Submission> List<T> filterSubmissions(
            Collection<? extends T> submissions,
            Predicate<? super T> predicate) {
        return submissions.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public static List<Submission> filterSubmissionsByMinScore(
            Collection<Submission> submissions,
            int minScore) {
        return filterSubmissions(submissions,
                s -> s.getScore().map(score -> score >= minScore).orElse(false));
    }

    public static List<Submission> filterSubmissionsByStudent(
            Collection<Submission> submissions,
            Student student) {
        return filterSubmissions(submissions,
                s -> s.getStudent().equals(student));
    }

    public static List<Integer> getAllGradedScores(Collection<Submission> submissions) {
        return submissions.stream()
                .map(Submission::getScore)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public static double calculatePassRate(Collection<Submission> submissions, int passingScore) {
        long total = submissions.stream()
                .filter(Submission::isGraded)
                .count();

        if (total == 0) return 0.0;

        long passed = submissions.stream()
                .filter(Submission::isGraded)
                .filter(s -> s.getScore()
                        .map(score -> score >= passingScore)
                        .orElse(false))
                .count();

        return (passed * 100.0) / total;
    }

    public static <T> long countMatching(Collection<? extends T> elements,
                                         Predicate<? super T> predicate) {
        return elements.stream()
                .filter(predicate)
                .count();
    }

    public static long countLateSubmissions(Collection<Submission> submissions) {
        return countMatching(submissions, Submission::isLate);
    }

    public static <T, K> Map<K, List<T>> groupBy(
            Collection<? extends T> elements,
            Function<? super T, ? extends K> classifier) {
        return elements.stream()
                .collect(Collectors.groupingBy(classifier));
    }

    public static Map<String, List<Submission>> groupSubmissionsByLetterGrade(
            Collection<Submission> submissions) {
        return groupBy(submissions, s -> s.getLetterGrade().name());
    }
}