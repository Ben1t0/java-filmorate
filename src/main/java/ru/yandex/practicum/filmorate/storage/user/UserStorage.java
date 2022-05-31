package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.Collection;

public interface UserStorage {

    Collection<User> getAll();
    User create(User user);

    void update(User user);

    void remove(User user);

    User getUserById(int userId);

    Collection<User> getUsersByIds(Collection<Integer> ids);

    void addUserFriend(int userId, int friendId);
    void removeUserFriend(int userId, int friendId);

    Collection<User> getUserFriends(int userId);

    void throwIfUserNotFound(int userId);
}
