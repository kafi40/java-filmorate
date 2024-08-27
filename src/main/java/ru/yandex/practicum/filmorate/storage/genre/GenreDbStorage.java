package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class GenreDbStorage extends BaseDbStorage<Genre> implements GenreStorage {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genre WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM genre";
    private static final String FIND_GENRE_FOR_FILM =
            "SELECT g.id, g.name FROM genre AS g " +
            "JOIN film_genre AS fg " +
            "ON g.id = fg.genre_id " +
            "JOIN film AS f " +
            "ON fg.film_id = f.id " +
            "WHERE f.id = ?";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Genre> get(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<Genre> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Set<Genre> getForFilm(Long id) {
        return new HashSet<>(jdbc.query(FIND_GENRE_FOR_FILM, mapper, id));
    }
}
