package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import java.util.*;

@Repository
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM user WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM user";
    private static final String INSERT_QUERY = "INSERT INTO user(email, login, name, birthday)" +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE user SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM user WHERE id = ?";
    private static final String FIND_FRIENDS_QUERY = "SELECT * FROM user WHERE id IN " +
            "(SELECT friend_id FROM user_friend " +
            "WHERE user_id = ? " +
            "UNION ALL " +
            "SELECT user_id FROM user_friend " +
            "WHERE friend_id = ? AND is_accept = true)";
    private static final String FIND_COMMON_FRIENDS = "SELECT * FROM user WHERE id IN " +
            "(SELECT * FROM" +
            "(SELECT friend_id  FROM user_friend " +
            "WHERE user_id = ? OR user_id = ? " +
            "UNION ALL " +
            "SELECT user_id FROM user_friend " +
            "WHERE (friend_id = ? OR friend_id = ?) AND is_accept = true) " +
            "GROUP BY friend_id " +
            "HAVING COUNT(*) > 1);";

    private static final String ADD_FRIEND_QUERY = "INSERT INTO user_friend(user_id, friend_id, is_accept)" +
            "VALUES (?, ?, false)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM user_friend WHERE user_id = ? AND friend_id = ?";
    private static final String IS_FRIEND_REQUEST = "SELECT * FROM user_friend WHERE friend_id = ? AND user_id = ? AND is_accept = false";
    private static final String ACCEPT_REQUEST = "UPDATE user_friend SET is_accept = true WHERE user_id = ? AND friend_id = ?";
    private static final String IS_FRIEND = "SELECT * FROM user_friend WHERE friend_id = ? AND user_id = ? AND is_accept = true";
    private static final String REMOVE_REQUEST = "UPDATE user_friend SET is_accept = false WHERE friend_id = ? AND user_id = ?";

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
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                newUser.getBirthday(),
                newUser.getId()
        );
        return newUser;
    }

    @Override
    public boolean delete(Long id) {
        return delete(DELETE_QUERY, id);
    }

    @Override
    public Set<User> getFriends(Long id) {
        List<User> users = jdbc.query(FIND_FRIENDS_QUERY, mapper, id, id);
        return new HashSet<>(users);
    }

    @Override
    public Set<User> getCommonFriends(Object... params) {
        List<User> users = jdbc.query(FIND_COMMON_FRIENDS, mapper, params[0], params[1], params[0], params[1]);
        return new HashSet<>(users);
    }

    @Override
    public boolean addFriend(Object... params) {
        return jdbc.update(ADD_FRIEND_QUERY, params) > 0;
    }

    @Override
    public boolean deleteFriend(Object... params) {
        return jdbc.update(DELETE_FRIEND_QUERY, params) > 1;
    }

    @Override
    public boolean isFriendRequest(Object... params) {
        return jdbc.queryForRowSet(IS_FRIEND_REQUEST, params).next();
    }

    @Override
    public boolean acceptRequest(Object... params) {
        return jdbc.update(ACCEPT_REQUEST, params) > 1;
    }

    @Override
    public boolean isFriend(Object... params) {
        return jdbc.queryForRowSet(IS_FRIEND, params).next();
    }

    @Override
    public boolean removeRequest(Object... params) {
        return jdbc.update(REMOVE_REQUEST, params) > 1;
    }
}
