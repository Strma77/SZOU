package org.example.persistence;

public class LessonData {
    public String name;
    public String courseName;
    public int lengthMinutes;
    public String type;
    public String scheduledDate;
    public String scheduledTime;

    public LessonData(String name, String courseName, int lengthMinutes,
                      String type, String scheduledDate, String scheduledTime) {
        this.name = name;
        this.courseName = courseName;
        this.lengthMinutes = lengthMinutes;
        this.type = type;
        this.scheduledDate = scheduledDate;
        this.scheduledTime = scheduledTime;
    }

    public LessonData() {}
}
