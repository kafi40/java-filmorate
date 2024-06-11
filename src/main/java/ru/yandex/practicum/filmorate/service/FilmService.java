package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;

@Service
public class FilmService extends BaseService<Film> {
    private final InMemoryFilmStorage inMemoryStorage;
    private final UserService userService;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryStorage, UserService userService) {
        super(inMemoryStorage);
        this.inMemoryStorage = inMemoryStorage;
        this.userService = userService;
    }

    @Override
    public Film update(Film entity) {
        if (entity.getId() == null) {
            throw new ValidationException("Должен быть указан ID");
        }
        if (inMemoryStorage.getStorage().containsKey(entity.getId())) {
            Film oldFilm = inMemoryStorage.getStorage().get(entity.getId());
            oldFilm.setName(entity.getName());
            oldFilm.setDescription(entity.getDescription());
            oldFilm.setReleaseDate(entity.getReleaseDate());
            oldFilm.setDuration(entity.getDuration());
            return oldFilm;
        }
        throw new NotFoundException("Фильма с ID " + entity.getId() + " не существует");
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
}
