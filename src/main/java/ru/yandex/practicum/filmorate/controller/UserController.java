package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.user.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Set<User> users = new HashSet<>();

    @GetMapping
    public Set<User> getAll() {
        return users;
    }

    @PutMapping
    public void putUser(@Valid @RequestBody User user) {
        validate(user);
        users.add(user);
        log.info("User updated: {}", user);
    }

    @PostMapping
    public void postUser(@Valid @RequestBody User user) {
        validate(user);
        if (users.contains(user)) {
            log.warn("User with ID = {} already exists", user.getId());
            throw new AlreadyExistsException("User with ID = " + user.getId() + " already exists");
        }

        users.add(user);
        log.info("User created: {}", user);
    }


    private void validate(User user) {
        if (user.getId() == null) {
            log.warn("User id should be present");
            throw new ValidationException("User id should be present", "id");
        }
        if (user.getEmail() == null || !Pattern.compile("^(.+)@(\\S+)$").matcher(user.getEmail()).matches()) {
            log.warn("Email should be valid");
            throw new ValidationException("Email should be valid", "email");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn("User login can't be blank");
            throw new ValidationException("User login can't be blank", "login");
        }
        if (user.getBirthday() == null
                || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("User birthday must be in the past");
            throw new ValidationException("User birthday must be in the past", "birthday");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
