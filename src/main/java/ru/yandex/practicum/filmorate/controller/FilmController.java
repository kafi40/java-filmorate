package ru.yandex.practicum.filmorate.controller;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService service;

    @GetMapping
    public Collection<Film> getFilms() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return service.save(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        return service.update(newFilm);
    }

    @DeleteMapping("/{id}")
    public Film deleteFilm(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("popular?count={count}")
    public Set<Film> getFilmsTop() {
        return null;
    }

    @PutMapping("/{id}/like/{userId}")
    public Film putLike(@PathVariable int id, @PathVariable int userId) {
        return null;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable int id, @PathVariable int userId) {
        return null;
    }

}