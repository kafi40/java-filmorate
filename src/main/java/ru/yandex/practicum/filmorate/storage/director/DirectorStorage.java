package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DirectorStorage {

    List<Director> findAll();

    Optional<Director> findById(Long id);

    boolean delete(Long id);

    Set<Director> getForFilm(Long id);

    Director save(Director director);

    public Director update(Director newDirector);
}
