package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM film WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM film";
    private static final String INSERT_QUERY = "INSERT INTO film(name, description, release_date, duration, rating_id)" +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, " +
            "rating_id = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM film WHERE id = ?";
    private static final String FIND_TOP_FILMS = "SELECT id, name, description, release_date, duration, rating_id FROM film " +
            "LEFT JOIN user_film_liked ufl ON film.id = ufl.film_id " +
            "GROUP BY id, name, description, release_date, duration, rating_id " +
            "ORDER BY COUNT(*) DESC " +
            "LIMIT ?";
    private static final String ADD_LIKE = "INSERT INTO user_film_liked(user_id, film_id) VALUES (?, ?)";
    private static final String DELETE_LIKE = "DELETE FROM user_film_liked WHERE user_id = ? AND film_id = ?";
    private static final String ADD_GENRE_FOR_FILM = "INSERT INTO film_genre(film_id, genre_id) VALUES (?, ?)";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Film> get(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<Film> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Film save(Film film) {
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        insert(
                UPDATE_QUERY,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getMpa().getId(),
                newFilm.getId()
        );
        return newFilm;
    }

    @Override
    public boolean delete(Long id) {
        return delete(DELETE_QUERY, id);
    }

    @Override
    public boolean putLike(Long id, Long userId) {
        return jdbc.update(ADD_LIKE, userId, id) > 0;
    }

    @Override
    public boolean deleteLike(Long id, Long userId) {
        return jdbc.update(DELETE_LIKE, userId, id) > 0;
    }

    @Override
    public List<Film> getTopFilms(int size) {
        return jdbc.query(FIND_TOP_FILMS, mapper, size);
    }

    @Override
    public void addGenreForFilm(Long id, Long genreId) {
        jdbc.update(ADD_GENRE_FOR_FILM, id, genreId);
    }
}