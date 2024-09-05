package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DirectorService {
    private final DirectorStorage directorStorage;


    public DirectorService(@Qualifier("directorDbStorage") DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    public List<Director> getAll() {
        return directorStorage.findAll();
    }

    public Optional<Director> getById(Long id) {
        if (directorStorage.findById(id).isEmpty()) {
            throw new NotFoundException("error");
        }
        return directorStorage.findById(id);
    }

    public boolean deleteById(Long id) {
        return directorStorage.delete(id);
    }

    public Set<Director> getForFilm(Long id) {
        return directorStorage.getForFilm(id);
    }

    public Director save(Director director) {
        return directorStorage.save(director);
    }

    public Director update(Director newDirector) {
        if (directorStorage.findById(newDirector.getId()).isEmpty()) {
            throw new NotFoundException("Пользователя с id = " + newDirector.getId() + " не существует");
        }
        return directorStorage.update(newDirector);
    }
}
