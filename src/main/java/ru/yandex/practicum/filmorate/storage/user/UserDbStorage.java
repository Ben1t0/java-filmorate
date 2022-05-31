package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;

@Repository("userDbStorage")
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> getAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        try {
            int id = simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();
            user.setId(id);
        } catch (DuplicateKeyException duplicateKeyException) {
            String errorMessage = duplicateKeyException.getCause().getMessage();
            errorMessage = errorMessage.substring(errorMessage.indexOf("Unique index"), errorMessage.indexOf("\";"));
            log.warn(errorMessage);
            throw new AlreadyExistsException("Email or login already registered: " + errorMessage);
        }
        return user;
    }

    @Override
    public void update(User user) {
        String updateQuery = "UPDATE users SET login = ?, name = ?, email = ?, birth_date = ? WHERE id = ?";
        int rowNum = 0;
        try {
            rowNum = jdbcTemplate.update(updateQuery, user.getLogin(), user.getName(), user.getEmail(),
                    user.getBirthday().toString(), user.getId());
        } catch (DuplicateKeyException duplicateKeyException) {
            String errorMessage = duplicateKeyException.getCause().getMessage();
            errorMessage = errorMessage.substring(errorMessage.indexOf("Unique index"), errorMessage.indexOf("\";"));
            log.warn(errorMessage);
            throw new AlreadyExistsException("Email or login already registered: " + errorMessage);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        if (rowNum != 1) {
            throw new UserNotFoundException(String.format("User with id = %d not found.", user.getId()));
        }
    }

    @Override
    public void remove(User user) {
        String deleteQuery = "DELETE FROM users where id = ? ";
        if (jdbcTemplate.update(deleteQuery, user.getId()) != 1) {
            throw new UserNotFoundException(String.format("User with id = %d not found.", user.getId()));
        }
    }

    @Override
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        User user;
        try {
            user = jdbcTemplate.queryForObject(sql, this::mapRowToUser, userId);
            log.info("Найден пользователь: {} {}", user.getId(), user.getName());
            return user;
        } catch (EmptyResultDataAccessException ex) {
            log.warn(String.format("User with ID = %d not found", userId));
            throw new UserNotFoundException(String.format("User with ID = %d not found", userId));
        }
    }

    @Override
    public Collection<User> getUsersByIds(Collection<Integer> ids) {
        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));
        return jdbcTemplate.query(String.format("SELECT * FROM users WHERE id IN (%s)", inSql),
                this::mapRowToUser, ids.toArray());
    }

    @Override
    public Collection<User> getUserFriends(int userId) {
        throwIfUserNotFound(userId);
        String friendsQuery = "SELECT * FROM users where id IN (SELECT friend_id FROM friends WHERE user_id = ?)";
        return jdbcTemplate.query(friendsQuery, this::mapRowToUser, userId);
    }

    @Override
    public void addUserFriend(int userId, int friendId) {
        throwIfUserNotFound(userId);
        throwIfUserNotFound(friendId);
        String startFriendshipQuery = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(startFriendshipQuery, userId, friendId);
    }

    @Override
    public void removeUserFriend(int userId, int friendId) {
        String stopFriendshipQuery = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(stopFriendshipQuery, userId, friendId);
    }

    @Override
    public void throwIfUserNotFound(int userId) {
        try{
            jdbcTemplate.queryForObject("SELECT id FROM users WHERE id = ?", Integer.class, userId);
        } catch (EmptyResultDataAccessException ex){
            log.warn(String.format("User with ID = %d not found", userId));
            throw new UserNotFoundException(String.format("User with ID = %d not found", userId));
        }
    }

    private User mapRowToUser(ResultSet rowSet, int rowNum) throws SQLException {
        return User.builder()
                .id(rowSet.getInt("id"))
                .login(rowSet.getString("login"))
                .name(rowSet.getString("name"))
                .email(rowSet.getString("email"))
                .birthday(rowSet.getDate("birth_date").toLocalDate())
                .build();
    }
}
