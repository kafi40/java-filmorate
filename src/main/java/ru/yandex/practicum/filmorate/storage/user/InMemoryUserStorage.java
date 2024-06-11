package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryStorage;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage extends InMemoryStorage<User> implements UserStorage {

    @Override
    public Set<User> getFriends(Long id) {
        User user = getStorage().get(id);
        return getStorage().entrySet().stream()
                .filter(entry -> user.getFriendsSet().contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> getCommonFriends(Long id, Long otherId) {
        Set<Long> commonFriends = getStorage().get(id).getFriendsSet().stream()
                .filter(getStorage().get(otherId).getFriendsSet()::contains)
                .collect(Collectors.toSet());

        return getStorage().entrySet().stream()
                .filter(entry -> commonFriends.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean addFriend(Long id, Long otherId) {
        getStorage().get(otherId).getFriendsSet().add(id);
        return getStorage().get(id).getFriendsSet().add(otherId);
    }

    @Override
    public boolean deleteFriend(Long id, Long otherId) {
        getStorage().get(otherId).getFriendsSet().remove(id);
        return getStorage().get(id).getFriendsSet().remove(otherId);
    }
}
