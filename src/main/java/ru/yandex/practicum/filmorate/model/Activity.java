package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Activity {
    private Timestamp timestamp;
    private Long userId;
    private EventType eventType;
    private Operation operation;
    private Long eventId;
    private Long entityId;
}
