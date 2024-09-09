package ru.yandex.practicum.filmorate.repository.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.repository.BaseRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@FieldNameConstants
public class UserRepositoryImpl extends BaseRepository<User> implements UserRepository {
    static String FIND_BY_ID_QUERY = "SELECT * FROM user WHERE id = ?";
    static String FIND_ALL_QUERY = "SELECT * FROM user";
    static String INSERT_QUERY = "INSERT INTO user(email, login, name, birthday)" +
            "VALUES (?, ?, ?, ?)";
    static String UPDATE_QUERY = "UPDATE user SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    static String DELETE_QUERY = "DELETE FROM user WHERE id = ?";
    static String FIND_FRIENDS_QUERY =
            """
                    SELECT * FROM "user" WHERE "id" IN (
                    SELECT "friend_id" FROM "user_friend"
                    WHERE "user_id" = ?
                    UNION ALL
                    SELECT "user_id" FROM "user_friend"
                    WHERE "friend_id" = ? AND "is_accept" = true)
                    """;

    static String FIND_COMMON_FRIENDS =
            """
                    SELECT * FROM "user" WHERE "id" IN (
                    SELECT * FROM (
                    SELECT "friend_id"  FROM "user_friend"
                    WHERE "user_id" = ? OR "user_id" = ?
                    UNION ALL
                    SELECT "user_id" FROM "user_friend"
                    WHERE ("friend_id" = ? OR "friend_id" = ?) AND "is_accept" = true) as cf
                    GROUP BY "friend_id"
                    HAVING COUNT(*) > 1)
                    """;

    static String ADD_FRIEND_QUERY =
            """
                    INSERT INTO "user_friend"("user_id", "friend_id", "is_accept")
                    VALUES (?, ?, false)
                    """;
    static String DELETE_FRIEND_QUERY =
            """
                    DELETE FROM "user_friend" WHERE "user_id" = ? AND "friend_id" = ?
                    """;
    static String IS_FRIEND_REQUEST =
            """
                    SELECT * FROM "user_friend" WHERE "friend_id" = ? AND "user_id" = ? AND "is_accept" = false
                    """;
    static String ACCEPT_REQUEST =
            """
                    UPDATE "user_friend" SET "is_accept" = true WHERE "user_id" = ? AND "friend_id" = ?
                    """;
    static String IS_FRIEND =
            """
                    SELECT * FROM "user_friend" WHERE "friend_id" = ? AND "user_id" = ? AND "is_accept" = true
                    """;
    static String REMOVE_REQUEST =
            """
                    UPDATE "user_friend" SET "is_accept" = false WHERE "friend_id" = ? AND "user_id" = ?
                    """;

    public UserRepositoryImpl(JdbcTemplate jdbc, RowMapper<User> mapper) {
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
