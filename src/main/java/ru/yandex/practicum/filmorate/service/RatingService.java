package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;
import java.util.List;

@Service
public class RatingService {
    private final RatingStorage ratingStorage;

    public RatingService(RatingStorage ratingStorage) {
        this.ratingStorage = ratingStorage;
    }

    public Rating get(Long id) {
        return ratingStorage.get(id)
                .orElseThrow(() -> new NotFoundException("Рейтинга с ID = " + id + " не найдено"));
    }

    public List<Rating> getAll() {
        return ratingStorage.getAll();
    }
}
