package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.user.NewOrUpdateUser;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidException;
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

    public UserDto save(NewOrUpdateUser request) {
        if (request.getName() == null || request.getName().isBlank()) {
            request.setName(request.getLogin());
        }
        User user = UserMapper.mapToUser(request);
        user = repository.save(user);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto update(NewOrUpdateUser request) {
        if (request.getId() == null) {
            throw new ValidException("ID","Должен быть указан ID");
        }
        User updatedUser = repository.get(request.getId())
                .map(user -> UserMapper.updateUserFields(user, request))
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + request.getId() + " не найден"));
        updatedUser = repository.update(updatedUser);
        return UserMapper.mapToUserDto(updatedUser);
    }

    public boolean delete(Long id) {
        return repository.delete(id);
    }

    public Set<UserDto> getFriends(Long id) {
        checkId(id);
        return repository.getFriends(id).stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toSet());
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
        if (repository.isFriendRequest(id, otherId)) {
            return repository.acceptRequest(id, otherId);
        }
       return repository.addFriend(id, otherId);
    }

    public boolean deleteFriend(Long id, Long otherId) {
        if (id.equals(otherId)) {
            throw new RuntimeException("Нельзя удалить самого себя из друзей");
        }
        checkId(id);
        checkId(otherId);
        if (repository.isFriend(id, otherId)) {
            return repository.removeRequest(id, otherId);
        }
        return repository.deleteFriend(id, otherId);
    }

    public void checkId(Long id) {
        if (repository.get(id).isEmpty()) {
            throw new NotFoundException("Объекта с ID " + id + " не существует");
        }
    }

}
