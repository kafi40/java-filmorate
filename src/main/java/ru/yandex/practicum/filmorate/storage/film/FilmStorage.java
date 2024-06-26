package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Optional<Film> get(Long id);

    List<Film> getAll();

    Film save(Film film);

    Film update(Film newFilm);

    Film delete(Long id);

    boolean putLike(Long id, Long userId);

    boolean deleteLike(Long id, Long userid);

    List<Film> getTopFilms(int size);

}
