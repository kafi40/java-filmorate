package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.director.DirectorDto;
import ru.yandex.practicum.filmorate.dto.director.DirectorRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DirectorService {
    private final DirectorStorage directorStorage;


    public DirectorService(@Qualifier("directorDbStorage") DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    public List<DirectorDto> getAll() {
        return directorStorage.findAll()
                .stream()
                .map(DirectorMapper::mapToDirectorDto)
                .collect(Collectors.toList());
    }

    public DirectorDto getById(Long id) {

        return directorStorage.findById(id)
                .map(DirectorMapper::mapToDirectorDto)
                .orElseThrow(() -> new NotFoundException("Режиссёр с ID = " + id + " не найден"));
    }

    public boolean deleteById(Long id) {
        return directorStorage.delete(id);
    }

    public Set<Director> getForFilm(Long id) {
        return directorStorage.getForFilm(id);
    }

    public DirectorDto save(DirectorRequest request) {
        Director director = DirectorMapper.mapToDirector(request);
        director = directorStorage.save(director);
        return DirectorMapper.mapToDirectorDto(director);
    }

    public DirectorDto update(DirectorRequest request) {

        Director updatedDirector = directorStorage.findById(request.getId())
                .map(director -> DirectorMapper.updateDirectorFields(director, request))
                .orElseThrow(() -> new NotFoundException("Режиссёра с id = " + request.getId() + " не существует"));
        updatedDirector = directorStorage.update(updatedDirector);
        return DirectorMapper.mapToDirectorDto(updatedDirector);
    }
}
