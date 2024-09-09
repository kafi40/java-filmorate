package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class Director implements Comparable<Director> {
    private Long id;
    @NotBlank(message = "Пустое имя")
    private String name;

    @Override
    public int compareTo(Director o) {
        return this.id.compareTo(o.getId());
    }
}
