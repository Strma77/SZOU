package org.example.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a lesson with name, duration, and optional scheduling information.
 * <p>
 * Implements {@link Schedulable} to support scheduling at specific dates and times.
 * This non-sealed class finalizes the Schedulable interface hierarchy.
 */
public non-sealed class Lesson implements Schedulable {

    private final String name;
    private final Integer lengthMinutes;
    private LocalDateTime startTime;

    /**
     * Constructs a lesson with the specified name and duration.
     *
     * @param name the lesson name (not null)
     * @param lengthMinutes the duration in minutes (not null)
     */
    public Lesson(String name, Integer lengthMinutes) {
        this.name = name;
        this.lengthMinutes = lengthMinutes;
    }

    /**
     * Returns the lesson name.
     *
     * @return lesson name
     */
    public String getName() { return name; }

    /**
     * Returns the lesson duration in minutes.
     *
     * @return duration in minutes
     */
    public Integer getLengthMinutes() { return lengthMinutes; }

    /**
     * Schedules this lesson at the specified date and time.
     * <p>
     * The {@code durMin} parameter is ignored; duration is determined by {@code lengthMinutes}.
     *
     * @param date the date for scheduling (not null)
     * @param startHour the starting hour (0-23)
     * @param startMinute the starting minute (0-59)
     * @param durMin the duration parameter (not used)
     */
    @Override
    public void schedule (LocalDate date, int startHour, int startMinute, int durMin){
        this.startTime = date.atTime(startHour, startMinute);
    }

    @Override
    public LocalDateTime getStartTime(){ return startTime; }

    @Override
    public int getDurationMinutes(){ return lengthMinutes; }

    /**
     * Returns string representation with name and duration.
     *
     * @return formatted string: "[name]-[lengthMinutes]min."
     */
    @Override
    public String toString(){ return name + "-" + lengthMinutes + "min."; }
}