package ru.yandex.practicum.filmorate.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.model.film.FilmDto;
import ru.yandex.practicum.filmorate.controller.model.film.FilmRequest;
import ru.yandex.practicum.filmorate.controller.model.genre.GenreRequest;
import ru.yandex.practicum.filmorate.controller.model.director.DirectorRequest;
import ru.yandex.practicum.filmorate.exception.ElementNotExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.controller.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.util.Util;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilmServiceImpl implements FilmService {
    FilmRepository filmRepository;
    RatingRepository ratingRepository;
    GenreRepository genreRepository;
    UserRepository userRepository;
    DirectorRepository directorRepository;

    public FilmDto get(Long id) {
        return filmRepository.get(id)
                .map(FilmMapper::mapToFilmDto)
                .orElseThrow(() -> new NotFoundException("Фильм с ID = " + id + " не найден"));
    }

    public List<FilmDto> getAll() {
        return filmRepository.getAll().stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public FilmDto save(FilmRequest request) {
        Film film = FilmMapper.mapToFilm(request);
        addRating(film, request);
        film = filmRepository.save(film);
        addGenres(film, request);
        addDirector(film, request);
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto update(FilmRequest request) {
        if (request.getId() == null) {
            throw new ValidationException("ID","Должен быть указан ID");
        }

        Film updatedFilm = filmRepository.get(request.getId())
                .map(film -> FilmMapper.updateFilmFields(film, request))
                .orElseThrow(() -> new NotFoundException("Фильм с ID " + request.getId() + " не найден"));
        addRating(updatedFilm, request);
        addGenres(updatedFilm, request);
        addDirector(updatedFilm, request);
        updatedFilm = filmRepository.update(updatedFilm);
        return FilmMapper.mapToFilmDto(updatedFilm);
    }

    public boolean delete(Long id) {
        return filmRepository.delete(id);
    }

    public boolean putLike(Long id, Long userId) {
        Util.checkId(filmRepository, id);
        Util.checkId(userRepository, id);
        return filmRepository.putLike(id, userId);
    }

    public boolean deleteLike(Long id, Long userId) {
        Util.checkId(filmRepository, id);
        Util.checkId(userRepository, id);
        return filmRepository.deleteLike(id, userId);
    }

    public List<FilmDto> getTopFilms(int count, Long genreId, Integer year) {
        if (genreId != null) {
            Util.checkId(genreRepository, genreId);
        }
        return filmRepository.getTopFilms(count, genreId, year).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public List<FilmDto> getCommonFilms(Long userId, Long friendId) {
        Util.checkId(userRepository, userId);
        Util.checkId(userRepository, friendId);
        return filmRepository.findCommonFilms(userId, friendId).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public List<FilmDto> getDirectorsFilmsByYear(Long id) {
        return filmRepository.getDirectorsFilmSortByYear(id).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public List<FilmDto> getDirectorsFilmsByLikes(Long id) {
        return filmRepository.getDirectorsFilmSortByLikes(id).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }


    public List<FilmDto> getSearchFilm(String query) {
        return filmRepository.getSearchFilm(query).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public List<FilmDto> getSearchDirector(String query) {
        return filmRepository.getSearchDirector(query).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }



    private void addRating(Film film, FilmRequest request) {
        Long ratingId = request.getMpa().getId();
        Rating mpa = ratingRepository.get(ratingId)
                .orElseThrow(() -> new ValidationException("ID", "Рейтинг с ID " + ratingId + " не найден"));
        film.setMpa(mpa);
    }

    private void addGenres(Film film, FilmRequest request) {
        if (request.getGenres() == null) {
            film.setGenres(new HashSet<>());
            return;
        }

        Set<Genre> genres = request.getGenres().stream()
                .map(GenreRequest::getId)
                .map(genreRepository::get)
                .map(genre -> genre
                        .orElseThrow(() -> new ElementNotExistsException("Жанр не найден")))
                .peek(genre -> filmRepository.addGenreForFilm(film.getId(), genre.getId()))
                .collect(Collectors.toSet());
        film.setGenres(genres);
    }

    private void addDirector(Film film, FilmRequest request) {
        if (request.getDirectors() == null) {
            film.setDirectors(new HashSet<>());
            return;
        }

        Set<Director> directors = request.getDirectors().stream()
                .map(DirectorRequest::getId)
                .map(directorRepository::findById)
                .map(director -> director
                        .orElseThrow(() -> new ElementNotExistsException("Режиссер не найден")))
                .peek(director -> filmRepository.addDirectorForFilm(film.getId(), director.getId()))
                .collect(Collectors.toSet());
        film.setDirectors(directors);
    }
}
