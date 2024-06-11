package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Set;

@Service
public class UserService extends BaseService<User> {

    private final InMemoryUserStorage inMemoryStorage;

    public UserService(InMemoryUserStorage inMemoryStorage) {
        super(inMemoryStorage);
        this.inMemoryStorage = inMemoryStorage;
    }

    @Override
    public User save(User entity) {
        entity.setId(getNextId());
        if (entity.getName() == null || entity.getName().isBlank()) {
            entity.setName(entity.getLogin());
        }
        return inMemoryStorage.save(entity);
    }

    @Override
    public User update(User newEntity) {
        if (newEntity.getId() == null) {
            throw new ValidationException("Должен быть указан ID");
        }
        if (inMemoryStorage.getStorage().containsKey(newEntity.getId())) {
            User oldUser = inMemoryStorage.getStorage().get(newEntity.getId());
            oldUser.setLogin(newEntity.getLogin());
            oldUser.setEmail(newEntity.getEmail());
            oldUser.setBirthday(newEntity.getBirthday());
            if (!(newEntity.getName() == null || newEntity.getName().isBlank())) {
                oldUser.setName(newEntity.getName());
            }
            return inMemoryStorage.update(oldUser);
        }
        throw new NotFoundException("Пользователя с ID " + newEntity.getId() + " не существует");
    }

    public Set<User> getFriends(Long id) {
        checkId(id);
        return inMemoryStorage.getFriends(id);
    }

    public Set<User> getCommonFriends(Long id, Long otherId) {
        checkId(id);
        checkId(otherId);
        return inMemoryStorage.getCommonFriends(id, otherId);
    }

    public boolean addFriend(Long id, Long otherId) {
        if (id.equals(otherId)) {
            throw new RuntimeException("Нельзя добавить самого себя в друзья");
        }
        checkId(id);
        checkId(otherId);
       return inMemoryStorage.addFriend(id, otherId);
    }

    public boolean deleteFriend(Long id, Long otherId) {
        if (id.equals(otherId)) {
            throw new RuntimeException("Нельзя удалить самого себя из друзей");
        }
        checkId(id);
        checkId(otherId);
        return inMemoryStorage.deleteFriend(id, otherId);
    }

}
