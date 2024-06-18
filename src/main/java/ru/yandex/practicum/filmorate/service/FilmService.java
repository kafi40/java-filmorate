package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import java.util.List;

@Service
public class FilmService {
    private final InMemoryFilmStorage inMemoryStorage;
    private final UserService userService;

    public FilmService(InMemoryFilmStorage inMemoryStorage, UserService userService) {
        this.inMemoryStorage = inMemoryStorage;
        this.userService = userService;
    }

    public Film get(Long id) {
        if (inMemoryStorage.get(id).isPresent()) {
            return inMemoryStorage.get(id).get();
        } else {
            throw new NotFoundException("Объект с ID = " + id + " не найден");
        }
    }

    public List<Film> getAll() {
        return inMemoryStorage.getAll();
    }

    public Film save(Film film) {
        film.setId(getNextId());
        return inMemoryStorage.save(film);
    }

    public Film update(Film film) {
        if (film.getId() == null) {
            throw new ValidationException("Должен быть указан ID");
        }
        if (inMemoryStorage.getStorage().containsKey(film.getId())) {
            Film oldFilm = inMemoryStorage.getStorage().get(film.getId());
            oldFilm.setName(film.getName());
            oldFilm.setDescription(film.getDescription());
            oldFilm.setReleaseDate(film.getReleaseDate());
            oldFilm.setDuration(film.getDuration());
            return inMemoryStorage.update(oldFilm);
        }
        throw new NotFoundException("Фильма с ID " + film.getId() + " не существует");
    }

    public Film delete(Long id) {
        return inMemoryStorage.delete(id);
    }

    public boolean putLike(Long id, Long userId) {
        checkId(id);
        userService.checkId(userId);
        return inMemoryStorage.putLike(id, userId);
    }

    public boolean deleteLike(Long id, Long userId) {
        checkId(id);
        userService.checkId(userId);
        return inMemoryStorage.deleteLike(id, userId);
    }

    public List<Film> getTopFilms(int size) {
        return inMemoryStorage.getTopFilms(size);
    }

    protected long getNextId() {
        long currentMaxId = inMemoryStorage.getStorage().keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public void checkId(Long id) {
        if (inMemoryStorage.get(id).isEmpty()) {
            throw new NotFoundException("Объекта с ID " + id + " не существует");
        }
    }
}
