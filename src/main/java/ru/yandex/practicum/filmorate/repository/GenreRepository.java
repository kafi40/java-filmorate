package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreRepository {
    Optional<Genre> get(Long id);

    List<Genre> getAll();

    Set<Genre> getForFilm(Long id);
}