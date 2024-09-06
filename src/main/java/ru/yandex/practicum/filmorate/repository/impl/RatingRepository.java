package ru.yandex.practicum.filmorate.repository.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.repository.BaseRepository;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;
import java.util.Optional;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@FieldNameConstants
public class RatingRepository extends BaseRepository<Rating> implements ru.yandex.practicum.filmorate.repository.RatingRepository {
    static String FIND_BY_ID_QUERY = "SELECT * FROM rating WHERE id = ?";
    static String FIND_ALL_QUERY = "SELECT * FROM rating";

    public RatingRepository(JdbcTemplate jdbc, RowMapper<Rating> mapper) {
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
