package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.activity.ActivityDto;
import ru.yandex.practicum.filmorate.dto.activity.ActivityRequest;
import ru.yandex.practicum.filmorate.model.Activity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivityMapper {
    public static ActivityDto MapToActivityDto(Activity activity) {
        ActivityDto activityDto = new ActivityDto();

        return activityDto;
    }

    public static Activity MapToActivity(ActivityRequest request) {
        return new Activity();
    }
}
