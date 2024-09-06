package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.controller.model.genre.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GenreService {
    private final GenreStorage genreStorage;

public interface GenreService {
    GenreDto get(Long id);

    public GenreDto get(Long id) {
        return genreStorage.get(id)
                .map(GenreMapper::mapToGenreDto)
                .orElseThrow(() -> new NotFoundException("Жанр с ID = " + id + " не найден"));
    }

    public List<GenreDto> getAll() {
        return genreStorage.getAll().stream()
                .map(GenreMapper::mapToGenreDto)
                .collect(Collectors.toList());
    }

    public void checkId(Long id) {
        if (genreStorage.get(id).isEmpty()) {
            throw new NotFoundException("Жанра с ID " + id + " не существует");
        }
    }

    public Set<Genre> getForFilm(Long id) {
        return genreStorage.getForFilm(id);
    }
    List<GenreDto> getAll();
}
