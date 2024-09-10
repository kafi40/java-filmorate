package ru.yandex.practicum.filmorate.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.controller.model.director.DirectorDto;
import ru.yandex.practicum.filmorate.controller.model.director.DirectorRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.repository.DirectorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DirectorServiceImpl implements DirectorService {
    DirectorRepository directorRepository;

    public List<DirectorDto> getAll() {
        return directorRepository.findAll()
                .stream()
                .map(DirectorMapper::mapToDirectorDto)
                .collect(Collectors.toList());
    }

    public DirectorDto get(Long id) {
        return directorRepository.findById(id)
                .map(DirectorMapper::mapToDirectorDto)
                .orElseThrow(() -> new NotFoundException("Режиссёр с ID = " + id + " не найден"));
    }

    public boolean delete(Long id) {
        return directorRepository.delete(id);
    }


    public DirectorDto save(DirectorRequest request) {
        Director director = DirectorMapper.mapToDirector(request);
        director = directorRepository.save(director);
        return DirectorMapper.mapToDirectorDto(director);
    }

    public DirectorDto update(DirectorRequest request) {
        Director updatedDirector = directorRepository.findById(request.getId())
                .map(director -> DirectorMapper.updateDirectorFields(director, request))
                .orElseThrow(() -> new NotFoundException("Режиссёра с id = " + request.getId() + " не существует"));
        updatedDirector = directorRepository.update(updatedDirector);
        return DirectorMapper.mapToDirectorDto(updatedDirector);
    }
}
