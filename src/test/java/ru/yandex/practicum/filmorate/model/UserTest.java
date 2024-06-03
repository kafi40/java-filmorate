package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void shouldCreateNewFilm() {
        User user = new User("test@test.ru", "test");
        assertNotNull(user, "Ошибка создания объекта");
    }

    @Test
    public void shouldGetNullPointerExceptionForEmail() {
        User user = new User("test@test.ru", "test");
        assertThrows(NullPointerException.class, () -> user.setEmail(null));
    }

    @Test
    public void shouldGetNullPointerExceptionForLogin() {
        User user = new User("test@test.ru", "test");
        assertThrows(NullPointerException.class, () -> user.setLogin(null));
    }
}
