package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class Genre implements Comparable<Genre> {
    private Long id;
    @NotBlank(message = "Пустое название")
    private String name;

    @Override
    public int compareTo(Genre o) {
        return this.id.compareTo(o.getId());
    }
}
