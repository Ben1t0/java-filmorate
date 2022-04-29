package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.user.User;

public interface UserStorage {

    void create(User user);

    void update(User user);

    void remove(User user);
}
