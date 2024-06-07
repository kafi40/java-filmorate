package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface BaseStorage<T> {
    T get(Long id);
    List<T> getAll();
    T save(T entity);
    T update(T newEntity);
    T delete(Long id);

}
