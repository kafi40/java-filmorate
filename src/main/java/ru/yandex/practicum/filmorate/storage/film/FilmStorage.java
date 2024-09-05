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

    List<Film> getTopFilms(int size, Long genreId, Integer year);

    void addGenreForFilm(Long id, Long genreId);

    List<Film> findCommonFilms(Long userId, Long friendId);
}
