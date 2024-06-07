package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

@Service
public class FilmService extends BaseService<Film> {
    public FilmService(InMemoryFilmStorage storage) {
        super(storage);
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
        throw new ValidationException("Фильма с ID " + entity.getId() + " не существует");
    }
}
