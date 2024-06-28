package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM user WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM user";
    private static final String INSERT_QUERY = "INSERT INTO user(email, login, name, birthday)" +
            "VALUES (?, ?, ?, ?) returning id";
    private static final String UPDATE_QUERY = "UPDATE user SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM user WHERE id = ?";
    private static final String FIND_FRIENDS_QUERY = "SELECT * FROM user WHERE id IN (" +
            "SELECT user_friend_id FROM user_friend WHERE user_id = ?" +
            ")";
    private static final String ADD_FRIEND_QUERY = "INSERT INTO user_friend(user_id, user_friend_id)" +
            "VALUES (?, ?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM user_friend WHERE user_id = ? AND user_friend_id = ?";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<User> get(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<User> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public User save(User user) {
        long id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    @Override
    public User update(User newUser) {
        update(
                UPDATE_QUERY,
                newUser.getId(),
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                newUser.getBirthday()
        );
        return newUser;
    }

    @Override
    public boolean delete(Long id) {
        return delete(DELETE_QUERY, id);
    }

    @Override
    public Set<User> getFriends(Long id) {
        List<User> users = jdbc.query(FIND_FRIENDS_QUERY, mapper, id);
        return new HashSet<>(users);
    }

    @Override
    public Set<User> getCommonFriends(Long id, Long otherId) {
        return null;
    }

    @Override
    public boolean addFriend(Long id, Long otherId) {
        int rowUpdate = jdbc.update(ADD_FRIEND_QUERY, id, otherId);
        return rowUpdate > 0;
    }

    @Override
    public boolean deleteFriend(Long id, Long otherId) {
        int rowDelete = jdbc.update(DELETE_FRIEND_QUERY, id, otherId);
        return rowDelete > 0;
    }
}
