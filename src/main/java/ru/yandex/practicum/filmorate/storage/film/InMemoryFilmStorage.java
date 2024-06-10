package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage extends InMemoryStorage<Film> implements FilmStorage{

    @Override
    public boolean putLike(Long id, Long userId) {
        checkId(id, userId);
        return getStorage().get(id).getLikesSet().add(userId);
    }

    @Override
    public boolean deleteLike(Long id, Long userId) {
        checkId(id, userId);
        return getStorage().get(id).getLikesSet().remove(userId);
    }

    @Override
    public List<Film> getTopFilms(int size) {
        return getStorage().values().stream()
                .sorted(Comparator.comparing(film -> -film.getLikesSet().size()))
                .limit(size)
                .collect(Collectors.toList());
    }
}
