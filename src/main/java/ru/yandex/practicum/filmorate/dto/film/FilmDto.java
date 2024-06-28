package ru.yandex.practicum.filmorate.dto.film;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.yandex.practicum.filmorate.dto.genre.GenreFromFilmRequest;
import ru.yandex.practicum.filmorate.dto.rating.RatingFromFilmRequest;
import java.time.LocalDate;
import java.util.List;

@Data
public class FilmDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private RatingFromFilmRequest mpa;
    private List<GenreFromFilmRequest> genres;
}
