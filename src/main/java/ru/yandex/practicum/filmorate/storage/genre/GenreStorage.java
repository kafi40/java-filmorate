package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    Optional<Genre> get(Long id);

    List<Genre> getAll();

    List<Genre> getForFilm(Long id);
}
