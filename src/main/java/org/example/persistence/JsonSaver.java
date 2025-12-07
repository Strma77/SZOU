package org.example.persistence;
import com.google.gson.*;
import org.example.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
public class JsonSaver {

    private static final Logger logger = LoggerFactory.getLogger(JsonSaver.class);
    private static final String DATA_DIR = "src/main/resources/data/";
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    private static final String STUDENTS_FILE = "students.json";
    private static final String PROFESSORS_FILE = "professors.json";
    private static final String COURSES_FILE = "courses.json";
    private static final String LESSONS_FILE = "lessons.json";
    private static final String ENROLLMENTS_FILE = "enrollments.json";

    static {
        try { Files.createDirectories(Paths.get(DATA_DIR)); }
        catch (IOException e) { logger.error("Failed to create data directory", e); }
    }

    private static <T> void saveToFile(String file, List<T> data){
        try (FileWriter w = new FileWriter(DATA_DIR + file)){
            gson.toJson(data, w);
            logger.info("Saved {} items to {}", data.size(), file);
        } catch (IOException e) { logger.error("Failed to save {}", file, e);}
    }
    public static void saveAll(Set<Student> students, Set<Professor> professors,
                               List<Course> courses, List<Enrollment> enrollments) {
        saveStudents(students);
        saveProfessors(professors);
        saveCourses(courses);
        saveLessons(courses);
        saveEnrollments(enrollments);
        logger.info("All data saved to JSON files");
    }
    public static void saveStudents(Set<Student> students) {
        List<StudentData> dataList = students.stream()
                .map(s -> new StudentData(
                        s.getID(), s.getFirstName(), s.getLastName(), s.getUsername(),
                        s.getPassword(), s.getEmail(), s.getMaxCourses(),
                        new ArrayList<>(s.getEnrolledCourses()), s.getCourseGrades().entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey,
                                e -> e.getValue().name()))
                ))
                .toList();
        saveToFile(STUDENTS_FILE, dataList);
    }
    public static void saveProfessors(Set<Professor> professors) {
        List<ProfessorData> dataList = professors.stream()
                .map(p -> new ProfessorData(
                        p.getID(), p.getFirstName(), p.getLastName(), p.getUsername(),
                        p.getPassword(), p.getEmail(), p.getMaxCourses(),
                        new ArrayList<>(p.getTeachingCourses())
                ))
                .toList();
        saveToFile(PROFESSORS_FILE, dataList);
    }
    public static void saveCourses(List<Course> courses) {
        List<CourseData> dataList = courses.stream()
                .map(c -> new CourseData(
                        c.getName(), c.getProfessor().getID(), 50, c.getECTS(), c.getLevel().name(),
                        c.getLessons().stream() .map(Lesson::getName).collect(Collectors.toList())
                ))
                .toList();
        saveToFile(COURSES_FILE, dataList);
    }
    public static void saveLessons(List<Course> courses) {
        List<LessonData> dataList = new ArrayList<>();

        for (Course course : courses) {
            for (Lesson lesson : course.getLessons()) {
                String date = null;
                String time = null;

                if (lesson.getStartTime().isPresent()) {
                    LocalDateTime startTime = lesson.getStartTime().get();
                    date = startTime.toLocalDate().toString();
                    time = startTime.toLocalTime()
                            .format(DateTimeFormatter.ofPattern("HH:mm"));
                }

                dataList.add(new LessonData(
                        lesson.getName(),
                        course.getName(),
                        lesson.getLengthMinutes(),
                        lesson.getType().name(),
                        date,
                        time
                ));
            }
        }
        saveToFile(LESSONS_FILE, dataList);
    }
    public static void saveEnrollments(List<Enrollment> enrollments) {
        List<EnrollmentData> dataList = enrollments.stream()
                .map(e -> new EnrollmentData(
                        e.student().getID(), e.course().getName(), e.semester().name(), e.status().name(),
                        e.grade().name(), e.enrollmentDate().toString(),
                        e.completionDate() != null ? e.completionDate().toString() : null
                ))
                .toList();
        saveToFile(ENROLLMENTS_FILE, dataList);
    }
    public static void deleteAllData() {
        try {
            Files.deleteIfExists(Paths.get(DATA_DIR + STUDENTS_FILE));
            Files.deleteIfExists(Paths.get(DATA_DIR + PROFESSORS_FILE));
            Files.deleteIfExists(Paths.get(DATA_DIR + COURSES_FILE));
            Files.deleteIfExists(Paths.get(DATA_DIR + LESSONS_FILE));
            Files.deleteIfExists(Paths.get(DATA_DIR + ENROLLMENTS_FILE));
            logger.info("All JSON data files deleted");
        } catch (IOException e) {
            logger.error("Failed to delete data files", e);
        }
    }
}