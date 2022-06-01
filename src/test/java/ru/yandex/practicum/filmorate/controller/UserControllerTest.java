package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.user.UserDTO;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;

    @BeforeEach
    public void init() {
        userController = new UserController(new UserService(new InMemoryUserStorage()));
    }

    @Test
    public void shouldntThrowExceptionWhenCorrectData() {
        UserDTO userDTO = UserDTO.builder()
                .email("test@test.com")
                .id(12)
                .login("testUser")
                .birthday(LocalDate.of(1995, 5, 15))
                .build();

        assertDoesNotThrow(() -> userController.postUser(userDTO));
        assertEquals(1, userController.getAll().size());
    }

    @Test
    public void shouldThrowExceptionWhenPostExistUserWithEmail() {
        UserDTO userDTO = UserDTO.builder()
                .email("test@test.com")
                .login("testUser")
                .birthday(LocalDate.of(1995, 5, 15))
                .build();

        UserDTO userDTO2 = UserDTO.builder()
                .email("test@test.com")
                .login("test2User")
                .birthday(LocalDate.of(1995, 5, 15))
                .build();

        userController.postUser(userDTO);

        AlreadyExistsException ex = assertThrows(AlreadyExistsException.class, () -> userController.postUser(userDTO2));

        assertEquals("User ID = 1 already use email 'test@test.com'", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenPostExistUserWithLogin() {
        UserDTO userDTO = UserDTO.builder()
                .email("test1@test.com")
                .login("testUser")
                .birthday(LocalDate.of(1995, 5, 15))
                .build();

        UserDTO userDTO2 = UserDTO.builder()
                .email("test@test.com")
                .login("testUser")
                .birthday(LocalDate.of(1995, 5, 15))
                .build();

        userController.postUser(userDTO);

        AlreadyExistsException ex = assertThrows(AlreadyExistsException.class, () -> userController.postUser(userDTO2));

        assertEquals("User ID = 1 already use login 'testUser'", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenIncorrectEmail() {
        UserDTO userDTO = UserDTO.builder()
                .email("testtest.com")
                .id(12)
                .login("testUser")
                .birthday(LocalDate.of(1995, 5, 15))
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> userController.postUser(userDTO));
        assertEquals("Email should be valid", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenUpdateWithIncorrectId() {
        UserDTO userDTO = UserDTO.builder()
                .email("test@test.com")
                .login("testUser")
                .birthday(LocalDate.of(1995, 5, 15))
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> userController.putUser(userDTO));
        assertEquals("User id should be present", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenIncorrectLogin() {
        UserDTO userDTO = UserDTO.builder()
                .id(12)
                .email("test@test.com")
                .birthday(LocalDate.of(1995, 5, 15))
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> userController.postUser(userDTO));
        assertEquals("User login can't be blank", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenUserFromFuture() {
        UserDTO userDTO = UserDTO.builder()
                .id(12)
                .login("testUser")
                .email("test@test.com")
                .birthday(LocalDate.of(2100, 5, 15))
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> userController.postUser(userDTO));
        assertEquals("User birthday must be in the past", ex.getMessage());
    }
}