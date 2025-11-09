package org.example.entities;

import org.example.enums.LessonType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a lesson with name, duration, type, and optional scheduling information.
 */
public non-sealed class Lesson implements Schedulable {

    private final String name;
    private final int lengthMinutes;
    private final LessonType type;
    private LocalDateTime startTime;

    public Lesson(String name, int lengthMinutes, LessonType type) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Lesson name cannot be empty.");
        if (lengthMinutes <= 0) throw new IllegalArgumentException("Lesson duration must be positive.");

        this.name = name;
        this.lengthMinutes = lengthMinutes;
        this.type = Objects.requireNonNull(type, "Lesson type cannot be null");
    }

    public String getName() { return name; }
    public Integer getLengthMinutes() { return lengthMinutes; }
    public LessonType getType() { return type; }

    @Override
    public void schedule(LocalDate date, int startHour, int startMinute, int durMin){
        this.startTime = date.atTime(startHour, startMinute);
    }

    @Override
    public LocalDateTime getStartTime(){ return startTime; }

    @Override
    public int getDurationMinutes(){ return lengthMinutes; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lesson lesson)) return false;
        return lengthMinutes == lesson.lengthMinutes &&
                name.equals(lesson.name) &&
                type == lesson.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lengthMinutes, type);
    }

    @Override
    public String toString(){
        return name + " (" + type + ") - " + lengthMinutes + "min.";
    }
}