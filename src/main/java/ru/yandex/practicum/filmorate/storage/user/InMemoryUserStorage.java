package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.user.User;

@Component
public class InMemoryUserStorage implements UserStorage{
    @Override
    public void create(User user) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public void remove(User user) {

    }
}
