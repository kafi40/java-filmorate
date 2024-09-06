package ru.yandex.practicum.filmorate.controller.model.review;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReviewRequest {
    private Long reviewId;
    @NotBlank
    private String content;
    private Boolean isPositive;
    private Long userId;
    private Long filmId;
    private Integer useful;
}
