package ru.yandex.practicum.filmorate.storage.activity;

import ru.yandex.practicum.filmorate.model.Activity;

import java.util.List;
import java.util.Set;

public interface ActivityStorage {
    List<Activity> getUserFeed(Long userId);

    void save(Activity activity);
}
