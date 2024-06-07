package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Set;

@Service
public class UserService extends BaseService<User> {

    private final InMemoryUserStorage storage;
    public UserService(InMemoryUserStorage storage) {
        super(storage);
        this.storage = (InMemoryUserStorage) inMemoryStorage;
    }

    @Override
    public User save(User entity) {
        entity.setId(getNextId());
        if (entity.getName() == null || entity.getName().isBlank()) {
            entity.setName(entity.getLogin());
        }
        return storage.save(entity);
    }

    @Override
    public User update(User newEntity) {
        if (newEntity.getId() == null) {
            throw new ValidationException("Должен быть указан ID");
        }
        if (storage.getStorage().containsKey(newEntity.getId())) {
            User oldUser = storage.getStorage().get(newEntity.getId());
            oldUser.setLogin(newEntity.getLogin());
            oldUser.setEmail(newEntity.getEmail());
            oldUser.setBirthday(newEntity.getBirthday());
            if (!(newEntity.getName() == null || newEntity.getName().isBlank())) {
                oldUser.setName(newEntity.getName());
            }
            return oldUser;
        }
        throw new ValidationException("Пользователя с ID " + newEntity.getId() + " не существует");
    }

    public Set<User> getFriends(Long id) {
        return storage.get(id).getFriendsSet();
    }

    public Set<User> getCommonFriends(Long id, Long otherId) {
        return storage.getCommonFriends(id, otherId);
    }

    public boolean addFriend(Long id, Long otherId) {
       return storage.addFriend(id, otherId);
    }

    public boolean deleteFriend(Long id, Long otherId) {
        return storage.deleteFriend(id, otherId);
    }

}
