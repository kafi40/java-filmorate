package ru.yandex.practicum.filmorate.dto.activity;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;

import java.sql.Timestamp;

@Data
public class ActivityRequest {
    private Timestamp timestamp;
    private Long userId;
    private EventType eventType;
    private Operation operation;
    private Long eventId;
    private Long entityId;
}
