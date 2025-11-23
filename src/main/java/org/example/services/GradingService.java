package org.example.services;

import org.example.entities.Enrollment;
import org.example.entities.Student;
import org.example.enums.GradeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Predicate;
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

    public static <T> List<T> filterStudents(Collection<? extends T> students,
                                             Predicate<? super T> predicate) {
        return students.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public static List<Student> filterStudentsByMinGPA(Collection<Student> students, double minGPA) {
        return filterStudents(students, s -> s.calculateGPA() >= minGPA);
    }

    public static List<Student> filterStudentsByMaxGPA(Collection<Student> students, double maxGPA) {
        return filterStudents(students, s -> s.calculateGPA() <= maxGPA);
    }

    public static List<Student> filterStudentsByGPARange(Collection<Student> students,
                                                         double minGPA, double maxGPA) {
        return filterStudents(students, s -> {
            double gpa = s.calculateGPA();
            return gpa >= minGPA && gpa <= maxGPA;
        });
    }

    public static Optional<Student> findTopStudentByGPA(Collection<Student> students) {
        return students.stream()
                .max(Comparator.comparingDouble(Student::calculateGPA));
    }

    public static Optional<Student> findLowestStudentByGPA(Collection<Student> students) {
        return students.stream()
                .filter(s -> s.calculateGPA() > 0.0)
                .min(Comparator.comparingDouble(Student::calculateGPA));
    }

    public static double calculateMedianGPA(Collection<Student> students) {
        List<Double> gpas = students.stream()
                .map(Student::calculateGPA)
                .filter(gpa -> gpa > 0.0)
                .sorted()
                .toList();

        if (gpas.isEmpty()) return 0.0;

        int size = gpas.size();
        if (size % 2 == 0) {
            return (gpas.get(size / 2 - 1) + gpas.get(size / 2)) / 2.0;
        } else {
            return gpas.get(size / 2);
        }
    }

    public static long countStudentsByGradeCategory(Collection<Student> students, String category) {
        Map<String, List<Student>> grouped = groupStudentsByGPARange(students);
        return grouped.getOrDefault(category, Collections.emptyList()).size();
    }

    public static List<Double> getAllGPAs(Collection<Student> students) {
        return students.stream()
                .map(Student::calculateGPA)
                .filter(gpa -> gpa > 0.0)
                .collect(Collectors.toList());
    }

    public static double calculateGPAStandardDeviation(Collection<Student> students) {
        List<Double> gpas = getAllGPAs(students);
        if (gpas.isEmpty()) return 0.0;

        double mean = calculateAverageGPA(students);
        double variance = gpas.stream()
                .mapToDouble(gpa -> Math.pow(gpa - mean, 2))
                .average()
                .orElse(0.0);

        return Math.sqrt(variance);
    }

    public static <T> Map<Boolean, List<T>> partitionBy(
            Collection<? extends T> elements,
            Predicate<? super T> predicate) {
        return elements.stream()
                .collect(Collectors.partitioningBy(predicate));
    }

    public static Map<Boolean, List<Student>> partitionStudentsByPassingGPA(
            Collection<Student> students, double passingGPA) {
        return partitionBy(students, s -> s.calculateGPA() >= passingGPA);
    }

    public static <T extends Comparable<T>> List<T> sortDescending(Collection<? extends T> elements) {
        return elements.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public static boolean anyStudentAboveGPA(Collection<Student> students, double threshold) {
        return students.stream()
                .anyMatch(s -> s.calculateGPA() >= threshold);
    }

    public static boolean allStudentsPassing(Collection<Student> students, double passingGPA) {
        return students.stream()
                .allMatch(s -> s.calculateGPA() >= passingGPA);
    }

    public static long countStudentsWithGrade(Collection<Student> students,
                                              String courseName,
                                              GradeType grade) {
        return students.stream()
                .filter(s -> s.getGrade(courseName) == grade)
                .count();
    }
}