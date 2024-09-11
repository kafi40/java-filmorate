package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.model.review.ReviewDto;
import ru.yandex.practicum.filmorate.controller.model.review.ReviewRequest;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {
    ReviewService reviewService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReviewDto getReview(@PathVariable Long id) {
        return reviewService.get(id);
    }

//    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
//    public List<ReviewDto> getReviews() {
//        return reviewService.getAll();
//    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDto createReview(@Valid @RequestBody ReviewRequest request) {
        return reviewService.save(request);
    }

    @PutMapping
    public ReviewDto updateReview(@Valid @RequestBody ReviewRequest request) {
        return reviewService.update(request);
    }

    @DeleteMapping("/{id}")
    public boolean deleteReview(@PathVariable Long id) {
        return reviewService.delete(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewDto> getReviews(@RequestParam(required = false) Long filmId, @RequestParam(defaultValue = "10") int count) {
        return reviewService.getFilmReviews(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public ReviewDto putLike(@PathVariable Long id, @PathVariable Long userId) {
        return reviewService.updateScore(id, userId, 1);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public ReviewDto putDislike(@PathVariable Long id, @PathVariable Long userId) {
        return reviewService.updateScore(id, userId, -1);
    }

    @DeleteMapping(value = {"/{id}/like/{userId}", "/{id}/dislike/{userId}"})
    public ReviewDto deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        return reviewService.updateScore(id, userId, 0);
    }
}
