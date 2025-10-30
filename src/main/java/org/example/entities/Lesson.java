package org.example.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public non-sealed class Lesson implements Schedulable {
    private final String name;
    private final Integer lengthMinutes;
    private LocalDateTime startTime;


    public Lesson(String name, Integer lengthMinutes) {
        this.name = name;
        this.lengthMinutes = lengthMinutes;
    }

    public String getName() { return name; }
    public Integer getLengthMinutes() { return lengthMinutes; }

    @Override
    public void schedule (LocalDate date, int startHour, int startMinute, int durMin){
        this.startTime = date.atTime(startHour, startMinute);
    }

    @Override
    public LocalDateTime getStartTime(){ return startTime; }
    @Override
    public int getDurationMinutes(){ return lengthMinutes; }
    @Override
    public String toString(){ return name + "-" + lengthMinutes + "min."; }
}
