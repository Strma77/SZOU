package org.example.entities;
import java.time.LocalDate;
import java.time.LocalDateTime;

public sealed interface Schedulable permits Lesson{
    void schedule (LocalDate date, int startHour, int startMinute, int durMin);
    LocalDateTime getStartTime();
    int getDurationMinutes();
}
