package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Entity;


import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@EqualsAndHashCode
@Data
@Component
public abstract class InMemoryStorage<T extends Entity> implements BaseStorage<T> {
    private final HashMap<Long, T> storage;

    protected InMemoryStorage() {
        this.storage = new HashMap<>();
    }

    @Override
    public Optional<T> get(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<T> getAll() {
        return storage.values().stream().toList();
    }

    @Override
    public T save(T entity) {
        storage.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public T update(T newEntity) {
        return storage.put(newEntity.getId(), newEntity);
    }

    @Override
    public T delete(Long id) {
        return storage.remove(id);
    }
}
