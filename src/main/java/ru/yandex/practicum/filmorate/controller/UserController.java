package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public Collection<User> getUsers() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        return service.save(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        return service.update(newUser);
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable Long id) {
        return service.delete(id);
    }

    @GetMapping("/{id}/friends")
    public Set<User> getFriends(@PathVariable Long id) {
        return service.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return service.getCommonFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendsId}")
    public boolean addFriend(@PathVariable Long id, @PathVariable Long friendsId) {
        return service.addFriend(id, friendsId);
    }

    @DeleteMapping("/{id}/friends/{friendsId}")
    public boolean deleteFriend(@PathVariable Long id, @PathVariable Long friendsId) {
        return service.deleteFriend(id, friendsId);
    }
}
