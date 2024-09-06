package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.controller.model.genre.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreService {
    GenreDto get(Long id);

    List<GenreDto> getAll();
}
