package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.MinimalDate;

import java.time.LocalDate;
import java.util.Set;

@Data
public class Film {
    private Long id;
    @NotBlank(message = "Пустое название")
    private String name;
    @Size(max = 200, message = "Превышена длина описания")
    private String description;
    @MinimalDate
    private LocalDate releaseDate;
    @Positive(message = "Отрицательное значение или ноль")
    private Integer duration;
    private Rating mpa;
    private Set<Genre> genres;
    private Set<Long> likesSet;
}
