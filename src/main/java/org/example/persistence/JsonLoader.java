package org.example.persistence;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.example.entities.*;
import org.example.enums.*;
import org.example.exceptions.DataLoadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

public class JsonLoader {
    private static final Logger logger = LoggerFactory.getLogger(JsonLoader.class);
    private static final String DATA_DIR = "src/main/resources/data/";
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    private static final String STUDENTS_FILE = "students.json";
    private static final String PROFESSORS_FILE = "professors.json";
    private static final String COURSES_FILE = "courses.json";
    private static final String LESSONS_FILE = "lessons.json";
    private static final String ENROLLMENTS_FILE = "enrollments.json";

    static {
        try { Files.createDirectories(Paths.get(DATA_DIR));}
        catch (IOException e) { logger.error("Failed to create data directory", e); }
    }
    private static <T> List<T> loadList(String file, Type type){
        try{
            Path path = Path.of(DATA_DIR + file);
            if(!Files.exists(path)) {
                logger.info("No {} found.",file);
                return new ArrayList<>();
            }
            List<T> result = gson.fromJson(Files.readString(path), type);
            return result != null ? result : new ArrayList<>();
        } catch (Exception e) {
            handleError(file, e);
            return new ArrayList<>();
        }
    }

    private static void handleError(String file, Exception e) {
        String msg = e instanceof JsonSyntaxException ? "JSON syntax error in " :
                     e instanceof JsonParseException  ? "Invalid JSON format in " :
                     e instanceof IllegalArgumentException ? "Enum conversion failed in " :
                     e instanceof IOException ? "File I/O issue in " : "Unexpected error in ";
        throw new DataLoadException(msg + file, e);
    }
    public static Set<Student> loadStudents() {
        Type t = new TypeToken<List<StudentData>>(){}.getType();
        Set<Student> students = new LinkedHashSet<>();

        for (StudentData d : JsonLoader.<StudentData>loadList(STUDENTS_FILE, t)) {
            Student s = new Student.StudentBuilder(d.firstName, d.lastName, d.id)
                    .username(d.username).password(d.password)
                    .email(d.email).maxCourses(d.maxCourses).build();

            if (d.enrolledCourses != null) d.enrolledCourses.forEach(s::enrollCourses);
            if (d.courseGrades != null)
                d.courseGrades.forEach((c,g) -> s.setGrade(c, GradeType.valueOf(g)));
            students.add(s);
        }
        logger.info("Loaded {} students", students.size());
        return students;
    }
    public static Set<Professor> loadProfessors() {
        Type t = new TypeToken<List<ProfessorData>>(){}.getType();
        Set<Professor> professors = new LinkedHashSet<>();

        for (ProfessorData d : JsonLoader.<ProfessorData>loadList(PROFESSORS_FILE, t)) {
            Professor p = new Professor.ProfessorBuilder(d.firstName, d.lastName, d.id)
                    .username(d.username).password(d.password)
                    .email(d.email).maxCourses(d.maxCourses).build();

            if (d.teachingCourses != null) d.teachingCourses.forEach(p::addCourse);
            professors.add(p);
        }
        logger.info("Loaded {} professors", professors.size());
        return professors;
    }
    public static List<Course> loadCourse(Set<Professor> profs) {
        Type t = new TypeToken<List<CourseData>>(){}.getType();
        Map<Integer, Professor> map = new HashMap<>();
        profs.forEach(p -> map.put(p.getID(), p));

        List<Course> courses = new ArrayList<>();
        for (CourseData d : JsonLoader.<CourseData>loadList(COURSES_FILE, t)) {
            Professor p = map.get(d.professorId);
            if (p == null) {
                logger.warn("Professor ID {} not found for course {}", d.professorId, d.name);
                continue;
            }
            courses.add(new Course(d.name, p, d.maxLessons, d.ects,
                    CourseLevel.valueOf(d.level)));
        }
        logger.info("Loaded {} courses", courses.size());
        return courses;
    }
    public static void loadLessons(List<Course> courses) {
        Type t = new TypeToken<List<LessonData>>(){}.getType();
        Map<String, Course> map = new HashMap<>();
        courses.forEach(c -> map.put(c.getName(), c));

        for (LessonData d : JsonLoader.<LessonData>loadList(LESSONS_FILE, t)) {
            Course c = map.get(d.courseName);
            if (c == null) {
                logger.warn("Course {} not found for lesson {}", d.courseName, d.name);
                continue;
            }
            Lesson l = new Lesson(d.name, d.lengthMinutes, LessonType.valueOf(d.type));
            if (d.scheduledDate != null && d.scheduledTime != null) {
                String[] tme = d.scheduledTime.split(":");
                l.schedule(java.time.LocalDate.parse(d.scheduledDate),
                        Integer.parseInt(tme[0]), Integer.parseInt(tme[1]), d.lengthMinutes);
            }
            c.addLesson(l);
        }
        logger.info("Lessons loaded");
    }
    public static List<Enrollment> loadEnrollments(Set<Student> students, List<Course> courses) {
        Type t = new TypeToken<List<EnrollmentData>>(){}.getType();
        Map<Integer, Student> sm = new HashMap<>();
        Map<String, Course> cm = new HashMap<>();
        students.forEach(s -> sm.put(s.getID(), s));
        courses.forEach(c -> cm.put(c.getName(), c));

        List<Enrollment> list = new ArrayList<>();
        for (EnrollmentData d : JsonLoader.<EnrollmentData>loadList(ENROLLMENTS_FILE, t)) {
            Student s = sm.get(d.studentId);
            Course c = cm.get(d.courseName);
            if (s == null || c == null) continue;

            c.enrollStudent(s);
            list.add(new Enrollment(
                    s, c, Semester.valueOf(d.semester),
                    EnrollmentStatus.valueOf(d.status),
                    GradeType.valueOf(d.grade),
                    LocalDateTime.parse(d.enrollmentDate),
                    d.completionDate != null ? LocalDateTime.parse(d.completionDate) : null
            ));
        }
        logger.info("Loaded {} enrollments", list.size());
        return list;
    }
}
