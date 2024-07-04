package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService service;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Genre get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Genre> getAll() {
        return service.getAll();
    }
}
