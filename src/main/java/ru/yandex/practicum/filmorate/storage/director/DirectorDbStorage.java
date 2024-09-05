package ru.yandex.practicum.filmorate.storage.director;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class DirectorDbStorage extends BaseDbStorage<Director> implements DirectorStorage {
    private static final String INSERT_QUERY = "INSERT INTO director(name) VALUES (?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM director WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM director";
    private static final String DELETE_QUERY = "DELETE FROM director WHERE id = ?";
    private static final String UPDATE_QUERY = " UPDATE director SET name = ? WHERE id = ?";
    private static final String FIND_DIRECTOR_FOR_FILM = """
            SELECT d.id, d.name FROM director AS d
            JOIN film_director AS fd ON d.id = fd.director_id
            JOIN film AS f ON fd.film_id = f.id
            WHERE f.id = ?
            """;

    public DirectorDbStorage(JdbcTemplate jdbc, RowMapper<Director> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Director> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Director> findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public boolean delete(Long id) {
        return delete(DELETE_QUERY, id);
    }

    @Override
    public Set<Director> getForFilm(Long id) {
        return new HashSet<>(jdbc.query(FIND_DIRECTOR_FOR_FILM, mapper, id));
    }

    @Override
    public Director save(Director director) {
        Long id = insert(
                INSERT_QUERY,
                director.getName()
        );
        director.setId(id);
        return director;
    }

    @Override
    public Director update(Director newDirector) {
        update(
                UPDATE_QUERY,
                newDirector.getName(),
                newDirector.getId()
        );
        return newDirector;
    }
}
