package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.director.DirectorDto;
import ru.yandex.practicum.filmorate.dto.director.DirectorRequest;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.Collection;

@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<DirectorDto> getDirectors() {
        return directorService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DirectorDto getDirector(@PathVariable("id") Long id) {
        return directorService.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean deleteDirector(@PathVariable("id") Long id) {
        return directorService.deleteById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public DirectorDto createDirector(@RequestBody DirectorRequest director) {
        return directorService.save(director);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public DirectorDto updateDirector(@RequestBody DirectorRequest newDirector) {
        return directorService.update(newDirector);
    }
}