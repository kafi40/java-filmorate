package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Entity;
import java.util.HashMap;
import java.util.List;

@EqualsAndHashCode
@Data
public abstract class InMemoryStorage<T extends Entity> implements BaseStorage<T> {
    private final HashMap<Long, T> storage;

    protected InMemoryStorage() {
        this.storage = new HashMap<>();
    }

    @Override
    public T get(Long id) {
        checkId(id);
        return storage.get(id);
    }

    @Override
    public List<T> getAll() {
        return storage.values().stream().toList();
    }

    @Override
    public T save(T entity) {
        return storage.put(entity.getId(), entity);
    }

    @Override
    public T update(T newEntity) {
        return storage.put(newEntity.getId(), newEntity);
    }

    @Override
    public T delete(Long id) {
        checkId(id);
        return storage.remove(id);
    }

    protected void checkId(Long... arg) {
        for (Long aLong : arg) {
            if (storage.get(aLong) == null) {
                throw new NotFoundException("Объект с ID = " + aLong + " не найден");
            }
        }
    }
}
