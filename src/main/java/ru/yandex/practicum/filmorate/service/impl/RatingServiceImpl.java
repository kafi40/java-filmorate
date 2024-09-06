package ru.yandex.practicum.filmorate.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.model.rating.RatingDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.controller.mapper.RatingMapper;
import ru.yandex.practicum.filmorate.repository.RatingRepository;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingServiceImpl implements RatingService {
    RatingRepository ratingRepository;

    public RatingDto get(Long id) {
        return ratingRepository.get(id)
                .map(RatingMapper::mapToMpaDto)
                .orElseThrow(() -> new NotFoundException("Рейтинга с ID = " + id + " не найдено"));
    }

    public List<RatingDto> getAll() {
        return ratingRepository.getAll().stream()
                .map(RatingMapper::mapToMpaDto)
                .collect(Collectors.toList());
    }
}
