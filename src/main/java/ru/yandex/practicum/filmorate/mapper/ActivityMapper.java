package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.activity.ActivityDto;
import ru.yandex.practicum.filmorate.dto.activity.ActivityRequest;
import ru.yandex.practicum.filmorate.model.Activity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ActivityMapper {
    public static ActivityDto MapToActivityDto(Activity activity) {
        ActivityDto activityDto = new ActivityDto();
        activityDto.setEntityId(activity.getEntityId());
        activityDto.setTimestamp(activity.getTimestamp());
        activityDto.setOperation(activity.getOperation());
        activityDto.setUserId(activity.getUserId());
        activityDto.setEventId(activity.getEventId());
        activityDto.setEventType(activity.getEventType());

        return activityDto;
    }

    public static Activity MapToActivity(ActivityRequest request) {
        return new Activity();
    }
}
