package ru.yandex.practicum.filmorate.dto.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;

import java.sql.Timestamp;

@Data
public class ActivityDto {

    private Timestamp timestamp;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long userId;
    private EventType eventType;
    private Operation operation;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long eventId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long entityId;
}
