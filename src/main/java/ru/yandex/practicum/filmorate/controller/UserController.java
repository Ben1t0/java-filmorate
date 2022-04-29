package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.user.UserDTO;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<UserDTO> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable("id") int userId) {
        return userService.findUserById(userId);
    }

    @PutMapping
    public void putUser(@Valid @RequestBody UserDTO userDTO) {
        userService.update(userDTO);
        log.info("User updated: {}", userDTO);
    }

    @PostMapping
    public void postUser(@Valid @RequestBody UserDTO userDTO) {
        userService.add(userDTO);
        log.info("User created: {}", userDTO);
    }

    @DeleteMapping("/{id}")
    public UserDTO removeUser(@PathVariable("id") int userId) {
        return userService.remove(userId);
    }
}
