package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {
    Optional<User> get(Long id);

    List<User> getAll();

    User save(User user);

    User update(User newUser);

    User delete(Long id);

    Set<User> getFriends(Long id);

    Set<User> getCommonFriends(Long id, Long otherId);

    boolean addFriend(Long id, Long otherId);

    boolean deleteFriend(Long id, Long otherId);
}
