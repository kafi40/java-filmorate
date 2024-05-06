package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class User {
    private Long id;
    @NonNull
    @NotBlank(message = "Пустой E-mail")
    @Email(message = "Некорректный E-mail")
    private String email;
    @NonNull
    @NotBlank(message = "Пустой логин")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,12}$")
    private String login;
    private String name;
    @Past(message = "Некорректная дата рождения")
    private LocalDate birthday;

}
