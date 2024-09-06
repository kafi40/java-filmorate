package ru.yandex.practicum.filmorate.storage.activity;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Activity;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.Set;

@Repository
public class ActivityDbStorage extends BaseDbStorage<Activity> implements ActivityStorage {


    public ActivityDbStorage(JdbcTemplate jdbc, RowMapper<Activity> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Set<Activity> getUserFeed(Long userId) {
        return Set.of();
    }
}
