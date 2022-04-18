package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

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
        validateAndAdd(user);
        log.info("User updated: {}", user);
    }

    @PostMapping
    public void postUser(@Valid @RequestBody User user) {
        if (users.contains(user)) {
            log.warn("User with ID = {} already exists", user.getId());
            throw new AlreadyExistsException("User with ID = " + user.getId() + " already exists");
        }
        validateAndAdd(user);
        log.info("User created: {}", user);
    }

    private void validateAndAdd(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.add(user);
    }
}
