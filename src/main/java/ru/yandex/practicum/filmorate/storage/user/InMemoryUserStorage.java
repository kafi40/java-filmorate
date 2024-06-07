package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryStorage;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage extends InMemoryStorage<User> implements UserStorage {

    @Override
    public Set<User> getFriends(Long id) {
        return getStorage().get(id).getFriendsSet();
    }

    @Override
    public Set<User> getCommonFriends(Long id, Long otherId) {
        return getStorage().get(id).getFriendsSet().stream()
                .filter(getStorage().get(otherId).getFriendsSet()::contains)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean addFriend(Long id, Long otherId) {
        return getStorage().get(id).getFriendsSet().add(getStorage().get(otherId));
    }

    @Override
    public boolean deleteFriend(Long id, Long otherId) {
        return getStorage().get(id).getFriendsSet().remove(getStorage().get(otherId));
    }
}
