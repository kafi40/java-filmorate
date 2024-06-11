package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Optional;

public interface BaseStorage<T> {
    Optional<T> get(Long id);

    List<T> getAll();

    T save(T entity);

    T update(T newEntity);

    T delete(Long id);
}
