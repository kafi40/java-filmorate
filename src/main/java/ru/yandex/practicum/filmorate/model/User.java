package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;
    @NotBlank(message = "Пустой E-mail")
    @Email(message = "Некорректный E-mail")
    private String email;
    @NotBlank(message = "Пустой логин")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,12}$")
    private String login;
    private String name;
    @Past(message = "Некорректная дата рождения")
    private LocalDate birthday;
    private Set<Long> friendsSet;

    public User() {
        friendsSet = new HashSet<>();
    }

}
