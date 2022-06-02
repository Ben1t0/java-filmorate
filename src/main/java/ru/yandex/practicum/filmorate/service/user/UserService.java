package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.model.user.UserDTO;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage storage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage storage) {
        this.storage = storage;
    }

    public Collection<UserDTO> getAll() {
        return storage.getAll().stream().map(UserDTO::fromUser).collect(Collectors.toList());
    }

    public UserDTO create(UserDTO userDTO) {
        validate(userDTO);
        return UserDTO.fromUser(storage.create(userDTO.asUser()));
    }

    public UserDTO update(UserDTO userDTO) {
        if (userDTO.getId() == null) {
            log.warn("UPDATE METHOD: User ID wasn't present");
            throw new ValidationException("User id should be present", "id");
        }
        validate(userDTO);
        storage.update(userDTO.asUser());
        return userDTO;
    }

    public UserDTO remove(int userId) {
        if (userId < 0) {
            log.warn("Film ID below 0");
            throw new ValidationException("Film id should be positive", "id");
        }
        User user = storage.getUserById(userId);
        storage.remove(user);
        return UserDTO.fromUser(user);
    }

    public UserDTO findUserById(int userId) {
        return UserDTO.fromUser(storage.getUserById(userId));
    }

    public Collection<UserDTO> addFriend(int userId, int friendId) {
        storage.addUserFriend(userId, friendId);
        return getFriends(userId);
    }

    public Collection<UserDTO> removeFriend(int userId, int friendId) {
        storage.removeUserFriend(userId, friendId);
        return getFriends(userId);
    }

    public Collection<UserDTO> getFriends(int userId) {
        return storage.getUserFriends(userId).stream()
                .map(UserDTO::fromUser)
                .collect(Collectors.toList());
    }

    public Collection<UserDTO> getCommonFriends(int user1Id, int user2Id) {
        Collection<User> friends1 = storage.getUserFriends(user1Id);
        Collection<User> friends2 = storage.getUserFriends(user2Id);
        return friends1.stream().filter(friends2::contains).map(UserDTO::fromUser).collect(Collectors.toList());
    }

    private void validate(UserDTO userDTO) {
        if (userDTO.getEmail() == null ||
                !Pattern.compile("^(.+)@(\\S+)$").matcher(userDTO.getEmail()).matches()) {
            log.warn("Email is invalid");
            throw new ValidationException("Email should be valid", "email");
        }
        if (userDTO.getLogin() == null || userDTO.getLogin().isBlank()) {
            log.warn("User login is blank");
            throw new ValidationException("User login can't be blank", "login");
        } else if (userDTO.getLogin().trim().contains(" ")) {
            log.warn("User login contains spaces");
            throw new ValidationException("User login can't contain spaces", "login");
        }

        if (userDTO.getBirthday() == null || userDTO.getBirthday().isAfter(LocalDate.now())) {
            log.warn("User birthday is in the future");
            throw new ValidationException("User birthday must be in the past", "birthday");
        }
        if (userDTO.getName() == null || userDTO.getName().isBlank()) {
            userDTO.setName(userDTO.getLogin());
        }
    }
}
