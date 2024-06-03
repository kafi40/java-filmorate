package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

public class UserControllerTest {
    static UserController userController;

    @BeforeAll
    public static void beforeAll() {
        userController = new UserController();
    }

    @Test
    public void test() {
        User user = new User("test@test.ru", "test");
        user.setEmail("");
        user.setLogin("");
        System.out.println(user);
        userController.createUser(user);
    }
}
