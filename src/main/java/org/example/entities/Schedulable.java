package org.example.entities;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Defines scheduling capabilities for entities that can be scheduled at specific times.
 * <p>
 * This sealed interface permits only {@link Lesson} implementations and provides
 * methods for scheduling and retrieving time information.
 */
public sealed interface Schedulable permits Lesson{

    /**
     * Schedules this entity at a specific date and time with duration.
     *
     * @param date the date for scheduling (not null)
     * @param startHour the starting hour (0-23)
     * @param startMinute the starting minute (0-59)
     * @param durMin the duration in minutes
     */
    void schedule (LocalDate date, int startHour, int startMinute, int durMin);

    /**
     * Returns the scheduled start date and time.
     *
     * @return the start LocalDateTime, or null if not scheduled
     */
    Optional<LocalDateTime> getStartTime();

    /**
     * Returns the duration in minutes.
     *
     * @return duration in minutes
     */
    int getDurationMinutes();
}