package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public Collection<UserDTO> getAll() {
        return storage.getAll().stream().map(User::toDTO).collect(Collectors.toList());
    }

    public UserDTO add(UserDTO userDTO) {
        validate(userDTO);
        storage.create(User.fromDTO(userDTO));
        return userDTO;
    }

    public UserDTO update(UserDTO userDTO) {
        if (userDTO.getId() == null) {
            log.warn("UPDATE METHOD: User ID wasn't present");
            throw new ValidationException("User id should be present", "id");
        }
        validate(userDTO);
        storage.update(User.fromDTO(userDTO));
        return userDTO;
    }

    private void validate(UserDTO userDTO) {
        if (userDTO.getId() != null && userDTO.getId() < 0) {
            log.warn("UPDATE METHOD: User ID below 0");
            throw new ValidationException("User id should be positive", "id");
        }
        if (userDTO.getEmail() == null || !Pattern.compile("^(.+)@(\\S+)$").matcher(userDTO.getEmail()).matches()) {
            log.warn("Email is invalid");
            throw new ValidationException("Email should be valid", "email");
        }
        if (userDTO.getLogin() == null || userDTO.getLogin().isBlank()) {
            log.warn("User login is blank");
            throw new ValidationException("User login can't be blank", "login");
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
