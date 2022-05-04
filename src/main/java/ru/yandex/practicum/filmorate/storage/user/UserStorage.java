package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.Collection;

public interface UserStorage {

    Collection<User> getAll();
    void create(User user);

    void update(User user);

    void remove(User user);

    User findUserById(int userId);
}
