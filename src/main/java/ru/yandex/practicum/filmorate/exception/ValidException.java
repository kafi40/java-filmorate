package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class ValidException extends IllegalArgumentException {
    private final String parameter;
    private final String reason;

    public ValidException(String parameter, String reason) {
        this.parameter = parameter;
        this.reason = reason;
    }

}
