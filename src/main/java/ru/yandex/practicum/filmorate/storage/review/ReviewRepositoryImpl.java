package ru.yandex.practicum.filmorate.storage.review;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class ReviewRepositoryImpl extends BaseDbStorage<Review> implements ReviewRepository {
    private static final String FIND_BY_ID_QUERY =
            """
            SELECT id, content, is_positive, user_id, film_id,
                (SELECT SUM("score") FROM "user_review_scored"
                    WHERE review_id = ?) as useful
            FROM reviews WHERE id = ?
            """;
    private static final String INSERT_QUERY = "INSERT INTO reviews(content, is_positive, user_id, film_id)" +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE reviews SET content = ?, is_positive = ?, user_id = ?, film_id = ? " +
            "WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM film WHERE id = ?";
    private static final String FIND_USEFUL =
            """
                    SELECT SUM("score") FROM "user_review_scored"
                    WHERE review_id = ?";
                    """;
    private static final String FIND_REVIEWS_WITH_LIMIT =
            """
                    SELECT "id", "content", "is_positive", "user_id", "film_id",
                        (SELECT SUM("score") FROM "user_review_scored"
                        WHERE "review_id" = ?) as useful
                    LIMIT ?
                    """;
    private static final String FIND_REVIEWS_FOR_FILM =
            """
                    SELECT "id", "content", "is_positive", "user_id", "film_id",
                        (SELECT SUM("score") FROM "user_review_scored"
                        WHERE "review_id" = ?) as useful
                    WHERE "film_id" = ?
                    LIMIT ?
                    """;
    private static final String INSERT_SCORE =
                """
                    INSERT INTO "user_review_scored"("review_id", "user_id", "score")
                    VALUES (?, ?, ?)
                    """;
    private static final String DELETE_SCORE =
            """
                    DELETE FROM "user_review_scored"
                    WHERE "review_id" = ? AND "user_id" = ?
                    """;

    public ReviewRepositoryImpl(JdbcTemplate jdbc, RowMapper<Review> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Review> findOne(Long id) {
        return findOne(FIND_BY_ID_QUERY, id, id);
    }

    @Override
    public Review save(Review review) {
        long id = insert(
                INSERT_QUERY,
                review.getContent(),
                review.getIsPositive(),
                review.getUser().getId(),
                review.getFilm().getId()
        );
        review.setId(id);
        return review;
    }

    @Override
    public Review update(Review newReview) {
        insert(
                UPDATE_QUERY,
                newReview.getContent(),
                newReview.getIsPositive(),
                newReview.getUser().getId(),
                newReview.getFilm().getId(),
                newReview.getId()
        );
        return newReview;
    }

    @Override
    public boolean delete(Long id) {
        return delete(DELETE_QUERY, id);
    }

    @Override
    public List<Review> findFilmReviews(Long filmId, int count) {
        if (filmId == null) {
            return jdbc.query(FIND_REVIEWS_WITH_LIMIT, mapper, count);
        }
        return jdbc.query(FIND_REVIEWS_FOR_FILM, mapper, filmId, count);
    }

    @Override
    public boolean updateScore(Long id, Long userId, int score) {
        int i;
        if (score == 0) {
            i = jdbc.update(DELETE_SCORE, userId);
        } else {
            i = jdbc.update(INSERT_SCORE, id, userId, score);
        }
        return i == 1;
    }
}
