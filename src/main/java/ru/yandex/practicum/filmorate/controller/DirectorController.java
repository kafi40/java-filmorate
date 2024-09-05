package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Director> getDirectors() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Director> getDirector(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean deleteDirector(@PathVariable("id") Long id) {
        return service.deleteById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Director createDirector(@RequestBody Director director) {
        return service.save(director);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Director updateDirector(@RequestBody Director newDirector) {
        return service.update(newDirector);
    }
}
