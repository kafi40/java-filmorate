package ru.yandex.practicum.filmorate.storage.activity;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Activity;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class ActivityDbStorage extends BaseDbStorage<Activity> implements ActivityStorage {

    private static final String GET_USER_FEED_QUERY = """
            SELECT "event_id", "user_id", "operation", "event_type", "timestamp", "entity_id" FROM "users_feed"
            WHERE "user_id" = ?;
            """;

    public ActivityDbStorage(JdbcTemplate jdbc, RowMapper<Activity> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Activity> getUserFeed(Long userId) {
        return findMany(GET_USER_FEED_QUERY, userId);
    }
}
