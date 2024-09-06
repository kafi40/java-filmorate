package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.controller.model.film.FilmDto;
import ru.yandex.practicum.filmorate.controller.model.film.FilmRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.director.DirectorRequest;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmRequest;
import ru.yandex.practicum.filmorate.dto.genre.GenreRequest;
import ru.yandex.practicum.filmorate.exception.ElementNotExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final RatingStorage ratingStorage;
    private final UserService userService;
    private final GenreService genreService;
    private final DirectorService directorService;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("ratingDbStorage") RatingStorage ratingStorage,
                       UserService userService,
                       GenreService genreService,
                       DirectorService directorService) {
        this.filmStorage = filmStorage;
        this.ratingStorage = ratingStorage;
        this.userService = userService;
        this.genreService = genreService;
        this.directorService = directorService;
    }

    public FilmDto get(Long id) {
        return filmStorage.get(id)
                .map(film -> {
                    film.setMpa(getRating(film.getMpa().getId()));
                    film.setGenres(genreService.getForFilm(id));
                    film.setDirectors(directorService.getForFilm(id));
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
        film.setDirectors(saveDirector(film.getId(), request.getDirectors()));
        return FilmMapper.mapToFilmDto(film);
    }

import java.util.List;
    public FilmDto update(FilmRequest request) {
        if (request.getId() == null) {
            throw new ValidationException("ID", "Должен быть указан ID");
        }
        Film updatedFilm = filmStorage.get(request.getId())
                .map(film -> FilmMapper.updateFilmFields(film, request))
                .orElseThrow(() -> new NotFoundException("Фильм с ID " + request.getId() + " не найден"));
        updatedFilm.setMpa(getRating(request.getMpa().getId()));
        updatedFilm.setGenres(saveGenre(request.getId(), request.getGenres()));
        updatedFilm.setDirectors(saveDirector(request.getId(), request.getDirectors()));
        updatedFilm = filmStorage.update(updatedFilm);
        return FilmMapper.mapToFilmDto(updatedFilm);
    }

public interface FilmService extends BaseService<FilmDto, FilmRequest> {
    boolean putLike(Long id, Long userId);

    boolean deleteLike(Long id, Long userId);

    List<FilmDto> getTopFilms(int count);

    List<FilmDto> getCommonFilms(Long userId, Long friendId);
    public List<FilmDto> getTopFilms(int count, Long genreId, Integer year) {
        if (genreId != null) {
            genreService.checkId(genreId);
        }
        return filmStorage.getTopFilms(count, genreId, year).stream()
                .peek(film -> film.setMpa(getRating(film.getMpa().getId())))
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

    public List<FilmDto> getDirectorsFilmsByYear(Long id) {
        return filmStorage.getDirectorsFilmSortByYear(id).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public List<FilmDto> getDirectorsFilmsByLikes(Long id) {
        return filmStorage.getDirectorsFilmSortByLikes(id).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public void checkId(Long id) {
        if (filmStorage.get(id).isEmpty()) {
            throw new NotFoundException("Объекта с ID " + id + " не существует");
        }
    }

    private Rating getRating(Long id) {
        return ratingStorage.get(id)
                .orElseThrow(() -> new ValidationException("ID", "Жанр с ID " + id + " не найден"));
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

    private Set<Director> saveDirector(Long id, List<DirectorRequest> directors) {
        if (directors != null) {
            directors.forEach(d -> {
                try {
                    directorService.getById(d.getId());
                    filmStorage.addDirectorForFilm(id, d.getId());
                } catch (RuntimeException e) {
                    throw new ElementNotExistsException(e.getMessage());
                }
            });
            return directorService.getForFilm(id);
        } else {
            return new HashSet<>();
        }
    }
}
