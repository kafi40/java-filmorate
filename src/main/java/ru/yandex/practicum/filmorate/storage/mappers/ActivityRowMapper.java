package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Activity;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;


import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ActivityRowMapper implements RowMapper<Activity> {

    @Override
    public Activity mapRow(ResultSet rs, int rowNum) throws SQLException {
        Activity activity = new Activity();
        activity.setEntityId(rs.getLong("entity_id"));
        activity.setTimestamp(rs.getTimestamp("timestamp"));
        activity.setOperation(Operation.valueOf(rs.getString("operation").toUpperCase()));
        activity.setEventId(rs.getLong("event_id"));
        activity.setUserId(rs.getLong("user_id"));
        activity.setEventType(EventType.valueOf(rs.getString("event_type").toUpperCase()));

        return activity;
    }
}
