package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.genre.GenreFromFilmRequest;
import ru.yandex.practicum.filmorate.dto.rating.RatingFromFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilmMapper {
    public static FilmDto mapToFilmDto(Film film) {
        FilmDto filmDto = new FilmDto();
        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDto.setDescription(film.getDescription());
        filmDto.setReleaseDate(film.getReleaseDate());
        filmDto.setDuration(film.getDuration());

        RatingFromFilmRequest mpa = new RatingFromFilmRequest();
        mpa.setId(film.getMpa().getId());
        filmDto.setMpa(mpa);

        List<GenreFromFilmRequest> genres = new ArrayList<>();
        film.getGenres().forEach(g -> {
            GenreFromFilmRequest genre = new GenreFromFilmRequest();
            genre.setId(g.getId());
            genres.add(genre);
        });
        filmDto.setGenres(genres);
        return filmDto;
    }

    public static Film mapToFilm(NewFilmRequest request) {
        Film film = new Film();
        film.setName(request.getName());
        film.setDescription(request.getDescription());
        film.setReleaseDate(request.getReleaseDate());
        film.setDuration(request.getDuration());

        return film;
    }

    public static Film updateFilmFields(Film film, NewFilmRequest request) {
        film.setName(request.getName());
        film.setDescription(request.getDescription());
        film.setReleaseDate(request.getReleaseDate());
        film.setDuration(request.getDuration());
        return film;
    }
}
