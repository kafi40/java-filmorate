package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.user.UserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage,
                       @Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("genreDbStorage") GenreStorage genreStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.genreStorage = genreStorage;
    }

    public UserDto get(Long id) {
        return userStorage.get(id)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID = " + id + " не найден"));
    }

    public List<UserDto> getAll() {

        return userStorage.getAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto save(UserRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            request.setName(request.getLogin());
        }
        User user = UserMapper.mapToUser(request);
        user = userStorage.save(user);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto update(UserRequest request) {
        if (request.getId() == null) {
            throw new ValidationException("ID", "Должен быть указан ID");
        }
        User updatedUser = userStorage.get(request.getId())
                .map(user -> UserMapper.updateUserFields(user, request))
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + request.getId() + " не найден"));
        updatedUser = userStorage.update(updatedUser);
        return UserMapper.mapToUserDto(updatedUser);
    }

    public boolean delete(Long id) {
        return userStorage.delete(id);
    }

    public Set<UserDto> getFriends(Long id) {
        checkId(id);
        return userStorage.getFriends(id).stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toSet());
    }

    public Set<UserDto> getCommonFriends(Long id, Long otherId) {
        checkId(id);
        checkId(otherId);
        return userStorage.getCommonFriends(id, otherId).stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toSet());
    }

    public boolean addFriend(Long id, Long otherId) {
        if (id.equals(otherId)) {
            throw new RuntimeException("Нельзя добавить самого себя в друзья");
        }
        checkId(id);
        checkId(otherId);
        if (userStorage.isFriendRequest(id, otherId)) {
            return userStorage.acceptRequest(id, otherId);
        }
        return userStorage.addFriend(id, otherId);
    }

    public boolean deleteFriend(Long id, Long otherId) {
        if (id.equals(otherId)) {
            throw new RuntimeException("Нельзя удалить самого себя из друзей");
        }
        checkId(id);
        checkId(otherId);
        if (userStorage.isFriend(id, otherId)) {
            return userStorage.removeRequest(id, otherId);
        }
        return userStorage.deleteFriend(id, otherId);
    }

    public void checkId(Long id) {
        if (userStorage.get(id).isEmpty()) {
            throw new NotFoundException("Объекта с ID " + id + " не существует");
        }
    }

    public List<Film> getRecommendations(int userId) {
        List<Long> bestRepetitionUserIds = userStorage.getBestRepetitionUserIds(userId);

        if (bestRepetitionUserIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<Film> recommendationsFilms = bestRepetitionUserIds.stream()
                .flatMap(userIdBestRep -> filmStorage.getRecommendations(userId, userIdBestRep).stream())
                .toList();

        if (!recommendationsFilms.isEmpty()) {
            recommendationsFilms.stream()
                    .filter(film -> !genreStorage.getForFilm(film.getId()).isEmpty())
                    .forEach(film -> film.setGenres(genreStorage.getForFilm(film.getId())));
        }

        return recommendationsFilms;
    }
}
