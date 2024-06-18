package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Data
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> storage;

    protected InMemoryFilmStorage() {
        this.storage = new HashMap<>();
    }

    @Override
    public Optional<Film> get(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Film> getAll() {
        return storage.values().stream().toList();
    }

    @Override
    public Film save(Film film) {
        storage.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        return storage.put(newFilm.getId(), newFilm);
    }

    @Override
    public Film delete(Long id) {
        return storage.remove(id);
    }

    @Override
    public boolean putLike(Long id, Long userId) {
        return storage.get(id).getLikesSet().add(userId);
    }

    @Override
    public boolean deleteLike(Long id, Long userId) {
        return storage.get(id).getLikesSet().remove(userId);
    }

    @Override
    public List<Film> getTopFilms(int size) {
        return storage.values().stream()
                .sorted(Comparator.comparing(film -> -film.getLikesSet().size()))
                .limit(size)
                .collect(Collectors.toList());
    }
}
