package org.example.entities;

public class Course {
    private final String name;
    private final Professor professor;
    private final Lesson[] lessons;
    private int lessonCount = 0;

    public Course(String name, Professor professor, int maxLessons) {
        this.name = name;
        this.professor = professor;
        this.lessons = new Lesson[maxLessons];
    }

    public void addLesson(Lesson lesson){
        if(lessonCount < lessons.length) lessons[lessonCount++] = lesson;
        else  System.out.println("Ne može se dodati više lekcija u tečaj " + name);
    }
    public String getName() { return name; }
    public Professor getProfessor() { return professor; }
    public Lesson[] getLessons() { return lessons; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        System.out.println("====================");
        sb.append("Course: ").append(name).append("\n");
        sb.append("Profesor: ").append(professor.getFirstName()).append(" ").append(professor.getLastName()).append("\n");
        sb.append("Lekcije:\n");
        for (int i = 0; i < lessonCount; i++) {
            sb.append("-").append(lessons[i]).append("\n");
        }
        System.out.println("====================");
        return sb.toString();
    }
}
