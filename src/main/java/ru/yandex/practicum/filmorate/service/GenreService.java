package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import java.util.List;
import java.util.Set;

@Service
public class GenreService {
    private final GenreStorage repository;

    public GenreService(GenreStorage repository) {
        this.repository = repository;
    }

    public Genre get(Long id) {
        return repository.get(id)
                .orElseThrow(() -> new NotFoundException("Жанр с ID = " + id + " не найден"));
    }

    public List<Genre> getAll() {
        return repository.getAll();
    }

    public Set<Genre> getForFilm(Long id) {
        return repository.getForFilm(id);
    }
}