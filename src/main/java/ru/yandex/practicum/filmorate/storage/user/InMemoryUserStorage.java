package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Data
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Long, User> storage;

    protected InMemoryUserStorage() {
        this.storage = new HashMap<>();
    }

    @Override
    public Optional<User> get(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<User> getAll() {
        return storage.values().stream().toList();
    }

    @Override
    public User save(User user) {
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User newUser) {
        return storage.put(newUser.getId(), newUser);
    }

    @Override
    public boolean delete(Long id) {
        return storage.remove(id) != null;
    }

    @Override
    public Set<User> getFriends(Long id) {
        User user = storage.get(id);
        return storage.entrySet().stream()
                .filter(entry -> user.getFriendsSet().contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> getCommonFriends(Long id, Long otherId) {
        Set<Long> commonFriends = storage.get(id).getFriendsSet().stream()
                .filter(storage.get(otherId).getFriendsSet()::contains)
                .collect(Collectors.toSet());

        return storage.entrySet().stream()
                .filter(entry -> commonFriends.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean addFriend(Long id, Long otherId) {
        storage.get(otherId).getFriendsSet().add(id);
        return storage.get(id).getFriendsSet().add(otherId);
    }

    @Override
    public boolean deleteFriend(Long id, Long otherId) {
        storage.get(otherId).getFriendsSet().remove(id);
        return storage.get(id).getFriendsSet().remove(otherId);
    }
}
