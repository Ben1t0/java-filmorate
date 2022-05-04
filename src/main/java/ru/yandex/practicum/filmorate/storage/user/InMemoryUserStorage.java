package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int globalId = 1;

    private Integer getNextID() {
        return globalId++;
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        if (user != null) {
            if (users.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()) ||
                    u.getLogin().equals(user.getLogin()))) {
                throw new AlreadyExistsException(String.format("User with email '%s' or login '%s' already exist!",
                        user.getEmail(), user.getLogin()));
            }
            if (user.getId() == null) {
                user.setId(getNextID());
            } else {
                if (users.containsKey(user.getId())) {
                    throw new AlreadyExistsException(String.format("User with ID = %d already exist!",
                            user.getId()));
                }
            }
            users.put(user.getId(), user);
            return user;
        }
        return null;
    }

    @Override
    public void update(User user) {
        if (user != null && user.getId() != null) {
            users.put(user.getId(), user);
        }
    }

    @Override
    public void remove(User user) {
        if (user != null && user.getId() != null) {
            users.remove(user.getId());
        }
    }

    @Override
    public User findUserById(int userId) {
        if (!users.containsKey(userId)) {
            throw new UserNotFoundException(String.format("User with ID = %d not found", userId));
        }
        return users.get(userId);
    }
}
