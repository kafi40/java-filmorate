package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.MinimalDate;
import ru.yandex.practicum.filmorate.dto.genre.GenreRequest;
import ru.yandex.practicum.filmorate.dto.rating.RatingRequest;
import java.time.LocalDate;
import java.util.List;

@Data
public class FilmRequest {
    private Long id;
    @NotBlank(message = "Пустое название")
    private String name;
    @Size(max = 200, message = "Превышена длина описания")
    private String description;
    @MinimalDate
    private LocalDate releaseDate;
    @Positive(message = "Отрицательное значение или ноль")
    private Integer duration;
    private RatingRequest mpa;
    private List<GenreRequest> genres;
}
