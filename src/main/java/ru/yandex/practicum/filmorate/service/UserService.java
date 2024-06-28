package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage repository;

    public UserService(@Qualifier("userDbStorage") UserStorage repository) {
        this.repository = repository;
    }

    public UserDto get(Long id) {
        return repository.get(id)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID = " + id + " не найден"));
    }

    public List<UserDto> getAll() {
        return repository.getAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto save(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        User newUser = repository.save(user);
        return UserMapper.mapToUserDto(newUser);
    }

    public UserDto update(User newUser) {
        if (newUser.getId() == null) {
            throw new ValidationException("Должен быть указан ID");
        }
        if (repository.get(newUser.getId()).isPresent()) {
            User oldUser = repository.get(newUser.getId()).get();
            oldUser.setLogin(newUser.getLogin());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setBirthday(newUser.getBirthday());
            if (!(newUser.getName() == null || newUser.getName().isBlank())) {
                oldUser.setName(newUser.getName());
            }
            User user = repository.update(oldUser);
            return UserMapper.mapToUserDto(user);
        }
        throw new NotFoundException("Пользователя с ID " + newUser.getId() + " не существует");
    }

    public boolean delete(Long id) {
        return repository.delete(id);
    }

    public Set<User> getFriends(Long id) {
        checkId(id);
        return repository.getFriends(id);
    }

    public Set<User> getCommonFriends(Long id, Long otherId) {
        checkId(id);
        checkId(otherId);
        return repository.getCommonFriends(id, otherId);
    }

    public boolean addFriend(Long id, Long otherId) {
        if (id.equals(otherId)) {
            throw new RuntimeException("Нельзя добавить самого себя в друзья");
        }
        checkId(id);
        checkId(otherId);
       return repository.addFriend(id, otherId);
    }

    public boolean deleteFriend(Long id, Long otherId) {
        if (id.equals(otherId)) {
            throw new RuntimeException("Нельзя удалить самого себя из друзей");
        }
        checkId(id);
        checkId(otherId);
        return repository.deleteFriend(id, otherId);
    }

    public void checkId(Long id) {
        if (repository.get(id).isEmpty()) {
            throw new NotFoundException("Объекта с ID " + id + " не существует");
        }
    }

}
