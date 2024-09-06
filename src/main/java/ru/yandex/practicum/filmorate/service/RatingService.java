package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.rating.RatingDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.RatingMapper;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;

import ru.yandex.practicum.filmorate.controller.model.rating.RatingDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingService {
    private final RatingStorage ratingStorage;

public interface RatingService {
    RatingDto get(Long id);

    List<RatingDto> getAll();
}
