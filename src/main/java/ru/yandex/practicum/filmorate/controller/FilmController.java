package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.RequestFilmDto;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<FilmDto> getFilms() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FilmDto getFilm(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDto createFilm(@Valid @RequestBody RequestFilmDto request) {
        return service.save(request);
    }

    @PutMapping
    public FilmDto updateFilm(@Valid @RequestBody RequestFilmDto request) {
        return service.update(request);
    }

    @DeleteMapping("/{id}")
    public boolean deleteFilm(@PathVariable Long id) {
        return service.delete(id);
    }

    @GetMapping("/popular")
    public List<FilmDto> getFilmsTop(@RequestParam(defaultValue = "10") int size) {
        if (size < 1) {
            throw new ValidationException("size", "Некорректный размер выборки. Размер должен быть больше нуля");
        }
        return service.getTopFilms(size);
    }

    @PutMapping("/{id}/like/{userId}")
    public boolean putLike(@PathVariable Long id, @PathVariable Long userId) {
        return service.putLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public boolean deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        return service.deleteLike(id, userId);
    }
}