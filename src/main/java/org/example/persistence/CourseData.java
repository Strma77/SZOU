package org.example.persistence;
import java.util.List;
public class CourseData {
    public String name;
    public int professorId;
    public int maxLessons;
    public int ects;
    public String level;
    public List<String> lessonNames;

    public CourseData(String name, int professorId, int maxLessons,
                      int ects, String level, List<String> lessonNames) {
        this.name = name;
        this.professorId = professorId;
        this.maxLessons = maxLessons;
        this.ects = ects;
        this.level = level;
        this.lessonNames = lessonNames;
    }

    public CourseData() {}
}
