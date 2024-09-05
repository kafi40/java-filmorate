package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Optional<Film> get(Long id);

    List<Film> getAll();

    Film save(Film film);

    Film update(Film newFilm);

    boolean delete(Long id);

    boolean putLike(Long id, Long userId);

    boolean deleteLike(Long id, Long userId);

    List<Film> getTopFilms(int size);

    List<Film> getTopFilmsByYearAndGenre(int count, Long genreId, int year);

    List<Film> getTopFilmsByYear(int count, int year);

    List<Film> getTopFilmsByGenre(int count, Long genreId);

    void addGenreForFilm(Long id, Long genreId);

    List<Film> findCommonFilms(Long userId, Long friendId);
}
