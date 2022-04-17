package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Set<User> users = new HashSet<>();

    @GetMapping
    public  Set<User> getAll() {
        return users;
    }

    @PutMapping
    public void putUser(@RequestBody User user) {
        users.add(user);
    }

    @PostMapping
    public void postUser(@RequestBody User user) {
        if (users.contains(user)) {
            throw new AlreadyExistsException("Пользователь с таким ID уже существует.");
        }
        users.add(user);
    }
}
