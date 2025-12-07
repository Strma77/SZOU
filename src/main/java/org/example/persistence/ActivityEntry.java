package org.example.persistence;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ActivityEntry(LocalDateTime timestamp, String action) implements Serializable {
    @Override
    public String toString(){
        return String.format("[%s] %s", timestamp, action);
    }
}
