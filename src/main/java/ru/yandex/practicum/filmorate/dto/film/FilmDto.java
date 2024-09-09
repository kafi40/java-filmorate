package ru.yandex.practicum.filmorate.dto.film;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.rating.RatingDto;

import ru.yandex.practicum.filmorate.model.Director;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class FilmDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private RatingDto mpa;
    private List<GenreDto> genres;
    private List<Director> directors;

    public FilmDto() {
        this.genres = new ArrayList<>();
        this.directors = new ArrayList<>();
    }
}
