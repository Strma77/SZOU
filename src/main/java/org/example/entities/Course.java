package org.example.entities;

/**
 * Represents an academic course with lessons, professor assignment, and ECTS credits.
 * <p>
 * A course contains a fixed-size array of lessons and tracks the assigned professor.
 * Lessons can be added up to the maximum capacity specified during construction.
 */
public class Course {

    private final String name;
    private final Professor professor;
    private final Lesson[] lessons;
    private int lessonCount = 0;
    private int ECTS = 0;

    /**
     * Constructs a course with the specified properties.
     *
     * @param name the course name (not null)
     * @param professor the assigned professor (not null)
     * @param maxLessons the maximum number of lessons allowed
     * @param ECTS the ECTS credit value
     */
    public Course(String name, Professor professor, int maxLessons, int ECTS) {
        this.name = name;
        this.professor = professor;
        this.lessons = new Lesson[maxLessons];
        this.ECTS = ECTS;
    }

    /**
     * Adds a lesson to this course.
     * <p>
     * If maximum lesson capacity is reached, prints a warning but does not throw exception.
     *
     * @param lesson the lesson to add (not null)
     */
    public void addLesson(Lesson lesson){
        if(lessonCount < lessons.length) lessons[lessonCount++] = lesson;
        else  System.out.println("Ne može se dodati više lekcija u tečaj " + name);
    }

    /**
     * Returns the course name.
     *
     * @return course name
     */
    public String getName() { return name; }

    /**
     * Returns the assigned professor.
     *
     * @return professor teaching this course
     */
    public Professor getProfessor() { return professor; }

    /**
     * Returns the array of lessons.
     *
     * @return lesson array (may contain null elements)
     */
    public Lesson[] getLessons() { return lessons; }

    /**
     * Returns the ECTS credit value.
     *
     * @return ECTS credits
     */
    public int getECTS() { return ECTS; }

    /**
     * Returns formatted string with course details including lessons.
     * <p>
     * Prints decorative separators to console as side effect.
     *
     * @return formatted multi-line string with course information
     */
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