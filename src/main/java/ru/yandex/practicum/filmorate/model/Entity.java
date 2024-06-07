package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public abstract class Entity {
    private Long id;
}
