package org.example.services;

import org.example.entities.*;
import org.example.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Service for demonstrating modern Java features (sorting, streams, sequenced collections).
 */
public class DemonstrationService {

    private static final Logger logger = LoggerFactory.getLogger(DemonstrationService.class);

    /**
     * Runs all demonstrations.
     */
    public static void demonstrateAll(Set<Student> students, Set<Professor> professors,
                                      List<Course> courses, List<Enrollment> enrollments) {
        demonstrateSorting(students, professors, courses, enrollments);
        demonstrateStreamOperations(students, courses, enrollments);
        demonstrateSequencedCollections(students, professors);
        demonstrateOptional(students, courses);
        demonstrateAssignments(courses, students);
    }

    /**
     * Demonstrates sorting with Comparators and lambda expressions.
     */
    private static void demonstrateSorting(Set<Student> students, Set<Professor> professors,
                                           List<Course> courses, List<Enrollment> enrollments) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("📊 DEMONSTRATION: SORTING WITH COMPARATORS");
        System.out.println("=".repeat(50));

        // Sort students by name
        System.out.println("\n1️⃣  Students sorted by name:");
        UserService.sortStudentsByName(students).forEach(s ->
                System.out.println("   - " + s.getFirstName() + " " + s.getLastName()));

        // Sort students by GPA (descending)
        System.out.println("\n2️⃣  Students sorted by GPA (highest first):");
        UserService.sortStudentsByGPA(students).forEach(s ->
                System.out.println("   - " + s.getFirstName() + " " + s.getLastName() +
                        " (GPA: " + String.format("%.2f", s.calculateGPA()) + ")"));

        // Sort courses by ECTS
        System.out.println("\n3️⃣  Courses sorted by ECTS (highest first):");
        CourseService.sortCoursesByECTS(courses).forEach(c ->
                System.out.println("   - " + c.getName() + " (" + c.getECTS() + " ECTS)"));

        // Sort courses by level
        System.out.println("\n4️⃣  Courses sorted by difficulty level:");
        CourseService.sortCoursesByLevel(courses).forEach(c ->
                System.out.println("   - " + c.getName() + " [" + c.getLevel() + "]"));

        // Sort enrollments by grade
        System.out.println("\n5️⃣  Enrollments sorted by grade (best first):");
        EnrollmentService.sortEnrollmentsByGrade(enrollments).stream()
                .limit(5)
                .forEach(e -> System.out.println("   - " + e.student().getFirstName() + " " +
                        e.student().getLastName() + " in " + e.course().getName() +
                        " (Grade: " + e.grade().name().replace("_", "+") + ")"));

        logger.info("Sorting demonstration completed");
    }

    /**
     * Demonstrates stream operations with groupingBy and partitioningBy.
     */
    private static void demonstrateStreamOperations(Set<Student> students,
                                                    List<Course> courses,
                                                    List<Enrollment> enrollments) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🔄 DEMONSTRATION: STREAM OPERATIONS");
        System.out.println("=".repeat(50));

        // groupingBy: Group students by course load
        System.out.println("\n1️⃣  Students grouped by course load:");
        Map<String, List<Student>> studentsByCourseLoad =
                UserService.groupStudentsByCourseLoad(students);
        studentsByCourseLoad.forEach((load, list) ->
                System.out.println("   " + load + ": " + list.size() + " students"));

        // partitioningBy: Partition students by GPA
        System.out.println("\n2️⃣  Students partitioned by passing GPA (>= 2.0):");
        Map<Boolean, List<Student>> studentsByGPA =
                UserService.partitionStudentsByGPA(students);
        System.out.println("   ✅ Passing: " + studentsByGPA.get(true).size() + " students");
        System.out.println("   ❌ At Risk: " + studentsByGPA.get(false).size() + " students");

        // groupingBy: Group courses by level
        System.out.println("\n3️⃣  Courses grouped by difficulty level:");
        Map<CourseLevel, List<Course>> coursesByLevel =
                CourseService.groupCoursesByLevel(courses);
        coursesByLevel.forEach((level, list) ->
                System.out.println("   " + level + ": " + list.size() + " courses"));

        // groupingBy: Group courses by professor
        System.out.println("\n4️⃣  Courses grouped by professor:");
        Map<String, List<Course>> coursesByProfessor =
                CourseService.groupCoursesByProfessor(courses);
        coursesByProfessor.forEach((prof, list) ->
                System.out.println("   " + prof + ": " + list.size() + " course(s)"));

        // partitioningBy: Partition courses by popularity
        System.out.println("\n5️⃣  Courses partitioned by popularity (>= 3 students):");
        Map<Boolean, List<Course>> coursesByPopularity =
                CourseService.partitionCoursesByPopularity(courses);
        System.out.println("   ⭐ Popular: " + coursesByPopularity.get(true).size() + " courses");
        System.out.println("   📉 Unpopular: " + coursesByPopularity.get(false).size() + " courses");

        // groupingBy: Group enrollments by semester
        System.out.println("\n6️⃣  Enrollments grouped by semester:");
        Map<Semester, List<Enrollment>> enrollmentsBySemester =
                EnrollmentService.groupEnrollmentsBySemester(enrollments);
        enrollmentsBySemester.forEach((semester, list) ->
                System.out.println("   " + semester + ": " + list.size() + " enrollments"));

        // partitioningBy: Partition enrollments by passing
        System.out.println("\n7️⃣  Enrollments partitioned by passing grade:");
        Map<Boolean, List<Enrollment>> enrollmentsByPassing =
                EnrollmentService.partitionEnrollmentsByPassing(enrollments);
        System.out.println("   ✅ Passed: " + enrollmentsByPassing.get(true).size() + " enrollments");
        System.out.println("   ❌ Failed: " + enrollmentsByPassing.get(false).size() + " enrollments");

        // groupingBy: Group students by GPA range
        System.out.println("\n8️⃣  Students grouped by GPA range:");
        Map<String, List<Student>> studentsByGPARange =
                GradingService.groupStudentsByGPARange(students);
        studentsByGPARange.forEach((range, list) ->
                System.out.println("   " + range + ": " + list.size() + " students"));

        logger.info("Stream operations demonstration completed");
    }

    /**
     * Demonstrates Sequenced Collections (getFirst, getLast, reversed).
     */
    private static void demonstrateSequencedCollections(Set<Student> students,
                                                        Set<Professor> professors) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🔢 DEMONSTRATION: SEQUENCED COLLECTIONS");
        System.out.println("=".repeat(50));

        // Get first and last student
        Optional<Student> firstStudent = UserService.getFirstStudent(students);
        Optional<Student> lastStudent = UserService.getLastStudent(students);

        System.out.println("\n1️⃣  First student enrolled: ");
        firstStudent.ifPresentOrElse(
                s -> System.out.println("   " + s.getFirstName() + " " + s.getLastName()),
                () -> System.out.println("   None")
        );

        System.out.println("\n2️⃣  Last student enrolled: ");
        lastStudent.ifPresentOrElse(
                s -> System.out.println("   " + s.getFirstName() + " " + s.getLastName()),
                () -> System.out.println("   None")
        );

        System.out.println("\n3️⃣  Students in reverse order:");
        if (students instanceof SequencedSet<Student> seq) {
            seq.reversed().stream()
                    .limit(3)
                    .forEach(s -> System.out.println("   - " + s.getFirstName() + " " +
                            s.getLastName()));
        }


        // Demonstrate with professors
        System.out.println("\n4️⃣  Professors in insertion order:");
        professors.stream()
                .limit(3)
                .forEach(p -> System.out.println("   - " + p.getFirstName() + " " +
                        p.getLastName()));

        logger.info("Sequenced collections demonstration completed");
    }

    private static void demonstrateOptional(Set<Student> students, List<Course> courses) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🎁 DEMONSTRATION: OPTIONAL<T> USAGE");
        System.out.println("=".repeat(50));

        // 1. Finding top student
        System.out.println("\n1️⃣  Finding top student by GPA:");
        Optional<Student> topStudent = GradingService.findTopStudentByGPA(students);
        topStudent.ifPresentOrElse(
                s -> System.out.println("   Top Student: " + s.getFirstName() + " " +
                        s.getLastName() + " (GPA: " + String.format("%.2f", s.calculateGPA()) + ")"),
                () -> System.out.println("   No students found")
        );

        // 2. Finding most popular course
        System.out.println("\n2️⃣  Finding most popular course:");
        Optional<Course> popularCourse = CourseService.findMostPopularCourse(courses);
        String courseName = popularCourse
                .map(c -> c.getName() + " (" + c.getEnrollmentCount() + " students)")
                .orElse("No courses available");
        System.out.println("   " + courseName);

        // 3. Optional chaining example
        System.out.println("\n3️⃣  Optional chaining - Get first student's email:");
        String email = UserService.getFirstStudent(students)
                .map(Student::getEmail)
                .orElse("no-email@example.com");
        System.out.println("   " + email);

        // 4. Optional with filter
        System.out.println("\n4️⃣  Optional with filter - Find honor student (GPA >= 4.0):");
        UserService.getFirstStudent(students)
                .filter(s -> s.calculateGPA() >= 4.0)
                .ifPresentOrElse(
                        s -> System.out.println("   Honor Student: " + s.getFirstName() + " " + s.getLastName()),
                        () -> System.out.println("   No honor students in first position")
                );

        // 5. Optional orElseThrow
        System.out.println("\n5️⃣  Optional orElseThrow - Safely get top student:");
        try {
            Student top = GradingService.findTopStudentByGPA(students)
                    .orElseThrow(() -> new NoSuchElementException("No students available"));
            System.out.println("   Found: " + top.getFirstName() + " " + top.getLastName());
        } catch (NoSuchElementException e) {
            System.out.println("   " + e.getMessage());
        }

        // 6. Optional flatMap example (if we had nested Optionals)
        System.out.println("\n6️⃣  Optional map vs flatMap:");
        System.out.println("   Using map: " +
                UserService.getFirstStudent(students)
                        .map(s -> s.getFirstName() + " " + s.getLastName())
                        .orElse("Unknown"));

        logger.info("Optional demonstration completed");
    }

    private static void demonstrateAssignments(List<Course> courses, Set<Student> students) {
        if (courses.isEmpty() || students.isEmpty()) return;

        System.out.println("\n" + "=".repeat(50));
        System.out.println("📝 DEMONSTRATION: ASSIGNMENTS & SUBMISSIONS (Optional<T>)");
        System.out.println("=".repeat(50));

        Course course = courses.getFirst();
        List<Student> studentList = new ArrayList<>(students);

        // 1. Create assignment
        Assignment assignment = AssignmentService.createAssignment(
                "Lambda Expressions Lab",
                "Implement functional programming concepts",
                course,
                100,
                java.time.LocalDateTime.now().plusDays(7)
        );

        System.out.println("\n1️⃣  Created assignment: " + assignment.getTitle());
        System.out.println("   Max Points: " + assignment.getMaxPoints());
        System.out.println("   Due Date: " + assignment.getDueDate().toLocalDate());

        // 2. Create submissions
        List<Submission> submissions = new ArrayList<>();
        for (int i = 0; i < Math.min(3, studentList.size()); i++) {
            Student student = studentList.get(i);
            Submission sub = AssignmentService.submitAssignment(
                    student, assignment, "Solution by " + student.getFirstName()
            );
            submissions.add(sub);
        }

        System.out.println("\n2️⃣  Created " + submissions.size() + " submissions");

        // 3. Grade some submissions (leave some ungraded to demonstrate Optional)
        if (!submissions.isEmpty()) {
            AssignmentService.gradeSubmission(submissions.get(0), 95, "Excellent!");
            if (submissions.size() > 1) {
                AssignmentService.gradeSubmission(submissions.get(1), 87, "Very good!");
            }
        }

        // 4. Demonstrate Optional with getScore()
        System.out.println("\n3️⃣  Demonstrating Optional<Integer> with scores:");
        submissions.forEach(sub -> {
            System.out.print("   " + sub.getStudent().getFirstName() + ": ");

            // Using Optional properly
            sub.getScore().ifPresentOrElse(
                    score -> System.out.println(score + "/" + assignment.getMaxPoints() +
                            " (" + String.format("%.1f", sub.getPercentage()) + "%)"),
                    () -> System.out.println("Not graded yet")
            );
        });

        // 5. Lambda operations on submissions
        System.out.println("\n4️⃣  Lambda filtering - Graded submissions:");
        long gradedCount = submissions.stream()
                .filter(Submission::isGraded)
                .count();
        System.out.println("   Graded: " + gradedCount + "/" + submissions.size());

        // 6. Find top submission using Optional
        System.out.println("\n5️⃣  Finding top submission (Optional usage):");
        AssignmentService.findTopSubmission(submissions)
                .ifPresentOrElse(
                        top -> System.out.println("   Top: " + top.getStudent().getFirstName() +
                                " with " + top.getPercentage() + "%"),
                        () -> System.out.println("   No graded submissions yet")
                );

        logger.info("Assignment demonstration completed");
    }
}