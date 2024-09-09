package ru.yandex.practicum.filmorate.repository.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.repository.BaseRepository;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.RatingRepository;

import java.util.List;
import java.util.Optional;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingRepositoryImpl extends BaseRepository<Rating> implements RatingRepository {
    static String FIND_BY_ID_QUERY = "SELECT * FROM ratings WHERE id = ?";
    static String FIND_ALL_QUERY = "SELECT * FROM ratings";

    public RatingRepositoryImpl(JdbcTemplate jdbc, RowMapper<Rating> mapper) {
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
