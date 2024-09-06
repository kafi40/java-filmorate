package ru.yandex.practicum.filmorate.storage.activity;

import ru.yandex.practicum.filmorate.model.Activity;

import java.util.Set;

public interface ActivityStorage {
    Set<Activity> getUserFeed(Long userId);
}
