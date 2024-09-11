package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.model.director.DirectorDto;
import ru.yandex.practicum.filmorate.controller.model.director.DirectorRequest;
import ru.yandex.practicum.filmorate.service.impl.DirectorServiceImpl;

import java.util.Collection;

@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DirectorController {
    DirectorServiceImpl directorServiceImpl;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<DirectorDto> getDirectors() {
        return directorServiceImpl.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DirectorDto getDirector(@PathVariable("id") Long id) {
        return directorServiceImpl.get(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean deleteDirector(@PathVariable("id") Long id) {
        return directorServiceImpl.delete(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public DirectorDto createDirector(@Valid @RequestBody DirectorRequest director) {
        return directorServiceImpl.save(director);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public DirectorDto updateDirector(@Valid @RequestBody DirectorRequest newDirector) {
        return directorServiceImpl.update(newDirector);
    }
}
