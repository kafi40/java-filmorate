package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmRequest;
import ru.yandex.practicum.filmorate.dto.genre.GenreRequest;
import ru.yandex.practicum.filmorate.exception.ElementNotExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final GenreService genreService;
    private final RatingService ratingService;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, UserService userService,
                       GenreService genreService, RatingService ratingService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.genreService = genreService;
        this.ratingService = ratingService;
    }

    public FilmDto get(Long id) {
        return filmStorage.get(id)
                .map(film -> {
                    film.setMpa(ratingService.get(film.getMpa().getId()));
                    film.setGenres(genreService.getForFilm(id));
                    return film;
                })
                .map(FilmMapper::mapToFilmDto)
                .orElseThrow(() -> new NotFoundException("Фильм с ID = " + id + " не найден"));
    }

    public List<FilmDto> getAll() {
        return filmStorage.getAll()
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public FilmDto save(FilmRequest request) {
        Film film = FilmMapper.mapToFilm(request);
        film.setMpa(getRating(request.getMpa().getId()));
        film = filmStorage.save(film);
        film.setGenres(saveGenre(film.getId(), request.getGenres()));
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto update(FilmRequest request) {
        if (request.getId() == null) {
            throw new ValidationException("ID","Должен быть указан ID");
        }
        Film updatedFilm = filmStorage.get(request.getId())
                .map(film -> FilmMapper.updateFilmFields(film, request))
                .orElseThrow(() -> new NotFoundException("Фильм с ID " + request.getId() + " не найден"));
        updatedFilm.setMpa(getRating(request.getMpa().getId()));
        updatedFilm.setGenres(saveGenre(request.getId(), request.getGenres()));
        updatedFilm = filmStorage.update(updatedFilm);
        return FilmMapper.mapToFilmDto(updatedFilm);
    }

    public boolean delete(Long id) {
        return filmStorage.delete(id);
    }

    public boolean putLike(Long id, Long userId) {
        checkId(id);
        userService.checkId(userId);
        return filmStorage.putLike(id, userId);
    }

    public boolean deleteLike(Long id, Long userId) {
        checkId(id);
        userService.checkId(userId);
        return filmStorage.deleteLike(id, userId);
    }

    public List<FilmDto> getTopFilms(int size) {
        return filmStorage.getTopFilms(size).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public List<FilmDto> getCommonFilms(Long userId, Long friendId) {
        userService.checkId(userId);
        userService.checkId(friendId);
        return filmStorage.findCommonFilms(userId, friendId).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public void checkId(Long id) {
        if (filmStorage.get(id).isEmpty()) {
            throw new NotFoundException("Объекта с ID " + id + " не существует");
        }
    }

    private Rating getRating(Long id) {
        try {
            return ratingService.get(id);
        } catch (NotFoundException e) {
            throw new ValidationException("ID", "Жанр с ID " + id + " не найден");
        }
    }

    private Set<Genre> saveGenre(Long id, List<GenreRequest> genres) {
        if (genres != null) {
            genres.forEach(g -> {
                try {
                    genreService.get(g.getId());
                    filmStorage.addGenreForFilm(id, g.getId());
                } catch (RuntimeException e) {
                    throw new ElementNotExistsException(e.getMessage());
                }
            });
            return genreService.getForFilm(id);
        } else {
            return new HashSet<>();
        }
    }
}
