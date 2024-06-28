package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage repository;
    private final UserService userService;
    private final RatingStorage ratingRepository;
    private final GenreStorage genreRepository;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage repository, UserService userService,
                       RatingStorage ratingRepository, GenreStorage genreRepository) {
        this.repository = repository;
        this.userService = userService;
        this.ratingRepository = ratingRepository;
        this.genreRepository = genreRepository;
    }

    public FilmDto get(Long id) {
        return repository.get(id)
                .map(FilmMapper::mapToFilmDto)
                .orElseThrow(() -> new NotFoundException("Фильм с ID = " + id + " не найден"));
    }

    public List<FilmDto> getAll() {
        return repository.getAll()
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public FilmDto save(NewFilmRequest request) {
        Rating rating = ratingRepository.get(request.getMpa().getId())
                .orElseThrow(() -> new ValidException("ID", "Рейтинга с ID = " + request.getMpa().getId() + " не существует, укажите корректный рейтинг"));

        List<Genre> genres = request.getGenres().stream()
                .map(g -> {
                    if (genreRepository.get(g.getId()).isPresent()) {
                        return genreRepository.get(g.getId()).get();
                    } else {
                        throw new ValidException("ID", "Жанра с ID = " + g.getId() + " не существует, укажите корректный жанр");
                    }
                })
                .toList();

        Film film = FilmMapper.mapToFilm(request);
        film.setMpa(rating);
        film.setGenres(genres);
        film = repository.save(film);
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto update(NewFilmRequest request) {
//        if (request.getId() == null) {
//            throw new ValidationException("Должен быть указан ID");
//        }
//        if (repository.get(request.getId()).isPresent()) {
//            Film oldFilm = repository.get(request.getId()).get();
//            oldFilm.setName(request.getName());
//            oldFilm.setDescription(request.getDescription());
//            oldFilm.setReleaseDate(request.getReleaseDate());
//            oldFilm.setDuration(request.getDuration());
//            oldFilm.setMpa(request.getMpa());
//            Film film = repository.update(oldFilm);
//            return FilmMapper.mapToFilmDto(film);
//        }
//        throw new NotFoundException("Фильма с ID " + request.getId() + " не существует");

        Film updatedFilm = repository.get(request.getId())
                .map(film -> FilmMapper.updateFilmFields(film, request))
                .orElseThrow(() -> new NotFoundException("Фильм с ID " + request.getId() + " не найден"));
        updatedFilm = repository.update(updatedFilm);
        return FilmMapper.mapToFilmDto(updatedFilm);
    }

    public boolean delete(Long id) {
        return repository.delete(id);
    }

    public boolean putLike(Long id, Long userId) {
        checkId(id);
        userService.checkId(userId);
        return repository.putLike(id, userId);
    }

    public boolean deleteLike(Long id, Long userId) {
        checkId(id);
        userService.checkId(userId);
        return repository.deleteLike(id, userId);
    }

    public List<Film> getTopFilms(int size) {
        return repository.getTopFilms(size);
    }

    public void checkId(Long id) {
        if (repository.get(id).isEmpty()) {
            throw new NotFoundException("Объекта с ID " + id + " не существует");
        }
    }
}
