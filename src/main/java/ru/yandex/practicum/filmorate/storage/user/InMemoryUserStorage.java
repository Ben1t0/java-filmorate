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
            for (User u : users.values()) {
                if (u.getEmail().equals(user.getEmail())) {
                    throw new AlreadyExistsException(String.format("User ID = %d already use email '%s'",
                            u.getId(), user.getEmail()));
                }
                if (u.getLogin().equals(user.getLogin())) {
                    throw new AlreadyExistsException(String.format("User ID = %d already use login '%s'",
                            u.getId(), user.getLogin()));
                }
            }
            user.setId(getNextID());
            users.put(user.getId(), user);
            return user;
        }
        return null;
    }

    @Override
    public void update(User user) {
        if (user != null && user.getId() != null) {
            for (User u : users.values()) {
                if (!u.getId().equals(user.getId())) {
                    if (u.getEmail().equals(user.getEmail())) {
                        throw new AlreadyExistsException(String.format("User ID = %d already use email '%s'",
                                u.getId(), user.getEmail()));
                    }
                    if (u.getLogin().equals(user.getLogin())) {
                        throw new AlreadyExistsException(String.format("User ID = %d already use login '%s'",
                                u.getId(), user.getLogin()));
                    }
                }
            }
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
