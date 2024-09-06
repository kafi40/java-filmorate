package ru.yandex.practicum.filmorate.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.model.user.UserRequest;
import ru.yandex.practicum.filmorate.controller.model.user.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.controller.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.util.Util;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    public UserDto get(Long id) {
        return userRepository.get(id)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID = " + id + " не найден"));
    }

    public List<UserDto> getAll() {
        return userRepository.getAll().stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto save(UserRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            request.setName(request.getLogin());
        }
        User user = UserMapper.mapToUser(request);
        user = userRepository.save(user);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto update(UserRequest request) {
        if (request.getId() == null) {
            throw new ValidationException("ID","Должен быть указан ID");
        }
        User updatedUser = userRepository.get(request.getId())
                .map(user -> UserMapper.updateUserFields(user, request))
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + request.getId() + " не найден"));
        updatedUser = userRepository.update(updatedUser);
        return UserMapper.mapToUserDto(updatedUser);
    }

    public boolean delete(Long id) {
        return userRepository.delete(id);
    }

    public Set<UserDto> getFriends(Long id) {
        Util.checkId(userRepository, id);
        return userRepository.getFriends(id).stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toSet());
    }

    public Set<UserDto> getCommonFriends(Long id, Long otherId) {
        Util.checkId(userRepository, id, otherId);
        return userRepository.getCommonFriends(id, otherId).stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toSet());
    }

    public boolean addFriend(Long id, Long otherId) {
        if (id.equals(otherId)) {
            throw new RuntimeException("Нельзя добавить самого себя в друзья");
        }
        Util.checkId(userRepository, id, otherId);
        if (userRepository.isFriendRequest(id, otherId)) {
            return userRepository.acceptRequest(id, otherId);
        }
       return userRepository.addFriend(id, otherId);
    }

    public boolean deleteFriend(Long id, Long otherId) {
        if (id.equals(otherId)) {
            throw new RuntimeException("Нельзя удалить самого себя из друзей");
        }
        Util.checkId(userRepository, id, otherId);
        if (userRepository.isFriend(id, otherId)) {
            return userRepository.removeRequest(id, otherId);
        }
        return userRepository.deleteFriend(id, otherId);
    }
}
