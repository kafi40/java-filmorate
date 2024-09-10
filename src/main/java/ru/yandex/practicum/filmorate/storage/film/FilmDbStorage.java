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
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String INSERT_QUERY =
            """
                    INSERT INTO films(name, description, release_date, duration, rating_id)
                                                       VALUES (?, ?, ?, ?, ?)
                    """;
    private static final String UPDATE_QUERY =
            """
                    UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?,
                                                       rating_id = ? WHERE id = ?
                    """;
    private static final String DELETE_QUERY = "DELETE FROM films WHERE id = ?";
    private static final String FIND_TOP_FILMS =
            """
                    SELECT "id", "name", "description", "release_date", "duration", "rating_id" FROM "films" f
                    LEFT JOIN "user_films_liked" ufl ON f."id" = ufl."film_id"
                    GROUP BY "id", "name", "description", "release_date", "duration", "rating_id"
                    ORDER BY COUNT(*) DESC
                    LIMIT ?
                    """;
    private static final String FIND_TOP_FILMS_BY_YEAR_AND_GENRE =
            """
                    SELECT "id", "name", "description", "release_date", "duration", "rating_id" FROM "films" AS f
                    LEFT JOIN "user_films_liked" ufl ON f.ID = ufl.FILM_ID
                    WHERE EXTRACT(YEAR FROM f.RELEASE_DATE) = ? AND f.ID IN
                    (SELECT FILM_ID FROM "film_genres" AS fg WHERE fg.GENRE_ID = ?)
                    GROUP BY f.ID
                    ORDER BY COUNT(*) DESC
                    LIMIT ?;
                    """;
    private static final String FIND_TOP_FILMS_BY_YEAR =
            """
                    SELECT "id", "name", "description", "release_date", "duration", "rating_id" FROM "films" AS f
                    LEFT JOIN "user_films_liked" ufl ON f.ID = ufl.FILM_ID
                    WHERE EXTRACT(YEAR FROM f.RELEASE_DATE) = ?
                    GROUP BY f.ID
                    ORDER BY COUNT(*) DESC
                    LIMIT ?;
                    """;
    private static final String FIND_TOP_FILMS_BY_GENRE =
            """
                    SELECT "id", "name", "description", "release_date", "duration", "rating_id" FROM "films" AS f
                    LEFT JOIN "user_films_liked" ufl ON f.ID = ufl.FILM_ID
                    WHERE f.ID IN
                    (SELECT FILM_ID FROM "film_genres" AS fg WHERE fg.GENRE_ID = ?)
                    GROUP BY f.ID
                    ORDER BY COUNT(*) DESC
                    LIMIT ?;
                    """;
    private static final String ADD_LIKE =
            """
                    INSERT INTO "user_films_liked"("user_id", "film_id") VALUES (?, ?)
                    """;
    private static final String DELETE_LIKE =
            """
                    DELETE FROM "user_films_liked" WHERE "user_id" = ? AND "film_id" = ?
                    """;
    private static final String ADD_GENRE_FOR_FILM =
            """
                    INSERT INTO "film_genres"("film_id", "genre_id") VALUES (?, ?)
                    """;
    private static final String FIND_COMMON_FILMS =
            """
                    SELECT "id", "name", "description", "release_date", "duration", "rating_id" FROM "films" f
                    LEFT JOIN "user_films_liked" ufl ON f."id" = ufl."film_id"
                    WHERE f."id" IN (
                    SELECT "film_id" FROM "user_films_liked"
                    WHERE "user_id" IN (?, ?)
                    GROUP BY "film_id"
                    HAVING COUNT(*) > 1)
                    GROUP BY "id", "name", "description", "release_date", "duration", "rating_id"
                    ORDER BY COUNT(*) DESC
                    """;
    private static final String FIND_FILMS_FOR_DIRECTOR_SORT_BY_YEAR_QUERY =
            """
                    SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rating_id
                    FROM "films" AS f
                    LEFT JOIN "film_directors" AS fd ON f.id = fd.film_id
                    WHERE fd.director_id = ?
                    GROUP BY id, name, description, release_date, duration, rating_id
                    ORDER BY f.release_date
                    """;
    private static final String FIND_FILMS_FOR_DIRECTOR_SORT_BY_LIKES_QUERY =
            """
                    SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rating_id
                    FROM "films" AS f
                    LEFT JOIN "user_films_liked" ufl ON f.id = ufl.film_id
                    JOIN "film_directors" fd ON f.id = fd.film_id
                    WHERE fd.director_id = ?
                    GROUP BY id, name, description, release_date, duration, rating_id
                    ORDER BY COUNT(ufl.user_id) DESC;
                    """;
    private static final String ADD_DIRECTOR_FOR_FILM =
            """
                    INSERT INTO film_directors(film_id, director_id) VALUES (?, ?)
                    """;

    private static final String SEARCH_FILM = "SELECT * FROM films WHERE name LIKE CONCAT('%',?,'%');";

    private static final String SEARCH_FILM_DIRECTOR =
            """
                    SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rating_id
                    FROM films f
                    JOIN film_directors fd ON f.id = fd.film_id
                    JOIN directors d ON fd.director_id = d.id
                    WHERE d.name LIKE CONCAT('%',?,'%')
                    """;

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
    public List<Film> getTopFilms(int count, Long genreId, Integer year) {
        if (genreId == null && year == null) {
            return jdbc.query(FIND_TOP_FILMS, mapper, count);
        } else if (genreId != null && year == null) {
            return jdbc.query(FIND_TOP_FILMS_BY_GENRE, mapper, genreId, count);
        } else if (genreId == null) {
            return jdbc.query(FIND_TOP_FILMS_BY_YEAR, mapper, year, count);
        } else {
            return jdbc.query(FIND_TOP_FILMS_BY_YEAR_AND_GENRE, mapper, year, genreId, count);
        }
    }

    @Override
    public List<Film> getDirectorsFilmSortByYear(Long id) {
        return jdbc.query(FIND_FILMS_FOR_DIRECTOR_SORT_BY_YEAR_QUERY, mapper, id);
    }

    @Override
    public List<Film> getDirectorsFilmSortByLikes(Long id) {
        return jdbc.query(FIND_FILMS_FOR_DIRECTOR_SORT_BY_LIKES_QUERY, mapper, id);
    }

    @Override
    public void addGenreForFilm(Long id, Long genreId) {
        jdbc.update(ADD_GENRE_FOR_FILM, id, genreId);
    }

    @Override
    public List<Film> findCommonFilms(Long userId, Long friendId) {
        return jdbc.query(FIND_COMMON_FILMS, mapper, userId, friendId);
    }

    @Override
    public void addDirectorForFilm(Long id, Long directorId) {
        jdbc.update(ADD_DIRECTOR_FOR_FILM, id, directorId);
    }

    @Override
    public List<Film> getSearchFilm(String query) {
        return jdbc.query(SEARCH_FILM, mapper, query);
    }

    @Override
    public List<Film> getSearchDirector(String query) {
        return jdbc.query(SEARCH_FILM_DIRECTOR, mapper, query);
    }
}
