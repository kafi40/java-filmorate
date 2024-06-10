package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.List;

public interface FilmStorage extends BaseStorage<Film> {

    boolean putLike(Long id, Long userId);

    boolean deleteLike(Long id, Long userid);

    List<Film> getTopFilms(int size);

}
