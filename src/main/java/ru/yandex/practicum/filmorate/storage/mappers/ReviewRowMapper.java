package ru.yandex.practicum.filmorate.storage.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class ReviewRowMapper implements RowMapper<Review> {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    @Override
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
        Review review = new Review();
        review.setId(rs.getLong("id"));
        review.setContent(rs.getString("content"));
        review.setIsPositive(rs.getBoolean("is_positive"));
        review.setUseful(rs.getInt("useful"));

        User user = userStorage.get(rs.getLong("user_id"))
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден"));
        review.setUser(user);
        Film film = filmStorage.get(rs.getLong("film_id"))
                .orElseThrow(() -> new NoSuchElementException("Фильм не найден"));
        review.setFilm(film);
        return review;
    }
}
