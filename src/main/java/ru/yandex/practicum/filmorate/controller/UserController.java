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

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int userId,
                          @PathVariable("friendId") int friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") int userId,
                             @PathVariable("friendId") int friendId) {
        userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<UserDTO> getFriends(@PathVariable("id") int userId) {
        return userService.getFriends(userId);
    }

    @PostMapping
    public void postUser(@Valid @RequestBody UserDTO userDTO) {
        userService.add(userDTO);
        log.info("User created: {}", userDTO);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<UserDTO> getCommonFriends(@PathVariable("id") int userId,
                                                @PathVariable("otherId") int otherId) {
        return userService.getCommonFriends(userId, otherId);
    }

    @DeleteMapping("/{id}")
    public UserDTO removeUser(@PathVariable("id") int userId) {
        return userService.remove(userId);
    }
}
