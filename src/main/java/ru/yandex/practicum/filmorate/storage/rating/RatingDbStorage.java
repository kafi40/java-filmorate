package ru.yandex.practicum.filmorate.storage.rating;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class RatingDbStorage extends BaseDbStorage<Rating> implements RatingStorage {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM rating WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM rating";

    public RatingDbStorage(JdbcTemplate jdbc, RowMapper<Rating> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Rating> get(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<Rating> getAll() {
        return findMany(FIND_ALL_QUERY);
    }
}
