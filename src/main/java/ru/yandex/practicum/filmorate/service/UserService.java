package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.controller.model.user.UserDto;
import ru.yandex.practicum.filmorate.controller.model.user.UserRequest;

import java.util.Set;

public interface UserService extends BaseService<UserDto, UserRequest> {
    Set<UserDto> getFriends(Long id);

    Set<UserDto> getCommonFriends(Long id, Long otherId);

    boolean addFriend(Long id, Long otherId);

    boolean deleteFriend(Long id, Long otherId);
}
