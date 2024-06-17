package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final InMemoryUserStorage inMemoryStorage;

    public UserService(InMemoryUserStorage inMemoryStorage) {
        this.inMemoryStorage = inMemoryStorage;
    }

    public User get(Long id) {
        if (inMemoryStorage.get(id).isPresent()) {
            return inMemoryStorage.get(id).get();
        } else {
            throw new NotFoundException("Объект с ID = " + id + " не найден");
        }
    }

    public List<User> getAll() {
        return inMemoryStorage.getAll();
    }

    public User save(User user) {
        user.setId(getNextId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return inMemoryStorage.save(user);
    }

    public User delete(Long id) {
        return inMemoryStorage.delete(id);
    }

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

    protected long getNextId() {
        long currentMaxId = inMemoryStorage.getStorage().keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public void checkId(Long id) {
        if (inMemoryStorage.get(id).isEmpty()) {
            throw new NotFoundException("Объекта с ID " + id + " не существует");
        }
    }

}
