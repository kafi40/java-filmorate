package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.controller.model.film.FilmDto;
import ru.yandex.practicum.filmorate.controller.model.film.FilmRequest;

import java.util.List;

public interface FilmService extends BaseService<FilmDto, FilmRequest> {
    boolean putLike(Long id, Long userId);

    boolean deleteLike(Long id, Long userId);

    List<FilmDto> getTopFilms(int count);

    List<FilmDto> getCommonFilms(Long userId, Long friendId);
}
