package ru.yandex.practicum.filmorate.repository.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.BaseRepository;
import ru.yandex.practicum.filmorate.repository.ReviewRepository;

import java.util.List;
import java.util.Optional;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@FieldNameConstants
public class ReviewRepositoryImpl extends BaseRepository<Review> implements ReviewRepository {
    static String FIND_BY_ID_QUERY =
            """
                    SELECT id, content, is_positive, user_id, film_id,
                        (SELECT SUM("score") FROM "user_review_scored"
                            WHERE review_id = id) as useful
                    FROM reviews WHERE id = ?
                    """;
    static String INSERT_QUERY = "INSERT INTO reviews(content, is_positive, user_id, film_id)" +
            "VALUES (?, ?, ?, ?)";
    static String UPDATE_QUERY = "UPDATE reviews SET content = ?, is_positive = ?, user_id = ?, film_id = ? " +
            "WHERE id = ?";
    static String DELETE_QUERY = "DELETE FROM reviews WHERE id = ?";
    static String FIND_REVIEWS_WITH_LIMIT =
            """
                    SELECT "id", "content", "is_positive", "user_id", "film_id",
                        (SELECT SUM("score") FROM "user_review_scored"
                        WHERE "review_id" = "id") as useful
                    FROM "reviews"
                    LIMIT ?
                    """;
    static String FIND_REVIEWS_FOR_FILM =
            """
                    SELECT "id", "content", "is_positive", "user_id", "film_id",
                        (SELECT SUM("score") FROM "user_review_scored"
                        WHERE "review_id" = "id") as useful
                    FROM "reviews"
                    WHERE "film_id" = ?
                    LIMIT ?
                    """;
    static String FIND_SCORE =
            """
                    SELECT * FROM "user_review_scored"
                    WHERE "review_id" = ? AND "user_id" = ?
                    """;
    static String INSERT_SCORE =
                """
                    INSERT INTO "user_review_scored"("review_id", "user_id", "score")
                    VALUES (?, ?, ?)
                    """;

    static String UPDATE_SCORE =
            """
                    UPDATE "user_review_scored"
                    SET "score" = ?
                    WHERE "review_id" = ? AND "user_id" = ?
                    """;
    static String DELETE_SCORE =
            """
                    DELETE FROM "user_review_scored"
                    WHERE "review_id" = ? AND "user_id" = ?
                    """;

    public ReviewRepositoryImpl(JdbcTemplate jdbc, RowMapper<Review> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Review> findOne(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
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
        boolean hasRow = jdbc.queryForRowSet(FIND_SCORE, id, userId).next();
        if (score == 0) {
            i = jdbc.update(DELETE_SCORE, id, userId);
        } else {
            if (hasRow) {
                i = jdbc.update(UPDATE_SCORE, score, id, userId);
            } else {
                i = jdbc.update(INSERT_SCORE, id, userId, score);
            }
        }
        return i == 1;
    }
}
