package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.Set;

public interface UserStorage extends BaseStorage<User> {
    Set<User> getFriends(Long id);

    Set<User> getCommonFriends(Long id, Long otherId);

    boolean addFriend(Long id, Long otherId);

    boolean deleteFriend(Long id, Long otherId);
}
