package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;
import java.util.List;

@Service
public class RatingService {
    private final RatingStorage repository;

    public RatingService(RatingStorage repository) {
        this.repository = repository;
    }

    public Rating get(Long id) {
        return repository.get(id)
                .orElseThrow(() -> new NotFoundException("Рейтинга с ID = " + id + " не найдено"));
    }

    public List<Rating> getAll() {
        return repository.getAll();
    }
}
