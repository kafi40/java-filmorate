package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.storage.InMemoryStorage;
import java.util.List;

@RequiredArgsConstructor
public abstract class BaseService<T extends Entity> {

    protected final InMemoryStorage<T> inMemoryStorage;

    public T get(Long id) {
        if (inMemoryStorage.get(id).isPresent()) {
            return inMemoryStorage.get(id).get();
        } else {
            throw new NotFoundException("Объект с ID = " + id + " не найден");
        }
    }

    public List<T> getAll() {
        return inMemoryStorage.getAll();
    }

    public T save(T entity) {
        entity.setId(getNextId());
        return inMemoryStorage.save(entity);
    }

    public T update(T newEntity) {
        return inMemoryStorage.update(newEntity);
    }

    public T delete(Long id) {
        return inMemoryStorage.delete(id);
    }

    protected long getNextId() {
        long currentMaxId = inMemoryStorage.getStorage().keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public boolean checkId(Long id) {
        if (inMemoryStorage.get(id).isPresent()) {
            return true;
        } else {
            throw new NotFoundException("Объекта с ID " + id + " не существует");
        }
    }
}
