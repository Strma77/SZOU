package org.example.services;

import org.example.entities.Enrollment;
import org.example.entities.Student;
import org.example.enums.GradeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for managing grades and GPA calculations.
 */
public class GradingService {

    private static final Logger logger = LoggerFactory.getLogger(GradingService.class);

    /**
     * Assigns a grade to a student for a specific course.
     */
    public static void gradeStudent(Student student, String courseName, GradeType grade) {
        student.setGrade(courseName, grade);
        logger.info("Student {} {} received grade {} for course {}",
                student.getFirstName(), student.getLastName(), grade, courseName);
    }

    /**
     * Assigns a grade to an enrollment (creates new enrollment with grade).
     */
    public static Enrollment gradeEnrollment(Enrollment enrollment, GradeType grade) {
        // Update the student's grade map FIRST
        enrollment.student().setGrade(enrollment.course().getName(), grade);

        // Then create new enrollment with the grade
        Enrollment graded = enrollment.withGrade(grade);

        logger.info("Enrollment graded: {} {} in {} - Grade: {}",
                enrollment.student().getFirstName(),
                enrollment.student().getLastName(),
                enrollment.course().getName(),
                grade);
        return graded;
    }

    /**
     * Assigns random grades to all enrollments (for testing purposes).
     * IMPORTANT: This updates both the Enrollment records AND the Student grade maps.
     */
    public static List<Enrollment> assignRandomGrades(List<Enrollment> enrollments) {
        Random random = new Random();
        List<GradeType> grades = Arrays.asList(
                GradeType.A_PLUS, GradeType.A, GradeType.B,
                GradeType.C, GradeType.D, GradeType.F
        );

        List<Enrollment> gradedEnrollments = new ArrayList<>();

        for (Enrollment e : enrollments) {
            GradeType randomGrade = grades.get(random.nextInt(grades.size()));

            // Update student's grade map
            e.student().setGrade(e.course().getName(), randomGrade);

            // Create new enrollment with grade
            Enrollment graded = e.withGrade(randomGrade);
            gradedEnrollments.add(graded);

            logger.debug("Assigned grade {} to {} {} for {}",
                    randomGrade,
                    e.student().getFirstName(),
                    e.student().getLastName(),
                    e.course().getName());
        }

        return gradedEnrollments;
    }

    /**
     * Calculates average GPA for a collection of students.
     */
    public static double calculateAverageGPA(Collection<Student> students) {
        return students.stream()
                .mapToDouble(Student::calculateGPA)
                .filter(gpa -> gpa > 0.0)
                .average()
                .orElse(0.0);
    }

    /**
     * Groups students by GPA range.
     */
    public static Map<String, List<Student>> groupStudentsByGPARange(
            Collection<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(s -> {
                    double gpa = s.calculateGPA();
                    if (gpa >= 4.5) return "Excellent (4.5-5.0)";
                    if (gpa >= 3.5) return "Very Good (3.5-4.4)";
                    if (gpa >= 2.5) return "Good (2.5-3.4)";
                    if (gpa >= 2.0) return "Satisfactory (2.0-2.4)";
                    if (gpa > 0.0) return "Poor (<2.0)";
                    return "Not Graded";
                }));
    }

    /**
     * Finds students with GPA above a threshold.
     */
    public static List<Student> findHonorStudents(Collection<Student> students,
                                                  double minGPA) {
        return students.stream()
                .filter(s -> s.calculateGPA() >= minGPA)
                .sorted(Comparator.comparingDouble(Student::calculateGPA).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Finds students at risk (GPA below threshold).
     */
    public static List<Student> findAtRiskStudents(Collection<Student> students,
                                                   double maxGPA) {
        return students.stream()
                .filter(s -> {
                    double gpa = s.calculateGPA();
                    return gpa > 0.0 && gpa < maxGPA;
                })
                .sorted(Comparator.comparingDouble(Student::calculateGPA))
                .collect(Collectors.toList());
    }

    /**
     * Generates a grade distribution report.
     */
    public static Map<GradeType, Long> getGradeDistribution(
            Collection<Enrollment> enrollments) {
        return enrollments.stream()
                .collect(Collectors.groupingBy(
                        Enrollment::grade,
                        Collectors.counting()
                ));
    }
}