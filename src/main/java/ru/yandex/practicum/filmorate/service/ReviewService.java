package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.review.ReviewDto;
import ru.yandex.practicum.filmorate.dto.review.ReviewRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewRepository;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    public ReviewDto get(Long id) {
        return reviewRepository.findOne(id)
                .map(ReviewMapper::mapToReviewDto)
                .orElseThrow(() -> new NotFoundException("Отзыв с ID = " + id + " не найден"));
    }

    public ReviewDto save(ReviewRequest request) {
        if (request.getUserId() < 1 || request.getFilmId() < 1) {
            throw new NotFoundException("ID должен быть больше 0");
        }
        Review review = ReviewMapper.mapToReview(request);
        User user = userStorage.get(request.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь с ID = " + request.getUserId() + " не найден"));
        review.setUser(user);
        Film film = filmStorage.get(request.getFilmId())
                .orElseThrow(() -> new NotFoundException("Фильм с ID = " + request.getFilmId() + " не найден"));
        review.setFilm(film);
        review = reviewRepository.save(review);
        return ReviewMapper.mapToReviewDto(review);
    }

    public ReviewDto update(ReviewRequest request) {
        if (request.getId() == null) {
            throw new ValidationException("ID","Должен быть указан ID");
        }
        Review updateReview = reviewRepository.findOne(request.getId())
                .map(review -> ReviewMapper.updateReviewFields(review, request))
                .orElseThrow(() -> new NotFoundException("Отзыва с ID " + request.getId() + " не найден"));
        User user = userStorage.get(request.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь с ID = " + request.getUserId() + " не найден"));
        updateReview.setUser(user);
        Film film = filmStorage.get(request.getFilmId())
                .orElseThrow(() -> new NotFoundException("Фильм с ID = " + request.getFilmId() + " не найден"));
        updateReview.setFilm(film);
        updateReview = reviewRepository.update(updateReview);
        return ReviewMapper.mapToReviewDto(updateReview);
    }

    public boolean delete(Long id) {
        return reviewRepository.delete(id);
    }

    public List<ReviewDto> getFilmReviews(Long filmId, int count) {
        return reviewRepository.findFilmReviews(filmId, count).stream()
                .map(ReviewMapper::mapToReviewDto)
                .collect(Collectors.toList());
    }

    public ReviewDto updateScore(Long id, Long userId, int score) {
        reviewRepository.updateScore(id, userId, score);
        return reviewRepository.findOne(id)
                .map(ReviewMapper::mapToReviewDto)
                .orElseThrow(() -> new NotFoundException("Отзыв с ID = " + id + " не найден"));
    }
}
