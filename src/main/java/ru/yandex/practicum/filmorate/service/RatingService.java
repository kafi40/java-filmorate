package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.rating.RatingDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.RatingMapper;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingService {
    private final RatingStorage ratingStorage;

    public RatingService(@Qualifier("ratingDbStorage") RatingStorage ratingStorage) {
        this.ratingStorage = ratingStorage;
    }

    public RatingDto get(Long id) {
        return ratingStorage.get(id)
                .map(RatingMapper::mapToMpaDto)
                .orElseThrow(() -> new NotFoundException("Рейтинга с ID = " + id + " не найдено"));
    }

    public List<RatingDto> getAll() {
        return ratingStorage.getAll().stream()
                .map(RatingMapper::mapToMpaDto)
                .collect(Collectors.toList());
    }
}
