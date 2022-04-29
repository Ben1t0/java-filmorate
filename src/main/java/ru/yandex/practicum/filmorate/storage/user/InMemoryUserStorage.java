package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer global_id = 0;

    private Integer getNextID() {
        return global_id++;
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public void create(User user) {
        if (user != null) {
            if (user.getId() != null && users.containsKey(user.getId())) {
                throw new AlreadyExistsException(String.format("User with ID = %d already exist!", user.getId()));
            }
            user.setId(getNextID());
            users.put(user.getId(), user);
        }
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
        return users.get(userId);
    }
}
