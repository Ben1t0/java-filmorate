package ru.yandex.practicum.filmorate.model.user;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @EqualsAndHashCode.Include
    private Integer id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private final Map<Integer, User> friends = new HashMap<>();

    public void setLogin(String login) {
        this.login = login.trim();
    }

    public void addFriend(User friend) {
        friends.put(friend.id, friend);
    }

    public void removeFriend(User friend) {
        friends.remove(friend.getId());
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("email", email);
        values.put("login", login);
        values.put("name", name);
        values.put("birth_date", birthday.toString());
        return values;
    }

}
