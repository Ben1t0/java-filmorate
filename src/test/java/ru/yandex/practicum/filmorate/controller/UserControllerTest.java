package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {
    private UserController userController;

    @BeforeEach
    public void init() {
        userController = new UserController();
    }

    @Test
    public void shouldntThrowExceptionWhenCorrectData() {
        User user = User.builder()
                .email("test@test.com")
                .id(12)
                .login("testUser")
                .birthday(LocalDate.of(1995, 5, 15))
                .build();

        assertDoesNotThrow(() -> userController.postUser(user));
        assertEquals(1, userController.getAll().size());
    }

    @Test
    public void shouldThrowExceptionWhenPostExistUser() {
        User user = User.builder()
                .email("test@test.com")
                .id(12)
                .login("testUser")
                .birthday(LocalDate.of(1995, 5, 15))
                .build();
        userController.postUser(user);

        AlreadyExistsException ex = assertThrows(AlreadyExistsException.class, () -> userController.postUser(user));

        assertEquals("User with ID = 12 already exists", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenIncorrectEmail() {
        User user = User.builder()
                .email("testtest.com")
                .id(12)
                .login("testUser")
                .birthday(LocalDate.of(1995, 5, 15))
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> userController.postUser(user));
        assertEquals("Email should be valid", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenIncorrectId() {
        User user = User.builder()
                .email("test@test.com")
                .login("testUser")
                .birthday(LocalDate.of(1995, 5, 15))
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> userController.postUser(user));
        assertEquals("User id should be present", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenIncorrectLogin() {
        User user = User.builder()
                .id(12)
                .email("test@test.com")
                .birthday(LocalDate.of(1995, 5, 15))
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> userController.postUser(user));
        assertEquals("User login can't be blank", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenUserFromFuture() {
        User user = User.builder()
                .id(12)
                .login("testUser")
                .email("test@test.com")
                .birthday(LocalDate.of(2100, 5, 15))
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> userController.postUser(user));
        assertEquals("User birthday must be in the past", ex.getMessage());
    }
}