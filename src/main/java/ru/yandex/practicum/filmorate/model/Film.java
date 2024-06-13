package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.annotation.MinimalDate;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class Film extends Entity {
    private Long id;
    @NotBlank(message = "Пустое название")
    private String name;
    @Size(max = 200, message = "Превышена длина описания")
    private String description;
    @MinimalDate
    private LocalDate releaseDate;
    @Positive(message = "Отрицательное значение или ноль")
    private Integer duration;
    private Set<Long> likesSet;

    public Film() {
        this.likesSet = new HashSet<>();
    }
}
